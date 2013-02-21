var nodeAttributeIndex = 0;
var nodeImageIndex = 0;

// 删除一条NodeAttribute
function deleteNodeAttribute(index) {
	$('#node_attribute_' + index).remove();
}

// 删除一张NodeImage
function deleteNodeImage(index) {
	$('#image_field_' + index).remove();
}

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

$(function() {
	$("#add_node_attribute").click(addNodeAttributeHandler);
	$("#add_image").click(addNodeImageHandler);
	$("#submit_form").click(submitHandler);
});