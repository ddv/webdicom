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
    
    The Original Code is part of WEB-DICOM, an implementation hosted at 
    <http://code.google.com/p/web-dicom/>
    
    In the project WEB-DICOM used the library open source project dcm4che
    The Original Code is part of dcm4che, an implementation of DICOM(TM) in
    Java(TM), hosted at http://sourceforge.net/projects/dcm4che.
    
    =======================================================================
    
    WEB-DICOM - Сохранение и предоставление информации с DICOM устройств

    Copyright (C) 2009-2010 psystems.org 
    Copyright (C) 2009-2010 Dmitry Derenok 

    Это программа является свободным программным обеспечением. Вы можете 
    распространять и/или модифицировать её согласно условиям Стандартной 
    Общественной Лицензии GNU, опубликованной Фондом Свободного Программного 
    Обеспечения, версии 3 или, по Вашему желанию, любой более поздней версии. 
    Эта программа распространяется в надежде, что она будет полезной, но
    БЕЗ ВСЯКИХ ГАРАНТИЙ, в том числе подразумеваемых гарантий ТОВАРНОГО СОСТОЯНИЯ ПРИ 
    ПРОДАЖЕ и ГОДНОСТИ ДЛЯ ОПРЕДЕЛЁННОГО ПРИМЕНЕНИЯ. Смотрите Стандартную 
    Общественную Лицензию GNU для получения дополнительной информации. 
    Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе 
    с программой. В случае её отсутствия, посмотрите <http://www.gnu.org/licenses/>
    Русский перевод <http://code.google.com/p/gpl3rus/wiki/LatestRelease>
    
    Оригинальный исходный код WEB-DICOM можно получить на
    <http://code.google.com/p/web-dicom/>
    
    В проекте WEB-DICOM использованы библиотеки открытого проекта dcm4che/
    Оригинальный исходный код проекта dcm4che, и его имплементация DICOM(TM) in
    Java(TM), находится здесь http://sourceforge.net/projects/dcm4che.
    
    
 */
package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;
import java.util.ArrayList;

//import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author dima_d
 * 
 */
public class StudyProxy implements Serializable {

    private static final long serialVersionUID = -7977302129675187420L;

    private long id; // ID
    private String studyModality; // Modality
    private String studyInstanceUID; // UID
    private String patientName; // ФИО пациента
    private String patientSex; // Пол пациента
    private String patientId; // ID пациента
    private String patientBirthDate; // Дата рождения пациента
    private String studyId; // ID исследования
    private String studyDate; // Дата исследования
    private String studyDoctor; // Врач исследования
    private String studyOperator; // Оператор исследования
    private String studyViewprotocolDate;// Дата описания исследования.
    private String studyType;// Вид исследования.
    private String studyDescription;// Описание исследования.
    private String studyViewprotocol;// Протокол осмотра
    private String studyResult;// Результат исследования.
    private String ManufacturerModelName; // Аппарат
    private String studyDateTimeModify; // Дата модификации
    private String studyDateTimeRemoved; // Дата удаления исследования
    private DirectionProxy direction; // Направление

    // Файлы связанные с исследованием
    private ArrayList<DcmFileProxy> files = new ArrayList<DcmFileProxy>();

    public long getId() {
	return id;
    }

    public String getPatientName() {
	return patientName;
    }

    public String getPatientBirthDate() {
	return patientBirthDate;
    }

    public String getPatientSex() {
	return patientSex;
    }

    public String getPatientId() {
	return patientId;
    }

    public String getStudyId() {
	return studyId;
    }

    public String getStudyDate() {
	return studyDate;
    }

    public String getStudyDoctor() {
	return studyDoctor;
    }

    public String getStudyOperator() {
	return studyOperator;
    }

    public String getStudyDescription() {
	return studyDescription;
    }

    public String getStudyResult() {
	return studyResult;
    }

    public String getStudyViewprotocolDate() {
	return studyViewprotocolDate;
    }

    public String getStudyType() {
	return studyType;
    }

    public String getManufacturerModelName() {
	return ManufacturerModelName;
    }

    public String getStudyViewprotocol() {
	return studyViewprotocol;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

    public ArrayList<DcmFileProxy> getFiles() {
	return files;
    }

    public void setFiles(ArrayList<DcmFileProxy> files) {
	this.files = files;
    }

    public String getStudyInstanceUID() {
	return studyInstanceUID;
    }

    public String getStudyModality() {
	return studyModality;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setStudyModality(String studyModality) {
	this.studyModality = studyModality;
    }

    public void setStudyInstanceUID(String studyInstanceUID) {
	this.studyInstanceUID = studyInstanceUID;
    }

    public void setPatientName(String patientName) {
	this.patientName = patientName;
    }

    public void setPatientSex(String patientSex) {
	this.patientSex = patientSex;
    }

    public void setPatientId(String patientId) {
	this.patientId = patientId;
    }

    public void setPatientBirthDate(String patientBirthDate) {
	this.patientBirthDate = patientBirthDate;
    }

    public void setStudyId(String studyId) {
	this.studyId = studyId;
    }

    public void setStudyDate(String studyDate) {
	this.studyDate = studyDate;
    }

    public void setStudyDoctor(String studyDoctor) {
	this.studyDoctor = studyDoctor;
    }

    public void setStudyOperator(String studyOperator) {
	this.studyOperator = studyOperator;
    }

    public void setStudyViewprotocolDate(String studyViewprotocolDate) {
	this.studyViewprotocolDate = studyViewprotocolDate;
    }

    public void setStudyType(String studyType) {
	this.studyType = studyType;
    }

    public void setStudyDescription(String studyDescription) {
	this.studyDescription = studyDescription;
    }

    public void setStudyViewprotocol(String studyViewprotocol) {
	this.studyViewprotocol = studyViewprotocol;
    }

    public void setStudyResult(String studyResult) {
	this.studyResult = studyResult;
    }

    public void setManufacturerModelName(String manufacturerModelName) {
	ManufacturerModelName = manufacturerModelName;
    }

    public String getStudyDateTimeModify() {
	return studyDateTimeModify;
    }

    public void setStudyDateTimeModify(String studyDateModify) {
	this.studyDateTimeModify = studyDateModify;
    }

    public String getStudyDateTimeRemoved() {
	return studyDateTimeRemoved;
    }

    public void setStudyDateTimeRemoved(String studyDateRemoved) {
	this.studyDateTimeRemoved = studyDateRemoved;
    }

    public DirectionProxy getDirection() {
	return direction;
    }

    public void setDirection(DirectionProxy direction) {
	this.direction = direction;
    }

    @Override
    public String toString() {
	return "StudyProxy [ManufacturerModelName=" + ManufacturerModelName + ", direction=" + direction + ", files="
		+ files + ", id=" + id + ", patientBirthDate=" + patientBirthDate + ", patientId=" + patientId
		+ ", patientName=" + patientName + ", patientSex=" + patientSex + ", studyDate=" + studyDate
		+ ", studyDateTimeModify=" + studyDateTimeModify + ", studyDateTimeRemoved=" + studyDateTimeRemoved
		+ ", studyDescription=" + studyDescription + ", studyDoctor=" + studyDoctor + ", studyId=" + studyId
		+ ", studyInstanceUID=" + studyInstanceUID + ", studyModality=" + studyModality + ", studyOperator="
		+ studyOperator + ", studyResult=" + studyResult + ", studyType=" + studyType + ", studyViewprotocol="
		+ studyViewprotocol + ", studyViewprotocolDate=" + studyViewprotocolDate + "]";
    }

}
