<?php
    header('content-type:text/html;charset=utf-8');
    mysql_set_charset('utf8');
    require_once("config.php");

    $link = mysql_connect($localhost,$USERNAME,$DBPASS);
    mysql_query("set names 'utf8'",$link);
    mysql_select_db($DBNAME);

    $uid = $_POST['userId'];

    if(empty($articleId)){
        $sql1 = "select count(*) from article,action_1 where article.id=action_1.articleId  and action_1.uid='$uid' and action_1.collect =1 order by article.id ";
        $sql2 = "select article.* from article,action_1 where article.id=action_1.articleId  and action_1.uid='$uid' and action_1.collect =1 order by article.id ";
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
            $arrListInfoTemp[] = $arrTemp;
        
        }
        $arr['list'] =$arrListInfoTemp;
    }
    $strr = json_encode($arr);
    mysql_close($link);
    echo($strr);
?>