// 节点属性计数器
var nodeAttributeIndex = 0;
// 节点图片计数器
var nodeImageIndex = 0;
// 特征计数器
var nodeFeatureIndex = 0;

// 删除一条NodeAttribute
function deleteNodeAttribute(index) {
	$('#node_attribute_' + index).remove();
	return false;
}

// 删除一张NodeImage
function deleteNodeImage(index) {
	$('#image_field_' + index).remove();
	return false;
}

// 删除指定id的DOM元素
function deleteElement(id) {
	$('#' + id).remove();
	return false;
}

// 点击添加属性
function addNodeAttributeHandler() {
	var location = $("#add_node_attribute_location");
	var newNodeAttributeDivHTML = $(
		"<div id='node_attribute_" + nodeAttributeIndex + "'>" +
			"<span>" +
				"属性名称: <input id='node_attribute_key_" + nodeAttributeIndex + "' type='text' name='attributes[" + nodeAttributeIndex + "].key'/>" +
			"</span>" + 
			"<span>" +
				"属性内容: <input id='node_attribute_value_" + nodeAttributeIndex + "' type='text' name='attributes[" + nodeAttributeIndex + "].value'>" +
			"</span>" +
			"<span>" +
				"<a class='node_attribute_delete' href='#' onclick='deleteNodeAttribute(" + nodeAttributeIndex + ");'>删除</a>" +
			"</span>" +
		"</div>"
	);
	location.append(newNodeAttributeDivHTML);
	nodeAttributeIndex++;
}

// 点击添加图片
function addNodeImageHandler() {
	var location = $('#add_image_location');
	var newImageDivHTML = $(
		"<div id='image_field_" + nodeImageIndex + "'>" +
			"<input type='file' name='imageFiles' accept='image/jpeg'/>" +
			"<span>" +
				"<a href='#' onclick='deleteNodeImage(" + nodeImageIndex + ");'>删除</a>" +
			"</span>" +
		"</div>"
	);
	location.append(newImageDivHTML);
	nodeImageIndex++;
}

// 点击保存按钮
function submitHandler() {
	// 检验表单
	if ($('input[name="name"]').val() == "") {
		alert("物种名称必须填写");
		$('input[name="name"]').addClass("noValueInputStyle");
		return false;
	}
	
	$('#add_node_form').submit();
	return false;
}

var featureImageIndex = [];

// 在新增的特征中点击添加图片按钮
function addFeatureImageHandler(featureIndex) {
	var location = $('#add_feature_image_location_' + featureIndex);
	featureImageIndex[featureIndex] = (featureImageIndex[featureIndex] == undefined ? 0 : featureImageIndex[featureIndex]); 
	var divId = "feature_" + featureIndex + "_image_" + featureImageIndex[featureIndex];
	var inputName = "newFeatures[" + featureIndex + "].imageFiles";
	var featureImageDivHTML = $(
		"<div id='" + divId + "'>" +
			"<input type='file' name='" + inputName + "' accept='image/jpeg'/>" +
			"<span>" +
				"<a href='#' onclick='deleteElement(\"" + divId + "\");'>删除</a>" +
			"</span>" +
		"</div>"
	);
	location.append(featureImageDivHTML);
	featureImageIndex[featureIndex]++;
}

// 点击添加特征按钮
function addNodeFeatureHandler() {
	var featureTableHTML = $("<table id='node_feature_" + nodeFeatureIndex + "'></table>");
	var featureNameTrHTML = $("<tr><td>特征中文名称: </td><td><input id='node_feature_name_" + nodeFeatureIndex +"' type='text' name='newFeatures[" + nodeFeatureIndex + "].name'/></td></tr>");
	var featureEnglishNameTrHTML = $("<tr><td>特征英文名称: </td><td><input id='node_feature_english_name_" + nodeFeatureIndex +"' type='text' name='newFeatures[" + nodeFeatureIndex + "].englishName'/></td></tr>");
	var featureDescTrHTML = $("<tr><td>特征描述: </td><td><textarea id='node_feature_desc_" + nodeFeatureIndex +"' wrap='virtual' name='newFeatures[" + nodeFeatureIndex + "].desc'/></td></tr>");
	var addFeatureImageAnchorHTML = $("<tr><td colspan='2'><a href='#' onClick='addFeatureImageHandler(" + nodeFeatureIndex + ");'>添加图片</a></td></tr>");
	var featureImageLocationTrHTML = $("<tr><td colspan='2'><div id='add_feature_image_location_" + nodeFeatureIndex + "'></div></td></tr>");
	var deleteCode = 'deleteElement("node_feature_' + nodeFeatureIndex + '")';
	var featureDeleteTrHTML = $("<tr><td colspan='2'><a href='#' onclick='" + deleteCode + "'>删除特征</a></td></tr>");
	featureTableHTML.append(featureNameTrHTML)
					.append(featureEnglishNameTrHTML)
		            .append(featureDescTrHTML)
		            .append(addFeatureImageAnchorHTML)
		            .append(featureImageLocationTrHTML)
		            .append(featureDeleteTrHTML);
	$('#add_node_feature_location').append(featureTableHTML);
	nodeFeatureIndex++;
	return false;
}

// 注册事件
$(function() {
	$("#add_node_attribute").click(addNodeAttributeHandler);
	$("#add_image").click(addNodeImageHandler);
	$("#submit_form").click(submitHandler);
	$('#add_node_feature').click(addNodeFeatureHandler);
});