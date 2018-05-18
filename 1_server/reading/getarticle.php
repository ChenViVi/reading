<?php
    header('content-type:text/html;charset=utf-8');
    mysql_set_charset('utf8');
    require_once("config.php");

    $link = mysql_connect($localhost,$USERNAME,$DBPASS);
    mysql_query("set names 'utf8'",$link);
    mysql_select_db($DBNAME);

    $uid = $_POST['userId'];
    $articleId = $_POST['articleId'];

    if(empty($articleId)){
        $sql1 = "select count(*) from article order by id desc LIMIT 0 , 1";
        $sql2 = "select * from article order by id desc LIMIT 0 , 1";
    }else{
        $sql2 = "select * from article where id <= '$articleId' order by id desc LIMIT 0 , 1";
        $sql1 = "select count(*) from article where id <= '$articleId' order by id desc LIMIT 0 , 1";
    }
    $r1 = mysql_query($sql1);

    $total_num = 0;
    $arr2 = array();
    $total_num=mysql_result($r1,0);
    
    if($total_num != 0 ){
        $result = mysql_query($sql2);
        if($result && mysql_num_rows($result)){
            $row = mysql_fetch_assoc($result);
            $arrTemp = array(
                'result' => 0,
                'id' => $row['id'],
                'title' => $row['title'],
                'info1' => $row['info1'],
                'info2' => $row['info2'],
                'author' => $row['author'],
                'date' => $row['date'],
                'imgUrl' => $row['imgUrl'],
                'content' => $row['content']
            );
        }
    }else{
        $arrTemp = array(
            'result' => 1
        );
    }
    
    $arr = $arrTemp;
    $strr = json_encode($arr);
    mysql_close($link);
    echo($strr);
?>