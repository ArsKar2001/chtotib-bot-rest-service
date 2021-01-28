<?php
require_once __DIR__.'/base/Settings.php';
require_once __DIR__.'/vendor/autoload.php';
require_once __DIR__.'/base/UUID.php';
require_once __DIR__.'/helpers/FileStorage.php';
require_once __DIR__."/helpers/Converter.php";
require_once __DIR__."/helpers/Watermarker.php";

use helpers\Converter;
use helpers\Watermarker;

$start = microtime(true);
//get files
$converter = new Converter();
$converter->query = "SELECT * FROM event_service_input_files WHERE add_to_order = false and file_processed = false";
$result = $converter->getFiles();
$n = 0; //Обработано файлов
$k = 0; //Добавлено файлов
//Перебор результата
while ($file = pg_fetch_assoc($result)) {
    $n++;
    $format = false;
    $convertFormat = false;
    $uuid = $file['fs_uuid'];
    $file_name = $file['file_name'];

    if ($file_name == 'archive-description.xml' || in_array(end(explode('.', $file_name)), ['sgn', 'zip', 'sig0'])) {
        $converter->updateTable($file['fs_uuid']); //Передать значение, что файл обработан;
        continue;
    }

    $cpgu_order = $file['cpgu_order'];
    echo $cpgu_order . "\n";
//    echo  $file_name;
    //Загружаем документ и проверяем его тип
    $path = $converter->saveDocument($uuid);


    if(!$path){
        $converter->saveLog($uuid, 'Формат не поддерживается', true);
        $converter->updateTable($file['fs_uuid']); //Передать значение, что файл обработан;
        continue;
    }


    //Конвертируем в зависимости от формата
    if($path['format'] == 'djvu'){
//        echo "Найден djvu";
        $pathNew = $converter->convertDjvu($path['path']);
        if($pathNew)
            $convertFormat = true;
        else
            $converter->saveLog($uuid, 'Ошибка при конвертации в djvu' , true);


    }
    elseif ($path['format'] == 'doc' || $path['format'] == 'docx' || $path['format'] == 'rtf') {
//        echo "Найден doc(x)";
        $pathNew = $converter->convertDoc($path['path']);
        if($pathNew)
            $convertFormat = true;
        else
            $converter->saveLog($uuid, 'Ошибка при конвертации в doc(x)/rtf' , true);

    }

    elseif ($path['format'] == 'pdf' || $path['format'] == 'jpg' || $path['format'] == 'jpeg'){
//        echo "Найден jpg/pdf";
        $format = $path['format'];
        $pathNew = $path['path'];
    }
    else
        $converter->updateTable($file['fs_uuid']); //Передать значение, что файл обработан;

    //Добавляем штамп, если есть подпись документа
    if($sigRow =  $converter->searchSig($cpgu_order,$file_name)){
        echo "sign is found. file " . $file_name . "\n";
        //Извлекаем и парсим подпись
        $getDataSign = $converter->getDataSign($sigRow['fs_uuid']);
        $dataSign = $converter->_getSignInfo($getDataSign);
        if (count($dataSign) == 0)
            $addSign = false;
//       var_dump($dataSign);
        else {
            $pathNew = Watermarker::watermark($pathNew,$dataSign);
            $addSign = true;
        }
    }
    else
        $addSign = false;

    //Сохраняем файл, если это pdf/jpg/jpeg с подписью ИЛИ doc(x)/djvu
 if((in_array($path['format'], ['pdf','jpg','jpeg']) && $addSign) || $convertFormat)
 {
    $fileName = $file_name.'(штамп).'.( ($format) ? $format : 'pdf');
//   echo PHP_EOL.$fileName.PHP_EOL;
    $uuid = $converter->setFile($pathNew,$fileName );
//        var_dump($uuid);
    if($uuid){
     $converter->updateTable($file['fs_uuid']);
     if(!$converter->insertFile($uuid,$fileName,$cpgu_order)){
        $converter->saveLog($uuid, 'Не удалось добавить документ' , true);
    }
    else {
        $k++;
    }
}
}
else{
    @unlink($pathNew);
}

    $converter->updateTable($file['fs_uuid']); //Передать значение, что файл обработан

}

echo 'Обработано ' .$n. ' файлов'.PHP_EOL;
echo  'Добавлено ' . $k . ' файлов';
$time = number_format(microtime(true) - $start,1);
echo 'Время выполненеия '.$time.' сек. ';
