<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link href="common.css" type="text/css" rel="stylesheet" />
	<title>添加根物种</title>
	<style type="text/css">
		.no_value_input {
			border-color: red;
		}
	</style>
	<script type="text/javascript" src="jquery-1.7.1.js"></script>
</head>
<body>
	<form id="add_node_form" action="node/add.action" method="post" enctype="multipart/form-data" >
		<table id="base_info">
			<tr><td>Name: </td><td><input name="name" type="text"/></td></tr>
			<tr><td>English Name: </td><td><input name="englishName" type="text"/></td></tr>
			<tr><td>Description: </td><td><input name="desc" type="text"/></td></tr>
			<tr><td>URI:</td><td><input name="uri" type="text"/></td></tr>
			<tr><td>URI Name: </td><td><input name="uriName" type="text"/></td></tr>
		</table>
		
		<a id="add_image" href="#">Add Image</a>
		<div id="add_image_location">
			<div id='image_value_x'>
				<input type='file' accept='image/jpeg' name='imageFiles'/>
				<input type='file' accept='image/jpeg' name='imageFiles'/>
				<input type='file' accept='image/jpeg' name='imageFiles'/>
			</div>
		</div>
		添加属性图片
		<input type='file' accept='image/jpeg' name='retrievalDataSource.features[0].imageFiles'/>
		<input type='file' accept='image/jpeg' name='retrievalDataSource.features[0].imageFiles'/>
		<div><input type="submit" value="SUBMIT" /></div>
	</form>
</body>
</html>