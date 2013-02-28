function deleteNodeAttribute(index) {
	$('#node_attribute_' + index).remove();
}

function deleteNodeFeature(index) {
	$('#node_feature_' + index).remove();
}

function deleteParentFeature(index) {
	$('#parent_feature_' + index).remove();
}

var selectParentNodeDialog;
var selectParentFeatureDialog;
var nodeId;
var parentId;
/**
 * 
 */
function selectParentNodeHandler() {
	$.ajax({
		url: "node/getParentNodesSelectAjax.action",
		type: "POST",
		dataType: "json",
		success: function (data) {
			showSelectParentNodes(data);
		}
	});
}

/**
 * 点击选择父节点特征按钮的响应时间函数
 * 从服务端获得的数据会传给showSelectParentFeatures构建供选择的Dialog
 */
function selectParentFeatureHandler() {
	if (parentId == null) {
		alert("请先选择父类");
		return;
	}
	$.ajax({
		url: "feature/getParentFeatureAjax.action",
		type: "POST",
		dataType: "json",
		data: { parentId: parentId },
		success: function (data) {
			showSelectParentFeatures(data);
		}
	});
}

function selectParent(id, name) {
	parentId = id;
	selectParentNodeDialog.dialog("close");
	$("input[name='parentNode.id']").val(name);
}

var parentFeatureIndex = 0;

/**
 * 在parent_feature_location添加已经被选中的parent_feature
 * 每个feature带有删除按钮，点击删除按钮（调用deleteParentFeature函数）可以删除该feature
 * feature对应的input标签的name形如featuresOfParent[x].id
 * @param id
 * @param name
 */
function selectFeature(id, name) {
	var location = $("#parent_feature_location");
	var html = 
		"<div style='margin-top: 10px;' id='parent_feature_:index'>" +
			"<input type='hidden' name='featuresOfParent[:index].id' value=':id' />" + 
			"<span>" +
				"<input id='parent_feature_:index' type='text' name='parent_feature_:index' value=':name'/>" +
			"</span>" + 
			"<span>" +
				"<a class='button' href='#' onclick='deleteParentFeature(:index);'>删除</a>" +
			"</span>" +
		"</div>";
	html = html.replace(/:index/g, parentFeatureIndex);
	html = html.replace(/:name/g, name);
	html = html.replace(/:id/g, id);
	location.append($(html));
	parentFeatureIndex++;
	return false;
}

/**
 * 生成用于选择父节点特征的Dialog
 * 在生成的HTML中可以点击“选择”按钮调用selectFeature(id, name)函数
 * @param data
 */
function showSelectParentFeatures(data) {
	var tbl = $("#select_parent_feature_location");
	tbl.empty();
	for (var i = 0; i < data.features.length; i++) {
		var featureNameTD = $("<td/>").html(data.features[i].name);
		var selectLinkTD = $("<td/>").append($("<a href='#' onclick='selectFeature(\"" + data.features[i].id + "\", \"" + data.features[i].name + "\")'>选择</a>"));
		tbl.append($("<tr/>").append(featureNameTD).append(selectLinkTD));
	}
	selectParentFeatureDialog.dialog("open");
}

function showSelectParentNodes(data) {
	var tbl = $("#selectParentNodeTable");
	tbl.empty();
	for (var i = 0; i < data.nodes.length; i++) {
		var parentNodeNameTD = $("<td/>").html(data.nodes[i].name);
		var selectLinkTD = $("<td/>").append($("<a href='#' onclick='selectParent(\"" + data.nodes[i].id + "\", \"" + data.nodes[i].name + "\")'>选择</a>"));
		tbl.append($("<tr/>").append(parentNodeNameTD).append(selectLinkTD));
	}
	selectParentNodeDialog.dialog("open");
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
	selectParentNodeDialog = $("#findParentNodeDialog").dialog({ autoOpen: false });
	selectParentFeatureDialog = $("#select_parent_feature_dialog").dialog({ autoOpen: false });
	
	$("#submit_form").click(function() {
		$('#add_node_form').submit();
		return false;
	});
});