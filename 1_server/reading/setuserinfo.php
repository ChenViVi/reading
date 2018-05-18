<?php
header('content-type:text/html;charset=utf-8');
mysql_set_charset('utf8');
require_once("config.php");

$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);

$uid = $_POST['userId'];
$name = $_POST['name'];
$sex = $_POST['sex'];
$sign = $_POST['sign'];

$result = 0;

$sql3 = "update user set name = '{$name}',sex = '{$sex}',sign = '{$sign}' where id = '{$uid}'";
$r=mysql_query($sql3);
  
$arr = array(
    'result' => $result
);
$strr = json_encode($arr);
mysql_close($link);
echo($strr);

?>
