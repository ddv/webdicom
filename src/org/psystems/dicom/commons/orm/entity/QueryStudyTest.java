package org.psystems.dicom.commons.orm.entity;

import junit.framework.TestCase;

public class QueryStudyTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetStudyModality() {
		QueryStudy query = new QueryStudy();
		query.setStudyModality("CR");
		query.chechEntity();
		query.setStudyModality("CC");
		try {
			query.chechEntity();
			fail("Wrong field type");
		} catch (IllegalArgumentException ex) {

		}
	}

	public void testSetBeginStudyDate() {
		fail("Not yet implemented");
	}

	public void testSetEndStudyDate() {
		fail("Not yet implemented");
	}

	public void testSetPatientSex() {
		fail("Not yet implemented");
	}

	public void testSetPatientBirthDate() {
		fail("Not yet implemented");
	}

}
