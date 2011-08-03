package org.psystems.dicom.commons.orm.entity;

import junit.framework.TestCase;

public class ManufacturerDeviceTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetManufacturerModelType() {
		ManufacturerDevice dev = new ManufacturerDevice();
		dev.setModality("CR");
		dev.chechEntity();
		dev.setModality("CC");
		try {
			dev.chechEntity();
			fail("Wrong field type");
		} catch (IllegalArgumentException ex) {

		}
	}

}
