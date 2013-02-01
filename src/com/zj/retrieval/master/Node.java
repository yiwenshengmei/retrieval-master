package com.zj.retrieval.master;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node {
	private static Logger logger = LoggerFactory.getLogger(Node.class);

	public final static String VIRTUAL_NODE_ID = "VIRTUAL_NODE";

	private String uri;
	private String id;
	private String name;
	private String uriName;
	private String englishName;
	private String desc;
	private String parentId;
	private String owl;
	private RetrievalDataSource retrievalDataSource;
	private String label;
	private String detailTypeId;
	private String contact;
	private List<NodeImage> images;
	private List<NodeAttribute> attributes;
	private List<Node> childNodes;

	public Node() {
		retrievalDataSource = new RetrievalDataSource(this);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUriName() {
		return uriName;
	}

	public void setUriName(String uriName) {
		this.uriName = uriName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String enName) {
		this.englishName = enName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getOwl() {
		return owl;
	}

	public void setOwl(String owl) {
		this.owl = owl;
	}

	public RetrievalDataSource getRetrievalDataSource() {
		return retrievalDataSource;
	}

	public void setRetrievalDataSource(RetrievalDataSource retrievalDataSource) {
		this.retrievalDataSource = retrievalDataSource;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<NodeImage> getImages() {
		return images;
	}

	public void setImages(List<NodeImage> image) {
		this.images = image;
	}

	//	public static Node parseVirtualNodeFromOWL(String owl) throws Exception {
	//		Node result = new Node();
	//		XMLBuilder builder = XMLBuilder.parse(new InputSource(new StringReader(owl)));
	//		Element elem = builder.xpathFind("/RDF/Class/childNodes").getElement();
	//		for(int i = 0; i < elem.getChildNodes().getLength(); i++) {
	//			Element elemNode = (Element)elem.getChildNodes().item(i);
	//			String text = elemNode.getTextContent();
	//			result.getRetrievalDataSource().getChildNodes().add(text);
	//		}
	//		logger.info("解析出VirtualNode的子结点列表" + result.getRetrievalDataSource().getChildNodes());
	//		return result;
	//	}
	//	public static void parseNodeFromOWL(Node nd) throws Exception {
	//		try {
	//			
	//			if (nd.getOwl() == null || nd.getOwl().equals("")) {
	//				nd.setDesc("");
	//				RetrievalDataSource rds = new RetrievalDataSource();
	//				rds.setAttributes(new ArrayList<NodeAttribute>());
	//				rds.setChildNodes(new ArrayList<String>());
	//				rds.setMatrix(new Matrix());
	//				nd.setRetrievalDataSource(rds);
	//				return;
	//			}
	//			
	//			XMLBuilder builder = XMLBuilder.parse(new InputSource(new StringReader(nd.getOwl())));
	//			
	////			// 解析节点类型
	////			int nodeType = -1;
	////			try {
	////				nodeType = Integer.valueOf(builder.xpathFind("/RDF/Class/nodeType").getElement().getTextContent());
	////			} catch (XPathExpressionException e) {
	////				//throw new Exception("不存在节点/RDF/Class/nodeType", e);
	////			}
	//			
	//			// 解析Attribute
	//			List<NodeAttribute> attrs = new ArrayList<NodeAttribute>();
	//			try {
	//				NodeList attributesElements = builder.xpathFind("/RDF/Class/attributes").getElement().getChildNodes();
	//				for(int i = 0; i < attributesElements.getLength(); i++) {
	//					Element attrElement = (Element)attributesElements.item(i);
	//					int attrIndex = Integer.valueOf(attrElement.getAttribute("index"));
	//					String attrEnName = attrElement.getAttribute("enName");
	//					String attrName = attrElement.getAttribute("name");
	//					String attrDesc = attrElement.getElementsByTagName("desc").item(0).getTextContent();
	//					String attrImage = attrElement.getElementsByTagName("image").item(0).getTextContent();
	//					
	//					NodeAttribute attr = new NodeAttribute(attrName, attrEnName, attrDesc, attrImage);
	//					
	//					NodeList fields = attrElement.getElementsByTagName("field");
	//					Map<String, String> attrUserfields = new HashMap<String, String>();
	//					for (int j = 0; j < fields.getLength(); j++) {
	//						Element field = (Element) fields.item(j);
	//						attrUserfields.put(field.getAttribute("key"), field.getTextContent());
	//					}
	//					attr.setUserFields(attrUserfields);
	//					attrs.add(attrIndex, attr);
	//				}
	//			} catch (XPathExpressionException e) {
	//				logger.info("不存在节点/RDF/Class/attributes");
	//			}
	//			
	//			// 解析Matrix
	//			Matrix matrix = new Matrix();
	//			try {
	//				NodeList rowElements = builder.xpathFind("/RDF/Class/matrix").getElement().getChildNodes();
	//				int rowSize = rowElements.getLength();
	//				int colSize = rowSize == 0 ? 0 : rowElements.item(0).getTextContent().length();
	//				for(int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
	//					int[] row = new int[colSize];
	//					String rowString = rowElements.item(rowIndex).getTextContent();
	//					for(int dig = 0; dig < colSize; dig++) {
	//						row[dig] = Integer.valueOf(rowString.substring(dig, dig + 1));
	//					}
	//					matrix.addRow(row, 0, row.length);
	//				}
	//			} catch (XPathExpressionException e) {
	//				logger.info("不存在节点/RDF/Class/matrix");
	//			}
	//
	//			// 解析ChildList
	//			List<String> child_nodes = new ArrayList<String>();
	//			try {
	//				NodeList nodeElements = builder.xpathFind("/RDF/Class/childNodes").getElement().getChildNodes();
	//				for(int i = 0; i < nodeElements.getLength(); i++) {
	//					String child_node_id = nodeElements.item(i).getTextContent();
	//					child_nodes.add(child_node_id);
	//				}
	//			} catch (XPathExpressionException e) {
	//				logger.info("不存在节点/RDF/Class/childNodes");
	//			}
	//			
	//			// 解析userfields
	//			Map<String, String> user_fields = new HashMap<String, String>();
	//			try {
	//				NodeList nodeFields = builder.xpathFind("/RDF/Class/userfields").getElement().getChildNodes();
	//				for (int i = 0; i < nodeFields.getLength(); i++) {
	//					String key = ((Element) nodeFields.item(i)).getAttribute("key");
	//					String value = nodeFields.item(i).getTextContent();
	//					user_fields.put(key, value);
	//				}
	//			} catch (XPathExpressionException e) {
	//				logger.info("不存在节点/RDF/Class/userfields");
	//			}
	//			
	//			// 解析Desc
	//			String desc = "";
	//			try {
	//				desc = builder.xpathFind("/RDF/Class/desc").getElement().getTextContent();
	//			} catch(XPathExpressionException e) {
	//				logger.info("不存在节点/RDF/Class/desc, desc将保持为空");
	//			}
	//			
	//			RetrievalDataSource data_source = new RetrievalDataSource();
	//			data_source.setAttributes(attrs);
	//			data_source.setMatrix(matrix);
	//			data_source.setChildNodes(child_nodes);
	//			
	//			nd.setRetrievalDataSource(data_source);
	//			nd.setDesc(desc);
	//			nd.setUserfields(user_fields);
	//			
	//		} catch (Exception e) {
	//			logger.error("解析OWL时出错", e);
	//			throw new Exception("解析OWL时出错@NodeServiceImpl.getRetrievalDataSource()", e);
	//		}
	//	
	//	}

	public String getDetailTypeId() {
		return detailTypeId;
	}

	public void setDetailTypeId(String detailTypeId) {
		this.detailTypeId = detailTypeId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void addNodeAttribute(NodeFeature attr) {
		this.retrievalDataSource.getAttributes().add(attr);
	}


	public List<NodeAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<NodeAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<Node> childNodes) {
		this.childNodes = childNodes;
	}
}
