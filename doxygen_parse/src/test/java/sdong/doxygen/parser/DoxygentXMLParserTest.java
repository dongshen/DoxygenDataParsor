package sdong.doxygen.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sdong.common.exception.SdongException;
import sdong.doxygen.bean.DoxygenCompound;

public class DoxygentXMLParserTest {

	private static final Logger logger = LoggerFactory.getLogger(DoxygentXMLParserTest.class);

	@Test
	public void testParseDoxygenXmlIndex() {
		String fileName = "input/doxygen/call_graph/xml/index.xml";
		try {
			ConcurrentHashMap<String, DoxygenCompound> compoundMap = DoxygentXMLParser.parseDoxygenXmlIndex(fileName);
			logger.info("compound size=" + compoundMap.size());
			assertEquals(41, compoundMap.size());
			
			//verify
			assertEquals("testcases.h",compoundMap.get("testcases_8h").getName());
			assertEquals(76,compoundMap.get("testcases_8h").getMembers().size());
			
		} catch (SdongException e) {
			e.printStackTrace();
			fail("Should Not get exception");
		}

	}

}
