<?php
header('content-type:text/html;charset=utf-8');
mysql_set_charset('utf8');
require_once("config.php");

$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);

$uid = $_POST['userId'];
$articleId = $_POST['articleId'];
$favorite = $_POST['favorite'];



    $result = 0;
    
    $sql ="select * from action_1 where uid = '{$uid}' and articleId='{$articleId}'";
    $r = mysql_query($sql);
    if($r && mysql_num_rows($r)){
  
        $sql3 = "update action_1 set favorite = $favorite where  uid = '{$uid}' and articleId='{$articleId}'";
        $r=mysql_query($sql3);
    }else{
        $sql1 = "insert into action_1(uid,articleId,favorite) values('{$uid}','{$articleId}','{$favorite}')";
        $r = mysql_query($sql1);
    }
    

$arr = array(
		'result' => $result
);
$strr = json_encode($arr);
mysql_close($link);
echo($strr);

?>
