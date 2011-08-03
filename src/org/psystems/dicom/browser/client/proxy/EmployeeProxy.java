package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

public class EmployeeProxy implements Serializable {

	private static final long serialVersionUID = -5420171826629626084L;

	private String employeeType;// Тип сотрудника. возможные варианты:
								// DOCTOR|OPERATOR
	private String employeeName;// Имя
	private String employeeCode;// Код, таб.номер

	public static String TYPE_DOCTOR = "DOCTOR";
	public static String TYPE_OPERATOR = "OPERATOR";

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	@Override
	public String toString() {
		return "EmployeeProxy [employeeCode=" + employeeCode
				+ ", employeeName=" + employeeName + ", employeeType="
				+ employeeType + "]";
	}
	
	

}
