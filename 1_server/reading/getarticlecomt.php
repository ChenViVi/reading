<?php
header('content-type:text/html;charset=utf-8');
mysql_set_charset('utf8');
require_once 'config.php';

$articleId = $_POST['articleId'];
$type = $_POST['type'];

$link = mysql_connect($localhost,$USERNAME,$DBPASS);
mysql_query("set names 'utf8'",$link);
mysql_select_db($DBNAME);

if($type == 0){
    $sql1 ="select count(*) from comment_1 where articleId = $articleId order by id desc";
    $sql2 ="select * from comment_1 where articleId = $articleId order by id desc";
}else{
    $sql1 ="select count(*) from comment_1 where articleId = $articleId order by favoriteCt desc";
    $sql2 ="select * from comment_1 where articleId = $articleId order by favoriteCt desc";
}

$result = mysql_query($sql1);

$total_num = 0;
$arr = array();
$total_num=mysql_result($result,0);
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

        $uid = $row['uid'];
        $sql1 = "select * from user where id = '$uid'";
        $r1 = mysql_query($sql1);
        $row2 = mysql_fetch_assoc($r1);

        $arrTemp = array(
            'id' => $row['id'],
            'uid' => $row['uid'],
            'name' => $row2['name'],
            'date' => $row['date'],
            'content' => $row['content'],
            'favoriteCt' => $row['favoriteCt']
        );
        $arrListInfoTemp[] = $arrTemp;

    }
    $arr['list'] =$arrListInfoTemp;
}


	$strr = json_encode($arr);
    mysql_close($link);
	echo($strr);
?>
