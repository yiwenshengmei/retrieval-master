<%@page import="com.zj.retrieval.master.Node"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.zj.retrieval.master.RetrievalResult"%>
<%@page import="com.zj.retrieval.master.NodeRetrieval"%>
<%@page import="com.zj.retrieval.master.Util"%>
<%@page import="com.zj.retrieval.master.dao.NodeDao"%>

<%@page language="java" 
		contentType="text/html; charset=utf-8"
    	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>检索</title>
<%
	String node_id = request.getParameter("node_id");
	String selected_state = request.getParameter("selected_state");
	if (selected_state == null) selected_state = "";
	List<String> result_node_ids = null;
	String attr_name = null;
	String attr_name_en = null;
	String attr_desc = null;
	List<String> attr_images = new ArrayList<String>();
	String display_style = "block";
	Map<String, String> attr_user_fields = null;
	NodeDao node_service = Util.getNodeDao();
	List<Node> root_childs = null;
	
	// 如果存在node_id，则进一步读取selected_state
	// 如果没有node_id，仅仅列出所有的根物种，然后配上RETRIEVAL FROM HERE按钮
	if (node_id != null && !node_id.equals("")) {
		NodeRetrieval node_retrieval = Util.getNodeRetrieval();
		
		RetrievalResult result = node_retrieval.retrieval(selected_state);
		
		if (result.hasResult()) {
	result_node_ids = result.getResult();
		} else {
	attr_name = result.getNext().getName();
	attr_name_en = result.getNext().getEnglishName();
	attr_desc = result.getNext().getDesc();
	attr_images.add(result.getNext().getImage());
	attr_user_fields = result.getNext().getUserFields();
		}
	} else {
		display_style = "none";
		Node root = node_service.queryNodeById(Node.VIRTUAL_NODE_NAME);
		root_childs = new ArrayList<Node>();
		for (String id : root.getRetrievalDataSource().getChildNodes()) {
	root_childs.add(node_service.queryNodeById(id));
		}
	}
%>
<script type="text/javascript" src='jquery-1.7.1.js'></script>
<script type="text/javascript">
	var selected_state = '<%=selected_state%>';

	function answer(answer) {
		$('#selected_state_post').val(selected_state + ' ' + answer);
		$('#retrieval_form').submit();
	}
	
	$(function() {
		
	});
</script>
</head>
<body>

	<form id='retrieval_form' style='display:<%=display_style%>;' action="retrieval.jsp" method="post">
		<input id='selected_state_post' type="hidden" name='selected_state'/>
		<input id='node_id' type='hidden' name='node_id' value='<%=node_id%>'/>
		<div style='display:<%=display_style%>;'><h1><%=attr_name%></h1><h3>( <%=attr_name_en %> )</h3></div>
		<table style='display:<%=display_style%>'>
			<tr><td>DESCRIPTION:&nbsp;</td><td><%=attr_desc%></td></tr>
		</table>
		
		<table style='display:<%=display_style%>'>
			<tr>
			<%
				for (String image_url : attr_images) { out.print(String.format("<td><img src='%1%s'/></td>", image_url)); } 
			%>
			</tr>
		</table>
		
		<div>
			<span onclick='answer(2);'>是</span>
			<span onclick='answer(1);'>否</span>
			<span onclick='answer(3)'>不知道</span>
		</div>
	</form>
	
	<% if ((node_id == null || node_id.equals("")) && root_childs != null) { %>
	<table>
		<tr><td>ID</td><td>中文名称</td><td>英文名称</td><td></td></tr>
		<% for (Node nd : root_childs) { %>
		<tr><td><%=nd.getId()%></td><td><%=nd.getName()%></td><td><%=nd.getEnglishName()%></td><a href='retrieval.jsp?node_id=<%=nd.getId()%>'>RETRIEVAL FROM HERE</a></tr>			
		<% } %>
	</table>	
	<% } %>
	

</body>
</html>