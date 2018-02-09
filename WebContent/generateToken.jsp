<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>生成Token</title>
    <script src="js/jquery-1.11.1.js"></script>
	<script src="js/task.js"></script>
  </head>
  
  <body>
  	 
  	  <div style="margin:100px;"> <a href="listNodes.action">节点列表</a></div>
	  <div style="margin:100px;">
		  <form>
		    Token:<input type="text" name="token" id="token" style="width:300px;">
		    <button type="button" name="submit" onclick="send()">生成</button>
		  </form>
	 </div>
  </body>
</html>
