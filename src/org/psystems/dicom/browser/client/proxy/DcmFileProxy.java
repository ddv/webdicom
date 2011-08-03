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
import java.util.Date;

/**
 * @author dima_d
 * 
 */
public class DcmFileProxy implements Serializable {

	private static final long serialVersionUID = -7977302129675187420L;

	private long id; // ID
	private long idStudy; // FID
	private String type; // Тип DCM файла (снимок, репорт,...)
	private String fileName; // Имя DCM файла
	private long fileSize; // Имя DCM файла
	private long imageSize; // Размер картинки
	private int imageWidth; //
	private int imageHeight; //
	private String mimeType;
	private long encapsulatedDocSize;// Размер вложенного документа
	private Date dateRemoved; // Дата удаления фала

	/**
	 * Инициализация класса
	 * 
	 * @param id
	 * @param idStudy
	 * @param type
	 * @param fileName
	 * @param fileSize
	 * @param imageSize
	 * @param imageWidth
	 * @param imageHeight
	 */
//	public void init(long id, long idStudy, String type, String mimeType,
//			long encapsulatedDocSize, String fileName, long fileSize,
//			long imageSize, int imageWidth, int imageHeight) {
//		this.id = id;
//		this.idStudy = idStudy;
//		this.type = type;
//		this.mimeType = mimeType;
//		this.encapsulatedDocSize = encapsulatedDocSize;
//		this.fileName = fileName;
//		this.fileSize = fileSize;
//		this.imageSize = imageSize;
//		this.imageWidth = imageWidth;
//		this.imageHeight = imageHeight;
//	}

	public boolean haveImage() {
		return true ? (imageSize > 0) : false;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public long getIdStudy() {
		return idStudy;
	}

	public String getType() {
		return type;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public long getImageSize() {
		return imageSize;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public long getEncapsulatedDocSize() {
		return encapsulatedDocSize;
	}

	public void setEncapsulatedDocSize(long encapsulatedDocSize) {
		this.encapsulatedDocSize = encapsulatedDocSize;
	}


	public void setId(long id) {
		this.id = id;
	}

	public void setIdStudy(long idStudy) {
		this.idStudy = idStudy;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setImageSize(long imageSize) {
		this.imageSize = imageSize;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public Date getDateRemoved() {
		return dateRemoved;
	}

	public void setDateRemoved(Date dateRemoved) {
		this.dateRemoved = dateRemoved;
	}
	
	

}
