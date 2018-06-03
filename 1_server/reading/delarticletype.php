<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$typeId = $_POST['typeId'];

$result = mysql_query("select * from article where type_id = '$typeId'");
while ($row =mysql_fetch_assoc($result)){
    $articleId = $row['id'];
    mysql_query("delete from commnet where articleId = '$articleId'");
    mysql_query("delete from article where id = '$articleId'");
}
mysql_query("delete from article_type where id = '$typeId'");

$arr = array(
    'result' => 0
);

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>
