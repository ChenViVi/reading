<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$userId = $_POST['userId'];
$seal = $_POST['seal'];

$r = mysql_query("update user set seal = '{$seal}' where id = '{$userId}'");
$sql2 = "select * from user ORDER by id DESC";
$arr = array();
$arr['result'] = 0;

$result = mysql_query($sql2);
$arr['result'] = 1;// 0 OK,1 NG
$arrListInfo = array();
$arr['list'] =$arrListInfo;
$arrListInfoTemp = array();
$arr['result'] = 0;
while ($row =mysql_fetch_assoc($result)){

    $arrTemp = array(
        'id' => $row['id'],
        'seal' => $row['seal'],
        'name' => $row['name']
    );
    $arrListInfoTemp[] = $arrTemp;
}
$arr['list'] =$arrListInfoTemp;
$strr = json_encode($arr);
echo($strr);
mysql_close($link);
?>
