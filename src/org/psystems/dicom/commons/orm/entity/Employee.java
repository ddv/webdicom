package org.psystems.dicom.commons.orm.entity;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Сотрудник
 * 
 * @author dima_d
 */
public class Employee implements Serializable {

	private static final long serialVersionUID = 5003881435162510751L;
	
	private String employeeType;// Тип сотрудника. возможные варианты: DOCTOR|OPERATOR
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

	/**
	 * Проверка всех полей.
	 */
	public void chechEntity() {
		if (employeeType == null)
			throw new IllegalArgumentException(
					"Employee type argument could not be null.");
		if (!employeeType.equals(TYPE_DOCTOR)
				&& !employeeType.equals(TYPE_OPERATOR))
			throw new IllegalArgumentException(
					"Field employeeType wrong format  [" + employeeType + "]  argument must be " + TYPE_DOCTOR + " or "
							+ TYPE_OPERATOR );
	}
	
	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null && !(obj instanceof Employee))
//			return false;
//		Employee emp = (Employee) obj;
//
//		if (getEmployeeCode() == null && emp.getEmployeeCode() != null)
//			return false;
//		if (!getEmployeeCode().equals(emp.getEmployeeCode()))
//			return false;
//		
//		
//
//		return true;
//	}
	

	@Override
	public String toString() {
		return "Employee [employeeCode=" + employeeCode + ", employeeName="
				+ employeeName + ", employeeType=" + employeeType + "]";
	}

}
