package org.psystems.dicom.commons.orm;

import junit.framework.TestCase;

public class ORMUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testMakeShortName() {
		assertEquals(ORMUtil.makeShortName("Иванов Иван Иванович", "1974-03-01"), "ИВАИИ74");
		assertEquals(ORMUtil.makeShortName("Иванов И И", "1974-03-01"), "ИВАИИ74");
	}

	public void testUtilDateToSQLDateString() {
		fail("Not yet implemented");
	}

	public void testDateSQLToUtilDate() {
		fail("Not yet implemented");
	}

	public void testUtilDateTimeToSQLDateTimeString() {
		fail("Not yet implemented");
	}

	public void testDateTimeSQLToUtilDate() {
		fail("Not yet implemented");
	}

	

}
