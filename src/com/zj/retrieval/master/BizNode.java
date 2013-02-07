package com.zj.retrieval.master;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jamesmurty.utils.XMLBuilder;

public class BizNode {
	
	private static final Logger logger = LoggerFactory.getLogger(BizNode.class);
	
	public static String createOwl(Node node) throws Exception {

		XMLBuilder xml = XMLBuilder.create("rdf:RDF");
		
		xml.a("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		xml.a("xmlns:owl", "http://www.w3.org/2002/07/owl#");
		xml.a("xmlns:rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		xml.a("xmlns:xsd", "http://www.w3.org/2001/XMLSchema#");
		
		createClassTypeXML(node, xml);

		String xmlStr = xml.asString();
		logger.debug(xmlStr);
		return xmlStr;
	}
	
	public static AttributeSelector getAttributeSelector(Node nd) {
		List<Integer> resultData = new ArrayList<Integer>();
		List<NodeFeature> attrs = nd.getRetrievalDataSource().getFeatures();
		for (int i = 0; i < attrs.size(); i++) {
			resultData.add(i);
		}
		return new AttributeSelector(resultData);
	}
	
	private static String nullEmpty(String value) {
		return (value == null ? StringUtils.EMPTY : value);
	}

	private static void createClassTypeXML(Node node, XMLBuilder xml) throws Exception {

		// Create <owl:Class>
		String parentURI = StringUtils.EMPTY;
		if (node.getParentNode() != null)
			parentURI = (node.getParentNode().getId() == Node.VIRTUAL_NODE_ID ? StringUtils.EMPTY :	nullEmpty(node.getParentNode().getUri()));
		String rdfId = nullEmpty(node.getUri()) + "#" + nullEmpty(node.getEnglishName());
		XMLBuilder elemClass = xml.e("owl:Class").a("rdf:ID", rdfId);
		elemClass.e("rdfs:subClassOf").a("rdf:resource", nullEmpty(parentURI));
		elemClass.e("rdfs:label").t(nullEmpty(node.getLabel()));
		
		// Create <desc>
		elemClass.e("desc").t(nullEmpty(node.getDesc()));
		
		// Create <images>
		XMLBuilder eImages = elemClass.e("images");
		for (NodeImage nodeImage : node.getImages()) {
			eImages.e("item").t(nullEmpty(nodeImage.getPath()));
		}
		
		// Create <attributes>
		XMLBuilder elemUserfields = elemClass.e("attributes");
		for (NodeAttribute attr : node.getAttributes()) {
			elemUserfields.e("attribute").a("key", nullEmpty(attr.getKey())).t(nullEmpty(attr.getValue()));
			
		}

		// Create <features>
		List<NodeFeature> features = node.getRetrievalDataSource().getFeatures();
		XMLBuilder elemAttributes = elemClass.e("features");
		for(int index = 0; index < features.size(); index ++) {
			
			XMLBuilder elemAttribute = elemAttributes.e("feature");
			elemAttribute.a("name", nullEmpty(features.get(index).getName()))
				         .a("english_name", nullEmpty(features.get(index).getEnglishName()))
				         .a("index", String.valueOf(index))
					     	.e("desc").t(nullEmpty(features.get(index).getDesc())).up();
			
			XMLBuilder featureImages = elemAttribute.e("images");
			for (FeatureImage featureImage : features.get(index).getImages()) {
				featureImages.e("image").a("path", nullEmpty(featureImage.getPath()));
			}
		}

		// Create <matrix>
		XMLBuilder elemMatrix = elemClass.e("matrix");
		Matrix matrix = node.getRetrievalDataSource().getMatrix();
		for(int rowindex = 0; rowindex < matrix.getRowSize(); rowindex++) {
			elemMatrix.e("row").a("index", String.valueOf(rowindex))
				.t(StringUtils.join(matrix.getRow(rowindex).getValueList(), " "));
		}
		
		// Create <childNodes>
		XMLBuilder elemChildNodes = elemClass.e("child_nodes");
		List<Node> childNodes = node.getChildNodes();
		for(int index = 0; index < childNodes.size(); index++) {
			elemChildNodes.e("node").a("index", String.valueOf(index)).t(nullEmpty(childNodes.get(index).getName()));
		}
	
	}
	
	private static void createIndividualTypeXML(Node node, XMLBuilder xml) throws Exception {

		String parentEnName = StringUtils.EMPTY;
		if (node.getParentNode() != null)
			nullEmpty(parentEnName = node.getParentNode().getEnglishName());
		XMLBuilder nodeXml = xml.e(parentEnName);
		nodeXml.a("rdf:ID", nullEmpty(node.getUri()) + "#" + nullEmpty(node.getEnglishName()))
			.e("label").t(nullEmpty(node.getLabel())).up()
			.e("name").t(nullEmpty(node.getName())).up()
			.e("desc").t(nullEmpty(node.getDesc()));

		// Create <images>
		XMLBuilder eImages = nodeXml.e("images");
		for (NodeImage nodeImage : node.getImages()) {
			eImages.e("item").t(nullEmpty(nodeImage.getPath()));
		}
		
		// Create <attributes>
		XMLBuilder elemUserfields = nodeXml.e("attributes");
		for (NodeAttribute attr : node.getAttributes()) {
			elemUserfields.e("attribute").a("key", nullEmpty(attr.getKey())).t(nullEmpty(attr.getValue()));
		}
	}
}
