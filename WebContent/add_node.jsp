<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link href="common.css" type="text/css" rel="stylesheet" />
	<title>添加物种</title>
	<style type="text/css">
		.no_value_input {
			border-color: red;
		}
	</style>
	<script type="text/javascript" src="jquery-1.7.1.js"></script>
	<script type="text/javascript" src="add_node.js"></script>
</head>
<body>
	<form style="width:700px;margin:0 auto;" id="add_node_form" action="node/addNode.action" method="post" enctype="multipart/form-data" >
		<table id="base_info">
			<tr><td>中文名称: </td><td><input name="name" type="text"/></td></tr>
			<tr><td>英文名称: </td><td><input name="englishName" type="text"/></td></tr>
			<tr><td>父节点ID: </td><td><input name="parentNode.id" type="text"/></td></tr>
			<tr><td>描述: </td><td><input name="desc" type="text"/></td></tr>
			<tr><td>URI:</td><td><input name="uri" type="text"/></td></tr>
			<tr><td>URI名称: </td><td><input name="uriName" type="text"/></td></tr>
			<tr><td>父节点特征ID: </td><td><input name='featuresOfParent[0].id' type='text'/></td></tr>
		</table>
		
		<a id="add_node_image" href="#">添加物种图片</a>
		<div id="add_node_image_location"></div>
		
		<div style='height: 10px;'></div>
		<a id='add_node_attribute' href='#'>添加属性</a>
		<div id='add_node_attribute_location'></div>
		
		<div style='height: 10px;'></div>
		<a id="add_node_feature" href="#">添加特征</a>
		<div id="add_node_feature_location"></div>
		
		<div style='height: 10px;'></div>
		<div><a id="submit_form" href="#">保存节点</a></div>
	</form>
</body>
</html>