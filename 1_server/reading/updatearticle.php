<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$articleId = $_POST['articleId'];
$type_id = $_POST['typeId'];
$title = $_POST['title'];
$info = $_POST['info'];
$content = $_POST['content'];

$r = mysql_query("update article set type_id = '{$type_id}',title = '{$title}',info = '{$info}',content = '{$content}' where id = '{$articleId}'");
$arr = array(
    'result' => 0
);

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>
