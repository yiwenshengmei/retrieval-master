<%@page import="org.apache.http.impl.client.BasicResponseHandler"%>
<%@page import="org.apache.http.client.ResponseHandler"%>
<%@page import="org.apache.http.HttpEntity"%>
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="org.apache.http.client.entity.UrlEncodedFormEntity"%>
<%@page import="org.apache.http.protocol.HTTP"%>
<%@page import="org.apache.http.message.BasicNameValuePair"%>
<%@page import="org.apache.http.NameValuePair"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="org.apache.http.client.HttpClient"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.zj.retrieval.master.Attribute"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String nodeId = request.getParameter("node_id");
	String url = request.getParameter("url");
	String userName = request.getParameter("user_name");
	String userPwd = request.getParameter("user_pwd");
	
	DefaultHttpClient httpclient = new DefaultHttpClient();
	
	HttpPost httpost = new HttpPost(url);
    List <NameValuePair> nvps = new ArrayList <NameValuePair>();
    nvps.add(new BasicNameValuePair("user_name", userName));
    nvps.add(new BasicNameValuePair("user_pwd", userPwd));
    httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    ResponseHandler<String> responseHandler = new BasicResponseHandler();
    String responseBody = httpclient.execute(httpost, responseHandler);
	
	
	//打印返回的信息
	System.out.println(responseBody);

	JSONObject j = new JSONObject(responseBody);
	String author = j.getString("author");
	String name = j.getString("name");
	String enName = j.getString("name_en");
	String desc = j.getString("desc");
	String uri = j.getString("uri");
	String uriName = j.getString("uri_name");
	String owl = j.getString("owl");
	List<String> images = new ArrayList<String>();
	JSONArray jImages = j.getJSONArray("images");
	for (int i = 0; i < jImages.length(); i++) {
		images.add(jImages.getString(i));
	}
	JSONArray jUserFields = j.getJSONArray("user_field");
	Map<String, String> userField = new HashMap<String, String>();
	for (int i = 0; i < jUserFields.length(); i++) {
		JSONObject jField = jUserFields.getJSONObject(i);
		userField.put(jField.getString("key"), jField.getString("value"));
	}
// 	JSONArray jAttr = j.getJSONArray("attr");
// 	List<Attribute> attrs = new ArrayList<Attribute>();
// 	for (int i = 0; i < jAttr.length(); i++) {
// 		JSONObject ja = jAttr.getJSONObject(i);
// 		Attribute attr = new Attribute();
// 		attr.setName(ja.getString("name"));
// 		attr.setEnName(ja.getString("name_en"));
// 		attr.setImage(ja.getString("image"));
// 		attr.setDesc(ja.getString("desc"));
// 		Map<String, String> attrUserFields = new HashMap<String, String>();
// 		JSONArray jAttrUserFields = ja.getJSONArray("user_filed");
// 		for (int k = 0; k < jAttrUserFields.length(); k++) {
// 			attrUserFields.put(
// 					jAttrUserFields.getJSONObject(k).getString("key"), 
// 					jAttrUserFields.getJSONObject(k).getString("value"));
// 		}
// 		attr.setUserFields(attrUserFields);
// 		attrs.add(attr);
// 	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>ID: <%=nodeId %> FROM <%=url %></title>
</head>
<body>
<table>
	<tr><td>ID</td><td><%=nodeId %></td></tr>
	<tr><td>NAME: </td><td><%=name %></td></tr>
	<tr><td>NAME_EN: </td><td><%=enName %></td></tr>
	<tr><td>AUTHOR: </td><td><%=author %></td></tr>
	<tr><td>DESC: </td><td><%=desc %></td></tr>
	<tr><td>URI: </td><td><%=uri %></td></tr>
	<tr><td>URI_NAME: </td><td><%=uriName %></td></tr>
	<tr><td>OWL: </td><td><%=owl %></td></tr>
	<tr><td colspan='2'>====== User Field ======</td></tr>
	<% for (Entry<String, String> entry : userField.entrySet()) { %>
			<tr><td>KEY: <%=entry.getKey() %></td><td>VALUE: <%=entry.getValue() %></td></tr>
	<% } %>
	<tr><td colspan='2'>====== Images ======</td></tr>
	<% for (String imageUrl : images) { %>
			<tr><td colspan='2'><img src='<%=imageUrl %>'/></td></tr>
	<% } %>
<%-- 	<% for (Attribute attrItem : attrs) { %> --%>
<%-- 			<tr><td colspan='2'>====== Attribute: <%=attrItem.getName() %> ======</td></tr> --%>
<!-- 			<tr><td>NAME: </td><td></td></tr> -->
<!-- 			<tr><td>NAME_EN: </td><td></td></tr> -->
<!-- 			<tr><td>DESC: </td><td></td></tr> -->
<%-- 			<% for (Entry<String, String> attrUserFiledEntry : attrItem.getUserFields().entrySet()) { %> --%>
<%-- 				<tr><td>KEY: <%=attrUserFiledEntry.getKey() %></td><td>VALUE: <%=attrUserFiledEntry.getValue() %></td></tr> --%>
<%-- 			<% } %> --%>
<%-- 			<tr><td colspan='2'><img src='<%=attrItem.getImage() %>'/></td></tr> --%>
<%-- 	<% } %> --%>
	<tr><td></td><td></td></tr>
	<tr><td></td><td></td></tr>
	<tr><td></td><td></td></tr>
	<tr><td></td><td></td></tr>
	<tr><td></td><td></td></tr>
	<tr><td></td><td></td></tr>
	<tr><td></td><td></td></tr>
	<tr><td></td><td></td></tr>
	<tr><td></td><td></td></tr>
</table>
</body>
</html>