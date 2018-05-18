<?php
    header('content-type:text/html;charset=utf-8');
	mysql_set_charset('utf8');
    require_once("config.php");
    
    $link = mysql_connect($localhost,$USERNAME,$DBPASS);
    mysql_query("set names 'utf8'",$link);
    mysql_select_db($DBNAME);
    
    $mobile = $_POST['phone']; //电话号码
    $password = $_POST['password'];//密码
    $password_md5 = md5($password);
    $password_md5 = $password;
    $result = 1; //0:成功 1:错误 2:用户名已存在
    
    $sql1 = "SELECT * FROM user WHERE phone = '{$mobile}'";   
    $r1 = mysql_query($sql1);//返回初次查询受影响的行数 验证机器是否已被注册过
    
    if ($r1 == true){
     if(mysql_num_rows($r1)){
            $result = 2;
        }else{
            $sql2 = "INSERT INTO user  (phone,password) VALUES ('{$mobile}','{$password_md5}')";
            $r = mysql_query($sql2);
            
            $id = mysql_insert_id();
            
            $sql1 = "select * from user where id = '$id'";
            $r1 = mysql_query($sql1);
    
            if (r1 == true){
                if(mysql_num_rows($r1)){
                    $row = mysql_fetch_assoc($r1);
                    $result = 0;
                    $name = $row['name'];
                    $sex = $row['sex'];
                    $imgUrl = $row['imgUrl'];
                    $sign = $row['sign'];
                
                }else{
                    $result = 1;//0 OK ,1 NG
                }
            }else{
                $result = 1;
            }
            
            if($r == true ){
                $result = 0;
            }else{
                $result = 1;//0 OK ,1 NG
            }
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