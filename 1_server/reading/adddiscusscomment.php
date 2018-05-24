<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$uid = $_POST['userId'];
$discussId = $_POST['discussId'];
$content = $_POST['content'];

$sql1 = "insert into discuss_comment(uid,did,content) values('{$uid}','{$discussId}','{$content}')";
$r = mysql_query($sql1);
$arr = array(
		'result' => 0
);

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>
