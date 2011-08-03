package org.psystems.dicom.commons.solr.entity;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Словарь сотрудников
 * 
 * @author dima_d
 */
public class Employee {

	@Field
	public String id;

	@Field
	public String dicName = "employee";// Имя словаря

	@Field
	public String employeeType;// Тип сотрудника. возможные варианты:
	// DOCTOR|OPERATOR
	
	@Field
	public String employeeName;// Имя

	@Field
	public String employeeCode;// Код, таб.номер

	public static String TYPE_DOCTOR = "DOCTOR";
	public static String TYPE_OPERATOR = "OPERATOR";

	public String getId() {
		return id;
	}

	@Field
	public void setId(String id) {
		this.id = id;
	}

	public String getDicName() {
		return dicName;
	}

	@Field
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	@Field
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	@Field
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	@Field
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	@Override
	public String toString() {
		return "Employee [dicName=" + dicName + ", employeeCode="
				+ employeeCode + ", employeeName=" + employeeName
				+ ", employeeType=" + employeeType + ", id=" + id + "]";
	}

}
