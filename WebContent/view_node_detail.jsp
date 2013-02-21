<%@page import="com.zj.retrieval.master.actions.XMLUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.zj.retrieval.master.FeatureImage"%>
<%@page import="com.zj.retrieval.master.NodeFeature"%>
<%@page import="com.zj.retrieval.master.NodeImage"%>
<%@page import="com.zj.retrieval.master.NodeAttribute"%>
<%@page import="com.zj.retrieval.master.BizNode"%>
<%@page import="com.zj.retrieval.master.Node"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="common.css" type="text/css" rel="stylesheet" />
<title>查看物种详细信息</title>
</head>
<body>
<table border="1" width="95%" style="margin: 0 auto;">
<%
	String nodeId = request.getParameter("node_id");
	Node node = BizNode.getNode(nodeId);
	BizNode.changePath2Url(node);
%>
<tr><td>ID: </td><td><%=node.getId()%></td></tr>
<tr><td>中文名称: </td><td><%=node.getName()%></td></tr>
<tr><td>英文名称: </td><td><%=node.getEnglishName()%></td></tr>
<tr><td>描述:</td><td><%=node.getDesc()%></td></tr>
<tr><td>OWL: </td><td><textarea rows="25" cols="120"><%=XMLUtils.format(node.getOwl())%></textarea></td></tr>
<tr><td>URI: </td><td><%=node.getUri()%></td></tr>
<tr><td>URI名称: </td><td><%=node.getUriName()%></td></tr>
<tr><td>父节点名称: </td><td><%=node.getParentNode() == null ? StringUtils.EMPTY : node.getParentNode().getName()%></td></tr>
<tr><td>标签: </td><td><%=node.getLabel()%></td></tr>
<tr><td>作者联系方式: </td><td><%=node.getAuthorContact()%></td></tr>
<tr><td colspan='2'>====== 属性 ======</td></tr>
<%
	for (NodeAttribute attr : node.getAttributes()) {
%>
<tr><td>属性名称: <%=attr.getKey()%></td><td>属性内容: <%=attr.getValue()%></td></tr>
<%
	}
%>
<tr><td colspan='2'>====== 图片 ======</td></tr>
<%
	for (NodeImage nodeImg : node.getImages()) {
%>
<tr><td colspan='2'><img src='<%=nodeImg.getUrl()%>'/></td></tr>
<%
	}
%>
<tr><td colspan='2'>====== 特征 ======</td></tr>
<%
	for (NodeFeature feature : node.getRetrievalDataSource().getFeatures()) {
%>
<tr><td>特征中文名称：</td><td><%=feature.getName() %></td></tr>
<tr><td>特征英文名称：</td><td><%=feature.getEnglishName() %></td></tr>
<tr><td>特征描述：</td><td><%=feature.getDesc() %></td></tr>
<%
		for (FeatureImage featureImg : feature.getImages()) {
%>
<tr><td>特征图片：</td><td><img src='<%="images/" + featureImg.getUrl() %>'/></td></tr>
<%
		}
	}
%>
</table>
</body>
</html>