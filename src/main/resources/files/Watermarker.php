<?php

namespace helpers;

use Ajaxray\PHPWatermark\Watermark;
use helpers\Converter;

/**
 * Class Watermarker
 *
 * @package helpers
 */
class Watermarker
{
    /**
     * @param string $fileId
     * @param string $fileName
     * @param array  $certData
     *
     * @return string
     * @throws \ErrorException
     */
    public static function watermark($tmpName,$certData = [])
    {

//        $tmpName = __DIR__.'/../tmp/a.pdf';
        try {
            $worker = new Watermark($tmpName);
        } catch (Exception $e) {
            echo 'Выброшено исключение: ',  $e->getMessage(), "\n";
        }


        $format = self::getFormat($tmpName);
//        echo $format;
        $output = __DIR__.'/../tmp/output.'.$format;


        $firstMdlName = isset($certData['subject']['GN']) ? $certData['subject']['GN'] : '';
        $lastName = isset($certData['subject']['SN']) ? $certData['subject']['SN'] : '';

        if ($firstMdlName || $lastName) {
            $name = $lastName.' '.$firstMdlName;
        } else {
            $name = isset($certData['subject']['CN']) ? $certData['subject']['CN'] : 'Неизвестно';
        }

        $serial    = isset($certData['serial']) ? $certData['serial'] : '-';
        $validFrom = isset($certData['validFrom'])
            ? date('d.m.Y', $certData['validFrom'])
            : '-';
        $validTo   = isset($certData['validTo'])
            ? date('d.m.Y', $certData['validTo'])
            : '-';


        $sizeFont = 12;
        // Watermarking with Text
        if($format == 'jpg' || $format == 'jpeg'){
            $sizeImage = getimagesize($tmpName);
            if($sizeImage[0] > 2000)
                $sizeFont = 48;
        }

        $worker->setFont('Helvetica')
            ->setFontSize($sizeFont)
            ->setOpacity(0.7)
            ->setPosition(Watermark::POSITION_BOTTOM);

        $text = "_____________________________________________\n\n" .
            "ДОКУМЕНТ ПОДПИСАН ЭЛЕКТРОННОЙ ПОДПИСЬЮ\n" .
            "Сертификат $serial\n" .
            "Владелец $name\n" .

            "Действителен с $validFrom по $validTo\n" .
            "_____________________________________________";

          $worker->withText($text, $output);
          file_put_contents(Converter::TMP_DIR.'stamp.txt', "\n" . $text. "\n",FILE_APPEND);

//        $uuid = FileStorage::saveFile(file_get_contents($tmpName . '.new'), $fileName);

        @unlink($tmpName);
//        @unlink($tmpName . '.new');

        return $output;
    }

    public static function getFormat($filename){
        return end(explode(".", $filename));
    }
}
