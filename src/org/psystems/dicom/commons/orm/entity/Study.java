/*
    WEB-DICOM - preserving and providing information to the DICOM devices
	
    Copyright (C) 2009-2010 psystems.org
    Copyright (C) 2009-2010 Dmitry Derenok 

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    Russian translation <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
     
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
 */

package org.psystems.dicom.commons.orm.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.psystems.dicom.commons.orm.ORMUtil;

public class Study implements Serializable {

	private static final long serialVersionUID = -3660580692585603011L;

	private Long id; // Внутренний ID
	private String patientId; // ID пациента (0016,0032) LO 'Patient ID' =
	// LAB_ID64936
	private String patientName; // ФИО пациента (0016,0016) PN 'Patient's Name'
	// = Ги****тди***ва И Ф
	private String patientShortName; // КБП пациента

	private String patientSex; // Пол пациента (0016,0064) CS "Patient's Sex" =
	// M/F
	private String patientBirthDate; // Дата рождения пациента (0016,0048) DA
	// "Patient's Birth Date". формат "yyyy-mm-dd"
	private String studyId; // ID исследования (0032,0016) SH 'Study ID' = 89729
	private String studyInstanceUID; // UID исследования (0032,0013) UI 'Study
	// Instance UID' =
	// 1.2.826.0.1.3680043.2.634.30.1.89729.20100305113905
	private String studyDate; // Дата исследования (0008,0032) DA 'Study Date'.
								// формат "yyyy-mm-dd"
	private String studyDoctor; // Врач исследования (0008,0144) PN
	// "Referring Physician's Name" = Куницкий В.Н
	private String studyOperator; // Оператор исследования (0008,4208) PN
	// "Operators' Name" = Гаврилова Н.Г.
	private String studyViewprotocolDate;// Дата описания исследования. формат
											// "yyyy-mm-dd"
	private String studyType;// Вид исследования.
	private String studyDescription;// Описание исследования.
	private String studyModality;// модальность (0008,0060).
	private String studyViewprotocol;// Протокол осмотра
	private String studyResult;// Результат исследования.
	private String ManufacturerModelUID; // UID Аппарата (0002,0003) UI
	// "Media Storage SOP Instance UID"
	// =
	// 1.2.826.0.1.3680043.2.634.0.18669.2010225.1416.1
	private String ManufacturerModelName; // Имя Аппарата (0008,4240) LO
	// "Manufacturer's Model Name" =
	// КРТ-Электрон
	private String studyUrl; // URL для открытия в обозревателе
	private Long[] dcmFilesId; // Связанные DCM-файлы
	private String dateFormat = "yyyy-MM-dd";
	// private String userDateTimeFormat = "dd-MM-yyyy HH:mm:ss";
	private String sqlTimestampFormat = "yyyy-MM-dd HH:mm:ss";

	private String studyDateTimeModify;// Дата модификации исследования. формат
										// "yyyy-mm-dd hh:mm:ss"
	private String studyDateTimeRemoved;// Дата удаления исследования. формат
										// "yyyy-mm-dd hh:mm:ss"

	private Direction direction;// Id связанного направления

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
		return patientShortName;
	}

	public void setPatientShortName(String patientShortName) {
		this.patientShortName = patientShortName;
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

	public void setPatientBirthDate(String patientBirthDate) {
		this.patientBirthDate = patientBirthDate;
	}

	public String getStudyId() {
		return studyId;
	}

	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}

	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}

	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}

	public String getStudyDate() {
		return studyDate;
	}

	public void setStudyDate(String studyDate) {
		this.studyDate = studyDate;
	}

	public String getStudyDoctor() {
		return studyDoctor;
	}

	public void setStudyDoctor(String studyDoctor) {
		this.studyDoctor = studyDoctor;
	}

	public String getStudyOperator() {
		return studyOperator;
	}

	public void setStudyOperator(String studyOperator) {
		this.studyOperator = studyOperator;
	}

	public String getStudyViewprotocolDate() {
		return studyViewprotocolDate;
	}

	public void setStudyViewprotocolDate(String studyViewprotocolDate) {
		this.studyViewprotocolDate = studyViewprotocolDate;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public String getStudyModality() {
		return studyModality;
	}

	public void setStudyModality(String studyModality) {
		this.studyModality = studyModality;
	}

	public String getStudyDescription() {
		return studyDescription;
	}

	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String getStudyViewprotocol() {
		return studyViewprotocol;
	}

	public void setStudyViewprotocol(String studyViewprotocol) {
		this.studyViewprotocol = studyViewprotocol;
	}

	public String getStudyResult() {
		return studyResult;
	}

	public void setStudyResult(String studyResult) {
		this.studyResult = studyResult;
	}

	public String getManufacturerModelUID() {
		return ManufacturerModelUID;
	}

	public void setManufacturerModelUID(String manufacturerModelUID) {
		ManufacturerModelUID = manufacturerModelUID;
	}

	public String getManufacturerModelName() {
		return ManufacturerModelName;
	}

	public void setManufacturerModelName(String manufacturerModelName) {
		ManufacturerModelName = manufacturerModelName;
	}

	public String getStudyUrl() {
		return studyUrl;
	}

	public void setStudyUrl(String studyUrl) {
		this.studyUrl = studyUrl;
	}

	public Long[] getDcmFilesId() {
		return dcmFilesId;
	}

	public void setDcmFilesId(Long[] dcmFiles) {
		this.dcmFilesId = dcmFiles;
	}

	public String getStudyDateTimeModify() {
		return studyDateTimeModify;
	}

	public void setStudyDateTimeModify(String studyDateModidy) {
		this.studyDateTimeModify = studyDateModidy;
	}

	public String getStudyDateTimeRemoved() {
		return studyDateTimeRemoved;
	}

	public void setStudyDateTimeRemoved(String studyDateRemoved) {
		this.studyDateTimeRemoved = studyDateRemoved;
	}


	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
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
		try {
			if (studyDate != null) {
				ORMUtil.dateSQLToUtilDate(studyDate);
			}
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(
					"Study Date field wrong format: " + ex.getMessage(), ex);
		}
		try {
			if (studyViewprotocolDate != null) {
				ORMUtil.dateSQLToUtilDate(studyViewprotocolDate);
			}
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException(
					"Study View Protocol Date field wrong format: "
							+ ex.getMessage(), ex);
		}

	}

	@Override
	public String toString() {
		return "Study [id=" + id + ", studyId=" + studyId + ", direction="
				+ direction + ", ManufacturerModelName="
				+ ManufacturerModelName + ", ManufacturerModelUID="
				+ ManufacturerModelUID + ", dateFormat=" + dateFormat
				+ ", dcmFilesId=" + Arrays.toString(dcmFilesId)
				+ ", patientBirthDate=" + patientBirthDate + ", patientId="
				+ patientId + ", patientName=" + patientName + ", patientSex="
				+ patientSex + ", patientShortName=" + patientShortName
				+ ", sqlTimestampFormat=" + sqlTimestampFormat + ", studyDate="
				+ studyDate + ", studyDateModify=" + studyDateTimeModify
				+ ", studyDateRemoved=" + studyDateTimeRemoved
				+ ", studyDescription=" + studyDescription + ", studyDoctor="
				+ studyDoctor + ", studyInstanceUID=" + studyInstanceUID
				+ ", studyModality=" + studyModality + ", studyOperator="
				+ studyOperator + ", studyResult=" + studyResult
				+ ", studyType=" + studyType + ", studyUrl=" + studyUrl
				+ ", studyViewprotocol=" + studyViewprotocol
				+ ", studyViewprotocolDate=" + studyViewprotocolDate + "]";
	}

}