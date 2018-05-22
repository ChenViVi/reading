<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$sql2 = "select * from discuss_type";
$arr2 = array();
$result = mysql_query($sql2);
while ($row =mysql_fetch_assoc($result)){
    array_push($arr2, array(
        'id' => $row['id'],
        'name' => $row['name']
    ));
}
$arr = array(
    'result' => 0,
    'list' => $arr2
);
$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>