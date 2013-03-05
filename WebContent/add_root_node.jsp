<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>添加根物种</title>
	<style type="text/css">
		.noValueInputStyle {
			border-color: red;
		}
	</style>
	<script type="text/javascript" src="jquery-1.7.1.js"></script>
	<script type="text/javascript" src="add_root_node.js"></script>
</head>
<body>
	<form id="add_node_form" action="node/addRootNode.action" method="post" enctype="multipart/form-data" >
		<table id="base_info">
			<tr><td>物种中文名称: </td><td><input name="name" type="text"/></td></tr>
			<tr><td>物种英文名称: </td><td><input name="englishName" type="text"/></td></tr>
			<tr><td>物种描述: </td><td><textarea name="desc" wrap="virtual"></textarea></td></tr>
			<tr><td>物种URI:</td><td><input name="uri" type="text"/></td></tr>
			<tr><td>物种URI名称: </td><td><input name="uriName" type="text"/></td></tr>
		</table>
		
		<a id="add_image" href="#">添加物种图片</a>
		<div id="add_image_location"></div>
		
		<a id="add_node_attribute" href="#">添加自定义属性</a>
		<div id="add_node_attribute_location"></div>
		
		<a id="add_node_feature" href="#">添加特征</a>
		<div id="add_node_feature_location"></div>
		
		<div><a id="submit_form" href="#">保存物种</a></div>
	</form>
</body>
</html>