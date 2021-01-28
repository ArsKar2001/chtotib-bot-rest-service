<?php
namespace helpers;
use setasign\Fpdi\Fpdi;
class WaterMark
{
    public $pdf, $file, $newFile,
        $wmText = "STACKOVERFLOW";

    /** $file and $newFile have to include the full path. */
    public function __construct($file, $newFile,$text)
    {
        $this->pdf =& new Fpdi();
        $this->file = $file;
        $this->newFile = $newFile;
        $this->wmText  = $text;
    }

    /** $file and $newFile have to include the full path. */
    public static function applyAndSpit($file, $newFile,$text)
    {
        $wm = new WaterMark($file, $newFile,$text);

        if($wm->isWaterMarked())
            return false;
        else{
            $wm->doWaterMark();
        }
    }

    /** @todo Make the text nicer and add to all pages */
    public function doWaterMark()
    {
        $currentFile = $this->file;
        $newFile = $this->newFile;
        $text = $this->wmText;
        $pagecount = $this->pdf->setSourceFile($currentFile);

        for($i = 1; $i <= $pagecount; $i++){
            $this->pdf->addPage();
            $tplidx = $this->pdf->importPage($i);
            $this->pdf->useTemplate($tplidx, 10, 10, 100);
            // now write some text above the imported page
            $this->pdf->SetFont('Arial', 'I', 10);
            $this->pdf->SetTextColor(255,0,0);
            $this->pdf->SetXY(10, 10);
            $this->pdf->Multicell(0,5,$text);
        }

        $this->pdf->Output($newFile, 'F');
    }

    public function isWaterMarked()
    {
        return (file_exists($this->newFile));
    }

    public function spitWaterMarked()
    {
        return readfile($this->newFile);
    }



}
