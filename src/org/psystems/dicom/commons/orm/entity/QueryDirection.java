package org.psystems.dicom.commons.orm.entity;

import org.psystems.dicom.commons.orm.ORMUtil;

/**
 * Запрос направлений
 * 
 * @author dima_d
 * 
 */
public class QueryDirection {

	private static final long serialVersionUID = -2840335603832244555L;
	private Long id; // Внутренний ID
	private String directionId; // штрих код
	private String dateDirection;// Дата направления. формат "yyyy-mm-dd"

	//
	
	private String ManufacturerDevice;// аппарат
	private String dateTimePlannedBegin;// Планируемая дата (начало интервала). Должны быть заданы оба!
	private String dateTimePlannedEnd;// Планируемая дата (конец интервала). Должны быть заданы оба!
	private String directionLocation;// Кабинет

	private String doctorDirectName;// Направивший врач Имя
	private String doctorDirectCode;// Направивший врач Код

	private String doctorPerformedName;// Выполнивший врач Имя
	private String doctorPerformedCode;// Выполнивший врач Код

	//

	private String patientId; // ID пациента
	private String patientName; // ФИО пациента
	private String patientSex; // Пол пациента (M/F)
	private String patientBirthDate; // Дата рождения пациента. формат
										// "yyyy-mm-dd"
	
	private String patientShortName; // КБП пациента (код быстрого поиска)

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDirectionId() {
		return directionId;
	}

	public void setDirectionId(String directionId) {
		this.directionId = directionId;
	}

	public String getDateDirection() {
		return dateDirection;
	}

	/**
	 * @param dateDirection
	 *            Формат SQL Date - "гггг.дд.мм"
	 */
	public void setDateDirection(String dateDirection) {
		this.dateDirection = dateDirection;
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

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public String getPatientBirthDate() {
		return patientBirthDate;
	}

	/**
	 * @param patientBirthDate
	 *            Формат SQL Date - "гггг.дд.мм"
	 */
	public void setPatientBirthDate(String patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}

	public String getManufacturerDevice() {
		return ManufacturerDevice;
	}

	public void setManufacturerDevice(String manufacturerDevice) {
		ManufacturerDevice = manufacturerDevice;
	}


	public String getDateTimePlannedBegin() {
		return dateTimePlannedBegin;
	}

	public void setDateTimePlannedBegin(String dateTimePlannedBegin) {
		this.dateTimePlannedBegin = dateTimePlannedBegin;
	}

	public String getDateTimePlannedEnd() {
		return dateTimePlannedEnd;
	}

	public void setDateTimePlannedEnd(String dateTimePlannedEnd) {
		this.dateTimePlannedEnd = dateTimePlannedEnd;
	}

	public String getDirectionLocation() {
		return directionLocation;
	}

	public void setDirectionLocation(String directionLocation) {
		this.directionLocation = directionLocation;
	}

	public String getDoctorDirectName() {
		return doctorDirectName;
	}

	public void setDoctorDirectName(String doctorDirectName) {
		this.doctorDirectName = doctorDirectName;
	}

	public String getDoctorDirectCode() {
		return doctorDirectCode;
	}

	public void setDoctorDirectCode(String doctorDirectCode) {
		this.doctorDirectCode = doctorDirectCode;
	}

	public String getDoctorPerformedName() {
		return doctorPerformedName;
	}

	public void setDoctorPerformedName(String doctorPerformedName) {
		this.doctorPerformedName = doctorPerformedName;
	}

	public String getDoctorPerformedCode() {
		return doctorPerformedCode;
	}

	public void setDoctorPerformedCode(String doctorPerformedCode) {
		this.doctorPerformedCode = doctorPerformedCode;
	}
	

	public String getPatientShortName() {
		return patientShortName;
	}

	public void setPatientShortName(String patientShortName) {
		this.patientShortName = patientShortName;
	}

	/**
	 * Проверка всех полей.
	 */
	public void chechEntity() {
		String field = null;
		try {
			if (dateDirection != null) {
				field = "dateDirection";
				ORMUtil.dateSQLToUtilDate(dateDirection);
			}
			if (patientBirthDate != null) {
				field = "patientBirthDate";
				ORMUtil.dateSQLToUtilDate(patientBirthDate);
			}
			if (dateTimePlannedBegin != null) {
				field = "dateTimePlannedBegin";
				ORMUtil.dateTimeSQLToUtilDate(dateTimePlannedBegin);
			}
			if (dateTimePlannedBegin != null) {
				field = "dateTimePlannedEnd";
				ORMUtil.dateTimeSQLToUtilDate(dateTimePlannedEnd);
			}
			
			if (dateTimePlannedBegin != null && dateTimePlannedEnd != null
					&& ORMUtil.dateTimeSQLToUtilDate(dateTimePlannedBegin).getTime() >
				ORMUtil.dateTimeSQLToUtilDate(dateTimePlannedEnd).getTime()) {
				field = "dateTimePlannedBegin and dateTimePlannedEnd";
				throw new IllegalArgumentException("wrong data range! " +dateTimePlannedBegin +" > "+
						dateTimePlannedEnd );
			}
			

		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("field " + field + " " + ex.getMessage(), ex);
		}
	}

	@Override
	public String toString() {
		return "QueryDirection [ManufacturerDevice=" + ManufacturerDevice
				+ ", dateDirection=" + dateDirection
				+ ", dateTimePlannedBegin=" + dateTimePlannedBegin
				+ ", dateTimePlannedEnd=" + dateTimePlannedEnd
				+ ", directionId=" + directionId + ", directionLocation="
				+ directionLocation + ", doctorDirectCode=" + doctorDirectCode
				+ ", doctorDirectName=" + doctorDirectName
				+ ", doctorPerformedCode=" + doctorPerformedCode
				+ ", doctorPerformedName=" + doctorPerformedName + ", id=" + id
				+ ", patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + ", patientShortName=" + patientShortName + "]";
	}

	

	

}
