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
	NodeDao nodeDao = Util.getNodeDao();
	Node root = nodeDao.queryNodeById(Node.VIRTUAL_NODE_NAME);
	List<Node> rootChilds = new ArrayList<Node>();
%>
<script type="text/javascript" src='jquery-1.7.1.js'></script>
</head>
<body>

	<table>
		<tr><td>ID</td><td>中文名称</td><td>英文名称</td><td></td></tr>
		<% for (Node nd : rootChilds) { %>
		<tr>
			<td><%=nd.getId()%></td>
			<td><%=nd.getName()%></td>
			<td><%=nd.getEnglishName()%></td>
			<td><a href='retrieval.jsp?node_id=<%=nd.getId()%>'>RETRIEVAL FROM HERE</a><td>
		</tr>			
		<% } %>
	</table>

</body>
</html>