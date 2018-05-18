<?php
header('content-type:text/html;charset=utf-8');
require_once("config.php");
$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);
mysql_set_charset('utf8');

$uid = $_POST['userId'];

if(empty($articleId)){
    $sql1 = "select count(*) from article,action where article.id=action.articleId  and action.uid='$uid' and action.collect =1 order by article.id ";
    $sql2 = "select article.* from article,action where article.id=action.articleId  and action.uid='$uid' and action.collect =1 order by article.id ";
}
$r1 = mysql_query($sql1);
$total_num = 0;
$total_num=mysql_result($r1,0);
$arr = array();
$arr['total_num'] = $total_num;
$arr['result'] = 0;
if($total_num != 0 ){
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
            'result' => 0,
            'id' => $row['id'],
            'title' => $row['title'],
            'type' => $row2['name'],
            'info' => $row['info'],
            'date' => $row['date'],
            'content' => $row['content']
        );
        $arrListInfoTemp[] = $arrTemp;
    }
    $arr['list'] =$arrListInfoTemp;
}

$strr = json_encode($arr);
mysql_close($link);
echo($strr);
?>