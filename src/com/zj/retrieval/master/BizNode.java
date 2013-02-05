package com.zj.retrieval.master;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jamesmurty.utils.XMLBuilder;

public class BizNode {
	
	private static final Logger logger = LoggerFactory.getLogger(BizNode.class);
	
	private static Node getParent(String id) throws Exception {
		return Configuration.getNodeDao().queryById(id);
	}
	
	public static String createOwl(Node node) throws Exception {

		XMLBuilder xml = XMLBuilder.create("rdf:RDF");
		
		xml.a("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		xml.a("xmlns:owl", "http://www.w3.org/2002/07/owl#");
		xml.a("xmlns:rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		xml.a("xmlns:xsd", "http://www.w3.org/2001/XMLSchema#");
		
		createClassTypeXML(node, xml);

		return xml.asString();
	}

	private static void createClassTypeXML(Node node, XMLBuilder xml) throws Exception {

		// Create <owl:Class>
		String parentURI = (node.getParentNode().getId() == Node.VIRTUAL_NODE_ID ? 
				StringUtils.EMPTY :	node.getParentNode().getUri());
		XMLBuilder elemClass = xml.e("owl:Class").a("rdf:ID", node.getUri() + "#" + node.getEnglishName());
		elemClass.e("rdfs:subClassOf").a("rdf:resource", parentURI);
		elemClass.e("rdfs:label").t(node.getLabel());
		
		// Create <desc>
		elemClass.e("desc").t(node.getDesc());
		
		// Create <images>
		XMLBuilder eImages = elemClass.e("images");
		for (NodeImage nodeImage : node.getImages()) {
			eImages.e("item").t(nodeImage.getPath());
		}
		
		// Create <attributes>
		XMLBuilder elemUserfields = elemClass.e("attributes");
		for (NodeAttribute attr : node.getAttributes()) {
			elemUserfields.e("attribute").a("key", attr.getKey()).t(attr.getValue());
			
		}

		// Create <features>
		List<NodeFeature> features = node.getRetrievalDataSource().getFeatures();
		XMLBuilder elemAttributes = elemClass.e("features");
		for(int index = 0; index < features.size(); index ++) {
			
			XMLBuilder elemAttribute = elemAttributes.e("feature");
			elemAttribute.a("name", features.get(index).getName())
				         .a("english_name", features.get(index).getEnglishName())
				         .a("index", String.valueOf(index))
					     	.e("desc").t(features.get(index).getDesc()).up();
			
			XMLBuilder featureImages = elemAttribute.e("images");
			for (FeatureImage featureImage : features.get(index).getImages()) {
				featureImages.e("image").a("path", featureImage.getPath());
			}
			
			Map<String, String> attrUserfields = features.get(index).getUserFields();
			XMLBuilder elemAttrUserfields = elemAttribute.element("userfields");
			for (String key : attrUserfields.keySet()) {
				elemAttrUserfields.e("field").a("key", key).t(attrUserfields.get(key));
			}
		}

		// Create <matrix>
		XMLBuilder elemMatrix = elemClass.e("matrix");
		Matrix matrix = node.getRetrievalDataSource().getMatrix();
		for(int rowindex = 0; rowindex < matrix.getRowSize(); rowindex++) {
			int[] row = matrix.getRow(rowindex);
			elemMatrix.e("row").a("index", String.valueOf(rowindex)).t(StringUtils.join(ArrayUtils.toObject(row)));
		}
		
		// Create <childNodes>
		XMLBuilder elemChildNodes = elemClass.e("childNodes");
		List<String> childNodes = node.getRetrievalDataSource().getChildNodes();
		for(int index = 0; index < childNodes.size(); index++) {
			elemChildNodes.e("node").a("index", String.valueOf(index))
				.t(childNodes.get(index));
		}
	
	}
	
	private static void createIndividualTypeXML(Node node, XMLBuilder xml) throws Exception {

		String parentEnName = getParent(node.getParentId()).getEnglishName();
		XMLBuilder individual = xml.e(parentEnName);
		individual.a("rdf:ID", node.getUri() + "#" + node.getEnglishName())
			.e("label").t(node.getLabel()).up()
			.e("name").t(node.getName()).up()
			.e("desc").t(node.getDesc());

		XMLBuilder eImages = individual.e("images");
		for (String image_path : node.getImages()) {
			eImages.e("item").t(image_path);
		}
		
		// Create <userfields>
		XMLBuilder elemUserfields = individual.e("userfields");
		Map<String, String> userfields = node.getUserfields();
		for (String key : userfields.keySet()) {
			elemUserfields.e("field").a("key", key).t(userfields.get(key));
		}
	}
}
