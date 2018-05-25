<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$type_id = $_POST['typeId'];
$title = $_POST['title'];
$info = $_POST['info'];
$author = $_POST['author'];
$date = $_POST['date'];
$content = $_POST['content'];

$sql1 = "insert into article(type_id,title,info,author,date,content) values('{$type_id}','{$title}','{$info}','{$author}','{$date}','{$content}')";
$r = mysql_query($sql1);
$arr = array(
    'result' => 0
);

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>
