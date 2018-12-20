package sdong.doxygen.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sdong.common.exception.SdongException;
import sdong.doxygen.bean.DoxygenCompound;
import sdong.doxygen.bean.DoxygenLocation;
import sdong.doxygen.bean.DoxygenMember;
import sdong.doxygen.bean.DoxygenParam;
import sdong.doxygen.bean.DoxygenReference;

public class DoxygentXMLParserTest {

	private static final Logger logger = LoggerFactory.getLogger(DoxygentXMLParserTest.class);

	@Test
	public void testParseDoxygenXmlIndex() {
		String fileName = "input/doxygen/call_graph/xml/index.xml";
		try {
			ConcurrentHashMap<String, DoxygenCompound> compoundMap = DoxygentXMLParser.parseDoxygenXmlIndex(fileName);
			logger.info("compound size=" + compoundMap.size());
			assertEquals(41, compoundMap.size());

			// verify
			String refid = "testcases_8h";
			assertEquals("testcases.h", compoundMap.get(refid).getName());
			assertEquals(76, compoundMap.get(refid).getMembersFunction().size());

			refid = "CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c";
			assertEquals("CWE15_External_Control_of_System_or_Configuration_Setting__w32_22b.c",
					compoundMap.get(refid).getName());
			assertEquals(3, compoundMap.get(refid).getMembersFunction().size());
			assertEquals(3, compoundMap.get(refid).getMembersVariable().size());
			assertEquals(2, compoundMap.get(refid).getMembersDefine().size());

		} catch (SdongException e) {
			e.printStackTrace();
			fail("Should Not get exception");
		}

	}

	@Test
	public void testParseDoxygenXmlFile() {
		String fileName = "input/doxygen/call_graph/xml/CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c.xml";
		try {
			DoxygenCompound compound = DoxygentXMLParser.parseDoxygenXmlFile(fileName);
			verifyCompound(compound);

		} catch (SdongException e) {
			e.printStackTrace();
			fail("Should Not get exception");
		}

	}

	private void verifyCompound(DoxygenCompound compound) {
		// compound
		assertEquals("CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c",
				compound.getRefid());
		assertEquals("CWE15_External_Control_of_System_or_Configuration_Setting__w32_22b.c", compound.getName());
		assertEquals("file", compound.getKind());
		assertEquals("C++", compound.getLanguage());

		assertEquals(3, compound.getMembersFunction().size());
		assertEquals(3, compound.getMembersVariable().size());
		assertEquals(2, compound.getMembersDefine().size());
		// location
		DoxygenLocation location = compound.getLocation();
		assertEquals(
				"example/CWE/CWE15_External_Control_of_System_or_Configuration_Setting/CWE15_External_Control_of_System_or_Configuration_Setting__w32_22b.c",
				location.getFile());

		// verify member function
		String refid = "CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c_1a17e4a785bc89dbd83816bb91d3e634fd";
		DoxygenMember member = compound.getMembersFunction().get(refid);
		assertEquals("function", member.getKind());
		assertEquals("char *", member.getType());
		assertEquals("CWE15_External_Control_of_System_or_Configuration_Setting__w32_22_badSource", member.getName());
		assertEquals("(char *data)", member.getArgsstring());
		assertEquals("char* CWE15_External_Control_of_System_or_Configuration_Setting__w32_22_badSource",
				member.getDefinition());

		assertEquals("public", member.getPort());
		assertEquals(false, member.isStatic());
		assertEquals(false, member.isConst());
		assertEquals(false, member.isExplicit());
		assertEquals(false, member.isInline());
		assertEquals("non-virtual", member.getVirt());

		// function -- parameter
		assertEquals(1, member.getParams().size());
		DoxygenParam param = member.getParams().get(0);
		assertEquals("char *", param.getType());
		assertEquals("data", param.getDeclname());

		// function -- verify location
		location = member.getLocation();
		assertEquals(
				"example/CWE/CWE15_External_Control_of_System_or_Configuration_Setting/CWE15_External_Control_of_System_or_Configuration_Setting__w32_22b.c",
				location.getFile());
		assertEquals(30, location.getLine());
		assertEquals(1, location.getColumn());
		assertEquals(
				"example/CWE/CWE15_External_Control_of_System_or_Configuration_Setting/CWE15_External_Control_of_System_or_Configuration_Setting__w32_22b.c",
				location.getBodyfile());
		assertEquals(30, location.getBodystart());
		assertEquals(105, location.getBodyend());

		// function -- reference
		assertEquals(3, member.getReference().size());
		DoxygenReference reference = member.getReference().get(
				"CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c_1a4fba0963c20988d1f1a45afb1c636e44");
		assertEquals("LISTEN_PORT", reference.getName());
		assertEquals(
				"CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c_1a4fba0963c20988d1f1a45afb1c636e44",
				reference.getRefid());
		assertEquals("CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c",
				reference.getCompoundref());
		assertEquals(22, reference.getStartline());

		// function -- referencedby
		assertEquals(1, member.getReferencedby().size());
		DoxygenReference referencedby = member.getReferencedby().get("testcases_8h_1a5c68fe5c6a56d9f243f06fe949cfe485");
		assertEquals("CWE15_External_Control_of_System_or_Configuration_Setting__w32_22_bad", referencedby.getName());
		assertEquals("testcases_8h_1a5c68fe5c6a56d9f243f06fe949cfe485", referencedby.getRefid());
		assertEquals("CWE15__External__Control__of__System__or__Configuration__Setting____w32__22a_8c",
				referencedby.getCompoundref());
		assertEquals(28, referencedby.getStartline());
		assertEquals(41, referencedby.getEndline());

		// verify member define
		refid = "CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c_1a4fba0963c20988d1f1a45afb1c636e44";
		member = compound.getMembersDefine().get(refid);
		assertEquals("define", member.getKind());
		assertEquals("LISTEN_PORT", member.getName());

		assertEquals("public", member.getPort());
		assertEquals(false, member.isStatic());

		// define -- location
		location = member.getLocation();
		assertEquals(
				"example/CWE/CWE15_External_Control_of_System_or_Configuration_Setting/CWE15_External_Control_of_System_or_Configuration_Setting__w32_22b.c",
				location.getFile());
		assertEquals(22, location.getLine());
		assertEquals(9, location.getColumn());
		assertEquals(
				"example/CWE/CWE15_External_Control_of_System_or_Configuration_Setting/CWE15_External_Control_of_System_or_Configuration_Setting__w32_22b.c",
				location.getBodyfile());
		assertEquals(22, location.getBodystart());
		assertEquals(-1, location.getBodyend());

		// verify member variable
		refid = "CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c_1a9ecde5e997da0ec1b8bc53de67a85d0c";
		member = compound.getMembersVariable().get(refid);
		assertEquals("variable", member.getKind());
		assertEquals("int", member.getType());
		assertEquals("CWE15_External_Control_of_System_or_Configuration_Setting__w32_22_goodG2B1Global",
				member.getName());
		assertEquals("", member.getArgsstring());
		assertEquals("int CWE15_External_Control_of_System_or_Configuration_Setting__w32_22_goodG2B1Global",
				member.getDefinition());

		assertEquals("public", member.getPort());
		assertEquals(false, member.isStatic());
		assertEquals(false, member.isMutable());

		// variable -- verify location
		location = member.getLocation();
		assertEquals(
				"example/CWE/CWE15_External_Control_of_System_or_Configuration_Setting/CWE15_External_Control_of_System_or_Configuration_Setting__w32_22b.c",
				location.getFile());
		assertEquals(112, location.getLine());
		assertEquals(1, location.getColumn());
		assertEquals(
				"example/CWE/CWE15_External_Control_of_System_or_Configuration_Setting/CWE15_External_Control_of_System_or_Configuration_Setting__w32_22a.c",
				location.getBodyfile());
		assertEquals(48, location.getBodystart());
		assertEquals(-1, location.getBodyend());

		// variable -- reference
		assertEquals(0, member.getReference().size());

		// variable -- referencedby
		assertEquals(2, member.getReferencedby().size());
		referencedby = member.getReferencedby().get(
				"CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c_1a95b1967d57f6342a39262d7e22a9a9d2");
		assertEquals("CWE15_External_Control_of_System_or_Configuration_Setting__w32_22_goodG2B1Source",
				referencedby.getName());
		assertEquals(
				"CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c_1a95b1967d57f6342a39262d7e22a9a9d2",
				referencedby.getRefid());
		assertEquals("CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c",
				referencedby.getCompoundref());
		assertEquals(116, referencedby.getStartline());
		assertEquals(129, referencedby.getEndline());
	}

	@Test
	public void testGetProjectCompound() {
		String indexFileName = "input/doxygen/call_graph/xml/index.xml";
		String refid = "CWE15__External__Control__of__System__or__Configuration__Setting____w32__22b_8c";
		ConcurrentHashMap<String, DoxygenCompound> compoundMap = DoxygentXMLParser.getProjectCompound(indexFileName);
		DoxygenCompound compound = compoundMap.get(refid);
		verifyCompound(compound);

	}
}
