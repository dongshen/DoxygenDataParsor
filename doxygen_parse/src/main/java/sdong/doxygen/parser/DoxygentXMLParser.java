package sdong.doxygen.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sdong.common.exception.SdongException;
import sdong.common.utils.FileUtil;
import sdong.doxygen.bean.DoxygenChildNode;
import sdong.doxygen.bean.DoxygenCompound;
import sdong.doxygen.bean.DoxygenConstant;
import sdong.doxygen.bean.DoxygenIncType;
import sdong.doxygen.bean.DoxygenLocation;
import sdong.doxygen.bean.DoxygenMember;
import sdong.doxygen.bean.DoxygenNode;
import sdong.doxygen.bean.DoxygenParam;
import sdong.doxygen.bean.DoxygenReference;

public class DoxygentXMLParser {

	private static final Logger logger = LoggerFactory.getLogger(DoxygentXMLParser.class);

	public static ConcurrentHashMap<String, DoxygenCompound> getProjectCompound(String indexFileName) {
		ConcurrentHashMap<String, DoxygenCompound> compoundMap = null;
		try {
			String folder = FileUtil.getFolderName(indexFileName);
			DoxygenCompound compound;

			// get compound list
			compoundMap = parseDoxygenXmlIndex(indexFileName);

			for (Map.Entry<String, DoxygenCompound> entry : compoundMap.entrySet()) {
				compound = entry.getValue();
				if (compound.getKind().equalsIgnoreCase("file")) {
					compound = parseDoxygenXmlFile(folder + File.separatorChar + entry.getKey() + ".xml");
					compoundMap.put(entry.getKey(), compound);
				}
			}

		} catch (SdongException e) {
			logger.error(e.getMessage(), e);
		}

		return compoundMap;

	}

	public static ConcurrentHashMap<String, DoxygenCompound> parseDoxygenXmlIndex(String fileName)
			throws SdongException {
		logger.info("Start process index file:" + fileName);
		ConcurrentHashMap<String, DoxygenCompound> compoundMap = new ConcurrentHashMap<String, DoxygenCompound>();
		DoxygenCompound compound = null;

		DoxygenMember member = null;

		boolean isCompound = false;
		boolean isMember = false;
		boolean isName = false;
		int attributeCount = 0;
		String attributeValue = "";
		String attributeName = "";
		String tagValue = "";
		String qName = "";
		String memberKind = "";
		int event;

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(fileName));
			while (xmlStreamReader.hasNext()) {
				event = xmlStreamReader.next();

				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					qName = xmlStreamReader.getLocalName();
					if (qName.equals(DoxygenConstant.COMPOUND)) {
						isCompound = true;
						compound = new DoxygenCompound();
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("refid")) {
								compound.setRefid(attributeValue);
							} else if (attributeName.equalsIgnoreCase("kind")) {
								compound.setKind(attributeValue);
							}
						}
						compoundMap.put(compound.getRefid(), compound);
					} else if (qName.equals(DoxygenConstant.MEMBER)) {
						isMember = true;
						member = new DoxygenMember();
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("refid")) {
								member.setRefid(attributeValue);
							} else if (attributeName.equalsIgnoreCase("kind")) {
								member.setKind(attributeValue);
							}
						}
						memberKind = member.getKind();
						if (compound.getMembers(memberKind) != null) {
							compound.getMembers(memberKind).put(member.getRefid(), member);
						} else {
							throw new SdongException("Cant find kind" + memberKind);
						}

					} else if (qName.equals("name")) {
						isName = true;
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if (isName == true) {
						tagValue = xmlStreamReader.getText();
						if (isMember == true) {
							member.setName(tagValue);
						} else if (isCompound == true) {
							compound.setName(tagValue);
						}
						isName = false;
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					qName = xmlStreamReader.getLocalName();
					if (qName.equalsIgnoreCase(DoxygenConstant.COMPOUND)) {
						isCompound = false;
					} else if (qName.equalsIgnoreCase(DoxygenConstant.MEMBER)) {
						isMember = false;
					}
					break;
				}
			}

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new SdongException(e);
		} catch (XMLStreamException e) {
			logger.error(e.getMessage(), e);
			throw new SdongException(e);
		}

		logger.info("End process index file:" + fileName);
		return compoundMap;
	}

	public static DoxygenCompound parseDoxygenXmlFile(String fileName) throws SdongException {

		logger.info("Start process file:" + fileName);

		DoxygenCompound compound = new DoxygenCompound();

		int attributeCount = 0;
		String attributeValue = "";
		String attributeName = "";
		String tagValue = "";
		String qName = "";
		String id = "";
		String refid = "";
		int event;

		boolean isIncludes = false;
		boolean isIncludedby = false;
		boolean isNode = false;
		boolean isLabel = false;
		boolean isMember = false;
		boolean isName = false;
		boolean isMemberName = false;
		boolean isReferencedby = false;
		boolean isReference = false;
		boolean isType = false;
		boolean isDefinition = false;
		boolean isArgsstring = false;
		boolean isParam = false;
		boolean isDclname = false;
		String memberKind = "";

		DoxygenIncType include = null;
		DoxygenIncType includedby = null;
		DoxygenNode node = null;
		DoxygenChildNode childNode = null;
		ConcurrentHashMap<String, DoxygenMember> members;
		DoxygenMember member = null;
		DoxygenLocation location = null;
		DoxygenReference reference = null;
		DoxygenReference referenceby = null;
		DoxygenParam param = null;

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {

			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(fileName));
			while (xmlStreamReader.hasNext()) {
				event = xmlStreamReader.next();

				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					qName = xmlStreamReader.getLocalName();
					if (qName.equals(DoxygenConstant.COMPOUND_DEF)) {
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("id")) {
								compound.setRefid(attributeValue);
							} else if (attributeName.equalsIgnoreCase("kind")) {
								compound.setKind(attributeValue);
							} else if (attributeName.equalsIgnoreCase("language")) {
								compound.setLanguage(attributeValue);
							}
						}
					} else if (qName.equals(DoxygenConstant.COMPOUND_NAME)) {
						isName = true;
					} else if (qName.equals(DoxygenConstant.COMPOUND_INCLUDES)) {
						isIncludes = true;
						include = new DoxygenIncType();
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("local")) {
								include.setLocal(attributeValue);
							} else if (attributeName.equalsIgnoreCase("refid")) {
								include.setRefid(attributeValue);
							}
						}
					} else if (qName.equals(DoxygenConstant.COMPOUND_INCLUDEDBY)) {
						isIncludedby = true;
						includedby = new DoxygenIncType();
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("local")) {
								includedby.setLocal(attributeValue);
							} else if (attributeName.equalsIgnoreCase("refid")) {
								includedby.setRefid(attributeValue);
							}
						}
					} else if (qName.equals(DoxygenConstant.COMPOUND_INCDEPGRAPH_NODE)) {
						isNode = true;
						node = new DoxygenNode();
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("id")) {
								node.setId(attributeValue);
							}
						}
					} else if (qName.equals("label")) {
						isLabel = true;
					} else if (qName.equals("link")) {
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("refid")) {
								node.setRefid(attributeValue);
							} 
						}	
					} else if (qName.equals(DoxygenConstant.COMPOUND_INCDEPGRAPH_NODE_CHILDNODE)) {
						childNode = new DoxygenChildNode();
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("refid")) {
								childNode.setRefid(attributeValue);
							}else if (attributeName.equalsIgnoreCase("relation")) {
								childNode.setRelation(attributeValue);
							}  
						}						
					} else if (qName.equals(DoxygenConstant.MEMBER_DEF)) {
						isMember = true;
						id = xmlStreamReader.getAttributeValue(null, "id");
						memberKind = xmlStreamReader.getAttributeValue(null, "kind");
						if (id == null || memberKind == null) {
							throw new SdongException("Cant find id or kind in memberdef!");
						}

						members = compound.getMembers(memberKind);

						if (members.containsKey(id)) {
							member = members.get(id);
						} else {
							member = new DoxygenMember();
							member.setRefid(id);
							member.setKind(memberKind);
							members.put(id, member);
						}

						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("prot")) {
								member.setPort(attributeValue);
							} else if (attributeName.equalsIgnoreCase("static")) {
								member.setStatic(attributeValue);
							} else if (attributeName.equalsIgnoreCase("const")) {
								member.setConst(attributeValue);
							} else if (attributeName.equalsIgnoreCase("explicit")) {
								member.setExplicit(attributeValue);
							} else if (attributeName.equalsIgnoreCase("inline")) {
								member.setInline(attributeValue);
							} else if (attributeName.equalsIgnoreCase("virt")) {
								member.setVirt(attributeValue);
							}
						}
					} else if (qName.equals("name")) {
						isMemberName = true;				
					} else if (qName.equals("type")) {
						isType = true;
					} else if (qName.equals("definition")) {
						isDefinition = true;
					} else if (qName.equals("argsstring")) {
						isArgsstring = true;
					} else if (qName.equals("param")) {
						isParam = true;
						param = new DoxygenParam();
					} else if (qName.equals("declname")) {
						isDclname = true;
					} else if (qName.equals("location")) {
						if (isMember == true) {
							location = member.getLocation();
						} else {
							location = compound.getLocation();
						}
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("file")) {
								location.setFile(attributeValue);
							} else if (attributeName.equalsIgnoreCase("line")) {
								location.setLine(attributeValue);
							} else if (attributeName.equalsIgnoreCase("column")) {
								location.setColumn(attributeValue);
							} else if (attributeName.equalsIgnoreCase("bodyfile")) {
								location.setBodyfile(attributeValue);
							} else if (attributeName.equalsIgnoreCase("bodystart")) {
								location.setBodystart(attributeValue);
							} else if (attributeName.equalsIgnoreCase("bodyend")) {
								location.setBodyend(attributeValue);
							}
						}
					} else if (qName.equals(DoxygenConstant.MEMBER_REFERENCEDBY)) {
						isReferencedby = true;
						refid = xmlStreamReader.getAttributeValue(null, "refid");
						if (refid == null) {
							throw new SdongException("Cant find refid in memberdef referencedby!");
						}

						if (member.getReferencedby().contains(refid)) {
							referenceby = member.getReferencedby().get(refid);
						} else {
							referenceby = new DoxygenReference();
							referenceby.setRefid(refid);
							member.getReferencedby().put(refid, referenceby);
						}
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("compoundref")) {
								referenceby.setCompoundref(attributeValue);
							} else if (attributeName.equalsIgnoreCase("startline")) {
								referenceby.setStartline(attributeValue);
							} else if (attributeName.equalsIgnoreCase("endline")) {
								referenceby.setEndline(attributeValue);
							}
						}
					} else if (qName.equals(DoxygenConstant.MEMBER_REFERENCE)) {
						isReference = true;
						refid = xmlStreamReader.getAttributeValue(null, "refid");
						if (refid == null) {
							throw new SdongException("Cant find refid in memberdef reference!");
						}

						if (member.getReferencedby().contains(refid)) {
							reference = member.getReference().get(refid);
						} else {
							reference = new DoxygenReference();
							reference.setRefid(refid);
							member.getReference().put(refid, reference);
						}
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("compoundref")) {
								reference.setCompoundref(attributeValue);
							} else if (attributeName.equalsIgnoreCase("startline")) {
								reference.setStartline(attributeValue);
							}
						}
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					tagValue = xmlStreamReader.getText().trim();
					if (isName == true) {
						compound.setName(tagValue);
						isName = false;
					} else if (isIncludes == true) {
						isIncludes = false;
						include.setName(tagValue);
						compound.getIncludes().put(include.getName(), include);
					} else if (isIncludedby == true) {
						isIncludedby = false;
						includedby.setName(tagValue);
						compound.getIncludedby().put(includedby.getName(), includedby);
					} else if (isLabel == true) {
						isLabel = false;
						node.setLabel(tagValue);						
					} else if (isMemberName == true) {
						member.setName(tagValue);
						isMemberName = false;
					} else if (isReferencedby == true) {
						referenceby.setName(tagValue);
						isReferencedby = false;
					} else if (isReference == true) {
						reference.setName(tagValue);
						isReference = false;
					} else if (isType == true) {
						if (isParam == true) {
							param.setType(tagValue);
						} else {
							member.setType(tagValue);
						}
						isType = false;
					} else if (isDefinition == true) {
						member.setDefinition(tagValue);
						isDefinition = false;
					} else if (isArgsstring == true) {
						member.setArgsstring(tagValue);
						isArgsstring = false;
					} else if (isDclname == true) {
						param.setDeclname(tagValue);
						isDclname = false;
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					qName = xmlStreamReader.getLocalName();
					if (qName.equalsIgnoreCase(DoxygenConstant.MEMBER_DEF)) {
						isMember = false;
					} else if (qName.equalsIgnoreCase(DoxygenConstant.COMPOUND_INCDEPGRAPH_NODE)) {
						compound.getIncdepgraph().put(node.getId(), node);
						isNode = false;
					} else if (qName.equalsIgnoreCase(DoxygenConstant.COMPOUND_INCDEPGRAPH_NODE_CHILDNODE)) {
						node.getChildNodes().put(childNode.getRefid(), childNode);
					} else if (qName.equalsIgnoreCase("param")) {
						member.getParams().add(param);
						isParam = false;
					}
					break;
				}
			}

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new SdongException(e);
		} catch (XMLStreamException e) {
			logger.error(e.getMessage(), e);
			throw new SdongException(e);
		}

		logger.info("End process file:" + fileName);
		return compound;
	}

	public static List<DoxygenMember> getTopFunction(ConcurrentHashMap<String, DoxygenCompound> compoundMap) {
		List<DoxygenMember> topFunction = new ArrayList<DoxygenMember>();

		// get all function members in compound
		ConcurrentHashMap<String, DoxygenMember> allFunctions = new ConcurrentHashMap<String, DoxygenMember>();

		for (Map.Entry<String, DoxygenCompound> entry : compoundMap.entrySet()) {
			if (entry.getValue().getKind().equalsIgnoreCase("file")) {
				allFunctions.putAll(entry.getValue().getMembers(DoxygenMember.MEMBER_KIND_FUNCTION));
			}
		}

		// get all reference
		for (Map.Entry<String, DoxygenMember> entry : allFunctions.entrySet()) {
			if (entry.getValue().getReferencedby().size() == 0) {
				topFunction.add(entry.getValue());
			}
		}

		return topFunction;
	}

}
