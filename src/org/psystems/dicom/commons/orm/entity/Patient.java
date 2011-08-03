package org.psystems.dicom.commons.orm.entity;

import java.io.Serializable;

import org.psystems.dicom.commons.orm.ORMUtil;

/**
 * Пациент
 * 
 * @author dima_d
 * 
 */
public class Patient implements Serializable {

	private static final long serialVersionUID = 1336950569742992093L;

	private long id; // ID
	private String patientId; // ID пациента
	private String patientName; // ФИО пациента
	private String patientShortName; // КБП пациента (код быстрого поиска)
	private String patientSex; // Пол пациента (M/F)
	private String patientBirthDate; // Дата рождения пациента. формат
										// "yyyy-mm-dd"

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientShortName() {
		return ORMUtil.makeShortName(patientName, patientBirthDate);
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public void setPatientBirthDate(String patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}

	public String getPatientBirthDate() {
		return patientBirthDate;
	}

	/**
	 * Проверка всех полей.
	 */
	public void chechEntity() {
		try {
			if (patientBirthDate != null) {
				ORMUtil.dateSQLToUtilDate(patientBirthDate);
			}
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(
					"Patient field Birth Date wrong format: " + ex.getMessage(),
					ex);
		}

	}

	@Override
	public String toString() {
		return "Patient [id=" + id + ", patientBirthDate=" + patientBirthDate
				+ ", patientId=" + patientId + ", patientName=" + patientName
				+ ", patientSex=" + patientSex + ", patientShortName="
				+ patientShortName + "]";
	}

}
