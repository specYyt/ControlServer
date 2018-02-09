<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">
	<title>节点列表</title>
</head>

<body>
	<div style="margin:100px;"> <a href="generateToken.jsp">添加节点</a></div>
	<div class="alert" style="margin:100px;">
		<table class="table table-hover table-condensed" border="1px">
			<tr align="center">
				<td>节点id</td>
				<td>token</td>
				<td>添加时间</td>
				<td>最后心跳时间</td>
				<td>节点状态</td>
			</tr>
			<s:if test="nodes.size()==0">
				<tr align="center">
					<td colspan="4">暂时没有节点。</td>
				</tr>
			</s:if>
			<s:else>
				<s:iterator value="nodes">
					<tr align="center">
						<td><s:property value="node_id" /></td>
						<td><s:property value="token" /></td>
						<td><s:property value="join_time" /></td>
						<td><s:property value="last_active_time" /></td>
						<td>
							<s:if test="status == 1">
								正常
							</s:if>
							<s:else>
								异常
							</s:else>
						</td>
					</tr>
				</s:iterator>
			</s:else>
		</table>
	</div>
</body>
</html>
