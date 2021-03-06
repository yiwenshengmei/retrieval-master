<%@page import="com.zj.retrieval.master.DetailType"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"
	import="com.zj.retrieval.master.dao.NodeDao"
	import="com.zj.retrieval.master.Util"
	import="java.util.List"
	import="com.zj.retrieval.master.Node"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="common.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery-1.7.1.js"></script>
<script type="text/javascript">
	function delete_node(id) {
		if ( $('#post_user_name').val().length == 0 || 
			 $('#post_user_password').val().length == 0) {
			alert('请首先输入管理员用户名和密码。');
			return false;
		}
		$.ajax({
			type: 'post',
			url: 'node/delete',
			success: function (data, textStatus, jqXHR) {
				if (data.isError) {
					alert("没有删除成功: " + data.message);
				} else {
					alert("删除成功: " + data.message);
					$('#' + id).hide('slow', function() {
						$('#' + id).remove();
					});
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
				alert('ajax error: ' + textStatus);
			},
			data: { 
				'node_id': id, 
				'post_user_name': $('#post_user_name').val(), 
				'post_user_password': $('#post_user_password').val() },
			dataType: 'json'
		});
		return false;
	}
</script>
<title>查询物种</title>
</head>
<body>
<div>
	UserID: <input id='post_user_name' type="text" name='post_user_id'/>
	UserPassword: <input id='post_user_password' type='password' name='post_user_password'>
</div>
<table width="100%">
	<tr><td>编号</td><td>名称</td><td>父节点ID</td><td>信息是否完整</td><td><!-- DELETE --></td><td><!-- View Detail --></td><td><!-- EDIT --></td></tr>
<%
	NodeDao nodeDao = Util.getNodeDao();
	List<Node> nodes = nodeDao.getAllNodeAsBrief();
	for (Node nd : nodes) {
		out.print(String.format(
				"<tr id='%1$s'>" +
					"<td>%1$s</td>" +
					"<td>%2$s</td>" + 
					"<td>%3$s</td>" +
					"<td>%4$s</td>" +
					"<td><a href='#' onclick=\"delete_node('%1$s')\">DELETE</a></td>" +
					"<td><a target='_blank' href='view_node_detail.jsp?node_id=%1$s'>View Detail</a></td>" + 
					"<td><a target='_blank' href='%5$s?node_id=%1$s'>EDIT</a></td>" +
				"</tr>", 
			nd.getId(), nd.getName(), nd.getParentId(), 
			nd.getDetailType() == DetailType.FULL ? "是" : "否", 
			nd.getParentId().equals("virtual_node") ? "edit_root_node.jsp" : "edit_node.jsp"));
	}
%>
</table>
</body>
</html>