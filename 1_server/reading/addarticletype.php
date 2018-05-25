<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$name = $_POST['name'];

$sql1 = "insert into article_type(name) values('{$name}')";
$r = mysql_query($sql1);
$arr = array(
    'result' => 0
);

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>
