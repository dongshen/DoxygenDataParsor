package sdong.doxygen.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sdong.common.exception.SdongException;
import sdong.doxygen.bean.DoxygenCompound;
import sdong.doxygen.bean.DoxygenLocation;
import sdong.doxygen.bean.DoxygenMember;
import sdong.doxygen.bean.DoxygenReference;

public class DoxygentXMLParser {

	private static final Logger logger = LoggerFactory.getLogger(DoxygentXMLParser.class);

	public static ConcurrentHashMap<String, DoxygenCompound> parseDoxygenXmlIndex(String fileName)
			throws SdongException {

		ConcurrentHashMap<String, DoxygenCompound> compoundMap = new ConcurrentHashMap<String, DoxygenCompound>();
		DoxygenCompound compound = null;

		ConcurrentHashMap<String, DoxygenMember> members = new ConcurrentHashMap<String, DoxygenMember>();
		DoxygenMember member = null;

		boolean isCompound = false;
		boolean isMember = false;
		boolean isName = false;
		int attributeCount = 0;
		String attributeValue = "";
		String attributeName = "";
		String tagValue = "";
		String qName = "";
		int event;

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(fileName));
			while (xmlStreamReader.hasNext()) {
				event = xmlStreamReader.next();

				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					qName = xmlStreamReader.getLocalName();
					if (qName.equals("compound")) {
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
					} else if (qName.equals("member")) {
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
					if (qName.equalsIgnoreCase("compound")) {
						compound.setMembers(members);
						compoundMap.put(compound.getRefid(), compound);
						members = new ConcurrentHashMap<String, DoxygenMember>();
						isCompound = false;
					} else if (qName.equalsIgnoreCase("member")) {
						members.put(member.getRefid(), member);
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

		return compoundMap;
	}

	public static void parseDoxygenXmlFile(String fileName, DoxygenCompound compound) throws SdongException {
		int attributeCount = 0;
		String attributeValue = "";
		String attributeName = "";
		String tagValue = "";
		String qName = "";
		String id = "";
		String refid = "";
		int event;

		boolean isCompound = false;
		boolean isMember = false;
		boolean isName = false;
		boolean isMemberName = false;
		boolean isReferencedby = false;
		boolean isReference = false;
		boolean isType = false;
		boolean isDefinition = false;
		boolean isArgsstring = false;

		DoxygenMember member = null;
		DoxygenLocation location = null;
		DoxygenReference reference = null;
		DoxygenReference referenceby = null;

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try {
			if (compound == null) {
				compound = new DoxygenCompound();
			}

			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(fileName));
			while (xmlStreamReader.hasNext()) {
				event = xmlStreamReader.next();

				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					qName = xmlStreamReader.getLocalName();
					if (qName.equals("compounddef")) {
						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("refid")) {
								compound.setRefid(attributeValue);
							} else if (attributeName.equalsIgnoreCase("kind")) {
								compound.setKind(attributeValue);
							} else if (attributeName.equalsIgnoreCase("language")) {
								compound.setLanguage(attributeValue);
							}
						}
					} else if (qName.equals("memberdef")) {
						isMember = true;

						id = xmlStreamReader.getAttributeValue(null, "id");
						if (id == null) {
							throw new SdongException("Cant find id in memberdef!");
						}

						if (compound.getMembers().containsKey(id)) {
							member = compound.getMembers().get(id);
						} else {
							member = new DoxygenMember();
							member.setRefid(id);
							compound.getMembers().put(id, member);
						}

						attributeCount = xmlStreamReader.getAttributeCount();
						for (int i = 0; i < attributeCount; i++) {
							attributeValue = xmlStreamReader.getAttributeValue(i);
							attributeName = xmlStreamReader.getAttributeLocalName(i);
							if (attributeName.equalsIgnoreCase("id")) {
								member.setRefid(attributeValue);
							} else if (attributeName.equalsIgnoreCase("kind")) {
								member.setKind(attributeValue);
							} else if (attributeName.equalsIgnoreCase("prot")) {
								member.setPort(attributeValue);
							} else if (attributeName.equalsIgnoreCase("static")) {
								member.setStatic(attributeValue);
							}
						}
					} else if (qName.equals("compoundname")) {
						isName = true;
					} else if (qName.equals("name")) {
						isMemberName = true;
					} else if (qName.equals("type")) {
						isType = true;
					} else if (qName.equals("definition")) {
						isDefinition = true;
					} else if (qName.equals("argsstring")) {
						isArgsstring = true;
					} else if (qName.equals("location")) {
						location = member.getLocation();
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
					} else if (qName.equals("referencedby")) {
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
					} else if (qName.equals("references")) {
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
					tagValue = xmlStreamReader.getText();
					if (isName == true) {
						compound.setName(tagValue);
						isName = false;
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
						member.setType(tagValue);
						isType = false;
					} else if (isDefinition == true) {
						member.setDefinition(tagValue);
						isDefinition = false;
					} else if (isArgsstring == true) {
						member.setArgsstring(tagValue);
						isArgsstring = false;
					}

					break;
				case XMLStreamConstants.END_ELEMENT:
					qName = xmlStreamReader.getLocalName();
					if (qName.equalsIgnoreCase("memberdef")) {
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

	}
}
