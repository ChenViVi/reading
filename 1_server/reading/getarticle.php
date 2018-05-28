<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$sql2 = "select * from article ORDER by id DESC";
$arr = array();
$arr['result'] = 0;

$result = mysql_query($sql2);
$arr['result'] = 1;// 0 OK,1 NG
$arrListInfo = array();
$arr['list'] =$arrListInfo;
$arrListInfoTemp = array();
$arr['result'] = 0;
while ($row =mysql_fetch_assoc($result)){
    $type_id = $row['type_id'];
    $sql1 = "select * from article_type where id = '$type_id'";
    $r1 = mysql_query($sql1);
    $row2 = mysql_fetch_assoc($r1);
    $arrTemp = array(
        'id' => $row['id'],
        'typeId' => $row['type_id'],
        'title' => $row['title'],
        'type' => $row2['name'],
        'info' => $row['info'],
        'date' => $row['date'],
        'content' => $row['content']
    );
    $arrListInfoTemp[] = $arrTemp;
}
$arr['list'] =$arrListInfoTemp;

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>