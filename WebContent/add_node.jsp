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
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  	<script src="http://code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
	<script type="text/javascript" src="add_node.js"></script>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
</head>
<body>
	<form style="width:700px;margin:0 auto;" id="add_node_form" action="node/addNode.action" method="post" enctype="multipart/form-data" >
		<table id="base_info">
			<tr><td>中文名称: </td><td><input name="name" type="text"/></td></tr>
			<tr><td>英文名称: </td><td><input name="englishName" type="text"/></td></tr>
			<tr><td>父节点: </td><td><input name="parentNode.id" type="text"/><a href="#" onclick='selectParentNodeHandler();' >选择</a></td></tr>
			<tr><td>描述: </td><td><input name="desc" type="text"/></td></tr>
			<tr><td>URI:</td><td><input name="uri" type="text"/></td></tr>
			<tr><td>URI名称: </td><td><input name="uriName" type="text"/></td></tr>
		</table>
		<input type="hidden" name="parentNode.id" />
		
		<a id="add_node_image" href="#">添加物种图片</a>
		<div id="add_node_image_location"></div>
		
		<div style='height: 10px;'></div>
		<a id='add_node_attribute' href='#'>添加属性</a>
		<div id='add_node_attribute_location'></div>
		
		<div style='height: 10px;'></div>
		<a href='#' onclick='selectParentFeatureHandler();'>选择父节点特征</a>
		<div id='parent_feature_location'></div>
		
		<div style='height: 10px;'></div>
		<a id="add_node_feature" href="#">添加特征</a>
		<div id="add_node_feature_location"></div>
		
		<div style='height: 10px;'></div>
		<div><a id="submit_form" href="#">保存节点</a></div>
	</form>
	
	<div id="findParentNodeDialog" style="display:none;" title="Basic dialog">
		<table id="selectParentNodeTable">
		</table>
	</div>
	
	<div id='select_parent_feature_dialog' style='display:none;'>
		<table id="select_parent_feature_location">
		</table>
	</div>
</body>
</html>