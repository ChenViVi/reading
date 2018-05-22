<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$did = 1;

$sql2 = "select * from discuss_comment where did = '$did' ";
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
    $uid = $row['uid'];
    $r1 = mysql_query("select * from user where id = '$uid'");
    $row1 = mysql_fetch_assoc($r1);
    $arrTemp = array(
        'id' => $row['id'],
        'did' => $row['did'],
        'user' => $row1['name'],
        'content' => $row['content'],
        'date' => $row['date']
    );
    $arrListInfoTemp[] = $arrTemp;
}
$arr['list'] =$arrListInfoTemp;

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>