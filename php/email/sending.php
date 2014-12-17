<?php

ini_set('display_errors', 'On');
error_reporting(E_ALL | E_STRICT);


header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept');
header('Content-Type: Application/JSON; Charset=UTF-8;');



$to = $_GET['email'];
$body = $_GET['body'];


try{  
	echo $body;
  
          $subject = "email subject";
          $headers = "From: man-r@man-r.me\n";
  
  
  if (mail($to, $subject, $body, $headers)) {
    echo "mail send ... OK";
    }
  
  else {
    echo "mail send ... ERROR";
  }
  
}
catch(Exception $e) {
	echo $body;
        echo $e->getMessage();
    }

?>
