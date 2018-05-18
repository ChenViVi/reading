<?php
header('content-type:text/html;charset=utf-8');

require_once("config.php");

$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$username = $_POST['phone'];
$password = $_POST['password'];
$password_md5 = md5($password);
$password_md5 = $password;

$sql1 = "select * from user where ((name = '$username' and password = '$password_md5') or (phone = '$username' and password = '$password_md5'))";
$r1 = mysql_query($sql1);


if (r1 == true){
    if(mysql_num_rows($r1)){
        $row = mysql_fetch_assoc($r1);
        $result = 0;
        $id = $row['id'];
        $name = $row['name'];
        $sex = $row['sex'];
        $imgUrl = $row['imgUrl'];
        $sign = $row['sign'];

    }else{
        $result = 2;//0 OK ,1 NG
    }
}else{
    $result = 1;
}


$arr = array(
    'result' => $result,
    'id' => $id,
    'name' => $name,
    'sex' => $sex,
    'imgUrl' => $imgUrl,
    'sign' => $sign,
);
$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>