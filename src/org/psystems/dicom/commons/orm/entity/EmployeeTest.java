package org.psystems.dicom.commons.orm.entity;

import junit.framework.TestCase;

public class EmployeeTest extends TestCase {

	public void testEmployeeType() {
		Employee emp = new Employee();
		emp.setEmployeeType(Employee.TYPE_DOCTOR);
		emp.chechEntity();
		emp.setEmployeeType(Employee.TYPE_OPERATOR);
		emp.chechEntity();
		emp.setEmployeeType("DDD");
		try {
			emp.chechEntity();
			fail("Wrong field type");
		} catch (IllegalArgumentException ex) {

		}
	}

}
