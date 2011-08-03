package org.psystems.dicom.commons.solr.entity;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Словарь диагнозов
 * 
 * @author dima_d
 */
public class Diagnosis {

	@Field
	public
	String id;
	
	@Field
	public String dicName = "diagnosis";// Имя словаря

	@Field
	public String diagnosisCode;// Код (по МКБ)

	@Field
	public String diagnosisDescription;// Описание

	
	
	public String getId() {
		return id;
	}

	@Field
	public void setId(String id) {
		this.id = id;
	}

	public String getDiagnosisCode() {
		return diagnosisCode;
	}

	@Field
	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}

	public String getDiagnosisDescription() {
		return diagnosisDescription;
	}

	@Field
	public void setDiagnosisDescription(String diagnosisDescription) {
		this.diagnosisDescription = diagnosisDescription;
	}

	public String getDicName() {
		return dicName;
	}

	@Field
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	@Override
	public String toString() {
		return "Diagnosis [diagnosisCode=" + diagnosisCode
				+ ", diagnosisDescription=" + diagnosisDescription
				+ ", dicName=" + dicName + ", id=" + id + "]";
	}

	
	
}
