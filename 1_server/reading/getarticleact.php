<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$uid = $_POST['userId'];
$articleId = $_POST['articleId'];

$sql1 = "select * from action where uid = '$uid' and articleId = '$articleId'";
$r1 = mysql_query($sql1);
if ($r1 == true){
    if(mysql_num_rows($r1)){
        $row = mysql_fetch_assoc($r1);
        $result = 0;
        $id = $row['id'];
        $favorite = $row['favorite'];
        $collect = $row['collect'];
    }else{
        $result = 1;//0 OK ,1 NG
    }
}else{
    $result = 1;
}

$arr = array(
    'result' => $result,
    'id' => $id,
    'favorite' => $favorite,
    'collect' => $collect
);
$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>