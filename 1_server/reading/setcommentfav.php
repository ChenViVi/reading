<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$id = $_POST['commentId'];

$result = 0;
$sql3 = "update comment set favoriteCt = favoriteCt+1 where id = '{$id}'";
$r=mysql_query($sql3);
  
$arr = array(
    'result' => $result
);
$strr = json_encode($arr);
mysql_close($link);
echo($strr);

?>
