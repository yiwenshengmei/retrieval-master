package com.zj.retrieval.master;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
	
	private static List<String> saveImageFiles(File[] files, String[] fileNames, File folder, String msg) throws IOException {
		List<String> paths = new ArrayList<String>();
		if (files == null) return paths;
		for (int i = 0; i < files.length; i++) {
			File destFile = new File(folder, UUID.randomUUID().toString() + ".jpg");
			FileUtils.copyFile(files[i], destFile);
			logger.debug(String.format("%1$s, %2$s -> %3$s", msg, fileNames[i], destFile));
			paths.add(destFile.getPath());
		}
		return paths;
	}
	
	public static void saveAndPrepareImages(Node node, String savePath) throws IOException {
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdirs();
			logger.debug("���ڱ���ͼƬ���ļ��в����ڣ���ʼ����: " + folder.getPath());
		}
		
		List<String> paths = saveImageFiles(node.getImageFiles(), node.getImageFilesFileName(), folder, "Save a NodeImage");
		node.setImages(NodeImage.batchCreate(paths, node));
		
		if (node.getRetrievalDataSource() != null && node.getRetrievalDataSource().getFeatures() != null) {
			for (NodeFeature feature : node.getRetrievalDataSource().getFeatures()) {
				List<String> featureImagePaths = saveImageFiles(feature.getImageFiles(), 
						feature.getImageFilesFileName(), folder, "Save a NodeFeatureImage");
				feature.setImages(FeatureImage.batchCreate(featureImagePaths, feature));
			}
		}
	}
	
	public static void rebuildRelation(Node node) {
		RetrievalDataSource rds = node.getRetrievalDataSource();
		rds.setNode(node);
		for (NodeAttribute attr : node.getAttributes()) {
			attr.setNode(node);
		}
		rds.getMatrix().setRetrievalDataSource(rds);
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
	
	public static void addChildToParent(Node child, Node parent, List<NodeFeature> newFeatures) {
		// ���¸��ڵ���ӽڵ��б�
		parent.getChildNodes().add(child);
		// ���¸��ڵ������
		List<NodeFeature> parentFeatures = parent.getRetrievalDataSource().getFeatures();
		parentFeatures.addAll(newFeatures);
		// ���¸��ڵ�ľ���
		Matrix mtx = parent.getRetrievalDataSource().getMatrix();
		// �½�һ�У��䳤�Ƚ����ڸ��ڵ���������
		MatrixRow newRow = new MatrixRow();
		// ������������������У������Ǹ��ڵ㵱ǰ���������Feature������node.getFeaturesOfParent�У�����Yes��������дNo
		for(int i = 0; i < mtx.getColSize(); i++) {
			MatrixItem item = containsFeature(child.getFeaturesOfParent(), parentFeatures.get(i)) ? MatrixItem.Yes(newRow) : MatrixItem.No(newRow);
			newRow.addItem(item);
		}
		mtx.addRow(newRow);
		
		// ���踸�ڵ��������3�У�����Ҫ���4���������������4��[0, 0, 1]����������ӵ����ڵ������
		for (int i = 0; i < newFeatures.size(); i++) {
			int rowSize = mtx.getRowSize();
			List<MatrixItem> newCol = new ArrayList<MatrixItem>();
			for (int j = 0; j < rowSize; j++) {
				newCol.add( ((j + 1) == rowSize) ? MatrixItem.Yes(null) : MatrixItem.Unknow(null));
			}
			mtx.addCol(newCol);
		}
	}
	
	/**
	 * ���feature�Ƿ������features�У�ƥ��ԭ���ǶԱ�id
	 * @param features ������Ŀ�꼯��
	 * @param feature ������Ŀ��
	 * @return �����򷵻�true�����򷵻�false
	 */
	private static boolean containsFeature(List<NodeFeature> features, NodeFeature feature) {
		for (NodeFeature f : features) {
			if (f.getId() == null)
				throw new IllegalArgumentException("���ڱȽϵ�Feature��id����Ϊnull��");
			if (f.getId().equals(feature.getId()))
				return true;
		}
		return false;
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
			eImages.e("item").t(nodeImage.getPath() == null ? StringUtils.EMPTY : FilenameUtils.getName(nodeImage.getPath()));
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
	
	private static void initialize(Node node) {
		Hibernate.initialize(node.getChildNodes());
		Hibernate.initialize(node.getImages());
		Hibernate.initialize(node.getParentNode());
		Hibernate.initialize(node.getAttributes());
		RetrievalDataSource rds = node.getRetrievalDataSource();
		Hibernate.initialize(rds.getMatrix());
		for (NodeFeature feature : rds.getFeatures()) {
			Hibernate.initialize(feature.getImages());
		}
		for (MatrixRow row : rds.getMatrix().getRows()) {
			Hibernate.initialize(row.getItems());
		}
	}
	
	public static Node getNode(final String id) throws Exception {
		Node nd = (Node) DALService.doAction(new IDALAction() {
			@Override
			public Object doAction(Session sess, Transaction tx) throws Exception {
				Node nd = (Node) sess.get(Node.class, id);
				initialize(nd);
				nd.setOwl(BizNode.createOwl(nd));
				return nd;
			}
		});
		return nd;
	}
	
	public static void changePath2Url(Node node) {
		String contextPath = ServletActionContext.getServletContext().getContextPath(); // ����"/retrieval-master"
		for (NodeImage img : node.getImages()) {
			img.setUrl(contextPath + "/images/" + FilenameUtils.getName(img.getPath()));
		}
		RetrievalDataSource rds = node.getRetrievalDataSource();
		for (NodeFeature feature : rds.getFeatures()) {
			for (FeatureImage img : feature.getImages())
				img.setUrl(contextPath + "/images/" + FilenameUtils.getName(img.getPath()));
		}
	}
}
