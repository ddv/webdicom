package org.psystems.dicom.commons.solr.entity;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Словарь Пациентов
 * 
 * @author dima_d
 * 
 */
public class Patient {

	@Field
	public String id;

	@Field
	public String dicName = "patient";// Имя словаря

	@Field
	public String patientId; // ID пациента

	@Field
	public String patientName; // ФИО пациента

	@Field
	public String patientShortName; // КБП пациента (код быстрого поиска)

	@Field
	public String patientSex; // Пол пациента (M/F)

	@Field
	public Date patientBirthDate; // Дата рождения пациента

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

	public String getPatientId() {
		return patientId;
	}

	@Field
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	@Field
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientShortName() {
		return patientShortName;
	}

	@Field
	public void setPatientShortName(String patientShortName) {
		this.patientShortName = patientShortName;
	}

	public String getPatientSex() {
		return patientSex;
	}

	@Field
	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public Date getPatientBirthDate() {
		return patientBirthDate;
	}

	@Field
	public void setPatientBirthDate(Date patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}

	@Override
	public String toString() {
		return "Patient [dicName=" + dicName + ", id=" + id
				+ ", patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + ", patientShortName=" + patientShortName + "]";
	}

}
