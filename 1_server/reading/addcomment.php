<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$uid = $_POST['userId'];
$articleId = $_POST['articleId'];
$content = $_POST['content'];

$sql1 = "insert into comment(uid,articleId,content) values('{$uid}','{$articleId}','{$content}')";
$r = mysql_query($sql1);
$arr = array(
		'result' => 0
);

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>
