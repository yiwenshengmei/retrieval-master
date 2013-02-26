function deleteNodeAttribute(index) {
	$('#node_attribute_' + index).remove();
}

function deleteNodeFeature(index) {
	$('#node_feature_' + index).remove();
}

var findParentNodeDialog;
/**
 * 
 */
function findParentNode() {
	findParentNodeDialog.dialog("open");
}

function loadNodes() {
	$.ajax({
		url: "node/getNodesAjax.action",
		type: "POST",
		dataType: "json"
	}).done(function (data) {
		alert(data);
	});
}

// 判断是否存在没有填写的input域，如果有，则高亮未填写的input域并弹出提示
function validate_input() {
	var has_no_value_input = false;
	$('input:text').each(function() {
		var input = $(this);
		if (input.val() == '') {
			if (input.attr('class') != undefined && input.attr('class').indexOf('allow_empty') != -1) {
				input.removeClass('no_value_input');
			} else {
				has_no_value_input = true;
				input.addClass('no_value_input');
			}
		} else {
			input.removeClass('no_value_input');
		}
	});
	if (has_no_value_input) {
		alert('数据未填写完整。');
		return false;
	} else {
		return true;
	}
}

function delete_self(self) {
	self.remove();
	return false;
}

var nodeAttributeIndex = 0;
function addNodeAttributeHandler() {
	var location = $("#add_node_attribute_location");
	var nodeAttributeDivHTML = $(
		"<div style='margin-top: 10px;' id='node_attribute_" + nodeAttributeIndex + "'>" +
			"<span>" +
				"属性名称: <input id='node_attribute_key_" + nodeAttributeIndex + "' type='text' name='attributes[" + nodeAttributeIndex + "].key'/>" +
			"</span>" + 
			"<span>" +
				"属性内容: <input id='node_attribute_value_" + nodeAttributeIndex + "' type='text' name='attributes[" + nodeAttributeIndex + "].value'>" +
			"</span>" +
			"<span>" +
				"<a class='button' href='#' onclick='deleteNodeAttribute(" + nodeAttributeIndex + ");'>删除</a>" +
			"</span>" +
		"</div>"
	);
	location.append(nodeAttributeDivHTML);
	nodeAttributeIndex++;
	return false;
}

function createImageDivHTML(divId, onClickCode, inputName) {
	var imageDivHTML = "<div id='" + divId + "'>" +
							"<input type='file' name='" + inputName + "' accept='image/jpeg'/>" +
							"<span>" +
								"<a href='#' onclick='" + onClickCode + "'>删除</a>" +
							"</span>" +
						"</div>";
	return imageDivHTML;
}

function deleteElement(id) {
	$('#' + id).remove();
	return false;
}

var nodeImageIndex = 0;
function addNodeImageHandler() {
	var location = $('#add_node_image_location');
	var newImageDivHTML = $(createImageDivHTML("node_image_" + nodeImageIndex, 'deleteElement("node_image_' + nodeImageIndex + '")', "imageFiles"));
	location.append(newImageDivHTML);
	nodeImageIndex++;
	return false;
}

var featureImageIndex = 0;
function addFeatureImageHandler(currentIndex) {
	var location = $("#add_feature_image_location_" + currentIndex);
	var featureImageDivHTML = $(createImageDivHTML("feature_image_" + featureImageIndex, 'deleteElement("feature_image_' + featureImageIndex + '")', "retrievalDataSource.features[" + currentIndex + "].imageFiles"));
	location.append(featureImageDivHTML);
	featureImageIndex++;
	return false;
}

var nodeFeatureIndex = 0;
function addNodeFeatureHandler() {
	var featureTableHTML = $("<table id='node_feature_" + nodeFeatureIndex + "'></table>");
	var featureNameTrHTML = $("<tr><td>特征中文名称: </td><td><input id='node_feature_name_" + nodeFeatureIndex +"' type='text' name='retrievalDataSource.features[" + nodeFeatureIndex + "].name'/></td></tr>");
	var featureEnglishNameTrHTML = $("<tr><td>特征英文名称: </td><td><input id='node_feature_english_name_" + nodeFeatureIndex +"' type='text' name='retrievalDataSource.features[" + nodeFeatureIndex + "].englishName'/></td></tr>");
	var featureDescTrHTML = $("<tr><td>特征描述: </td><td><textarea id='node_feature_desc_" + nodeFeatureIndex +"' wrap='virtual' name='retrievalDataSource.features[" + nodeFeatureIndex + "].desc'/></td></tr>");
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


$(function() {
	
	$("#add_node_image").click(addNodeImageHandler);
	$('#add_node_feature').click(addNodeFeatureHandler);
	$("#add_node_attribute").click(addNodeAttributeHandler);
	findParentNodeDialog = $("#findParentNodeDialog").dialog({ autoOpen: false });
	
	$("#submit_form").click(function() {
		$('#add_node_form').submit();
		return false;
	});
});