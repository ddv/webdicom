package org.psystems.dicom.commons.orm.entity;

import junit.framework.TestCase;

public class DiagnosisTest extends TestCase {

	public void testdiagnosisType() {
		Diagnosis dia = new Diagnosis();
		dia.setDiagnosisType(Diagnosis.TYPE_ACCOMPANYING);
		dia.chechEntity();
		dia.setDiagnosisType(Diagnosis.TYPE_MAIN);
		dia.chechEntity();
		dia.setDiagnosisType(Diagnosis.TYPE_INVOLVEMENT);
		dia.chechEntity();
		dia.setDiagnosisType("DDD");
		try {
			dia.chechEntity();
			fail("Wrong field type");
		}catch(IllegalArgumentException ex) {
			
		}
	}

}
