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

/**
 * @author dima_d
 * 
 */
public class DcmTagProxy implements Serializable {

	private static final long serialVersionUID = -7977302129675187320L;

	private long idDcm; // ID файла в БД
	private Integer idTag; // ID
	private String majorStr = null;
	private String minorStr = null;
	private short major;
	private short minor;
	private String tagType; // Тип
	private String tagName; // Имя
	private String tagValue; // Значение

	/**
	 * Инициализация класса
	 * 
	 * @param idDcm
	 * @param idTag
	 * @param major
	 * @param majorStr
	 * @param minor
	 * @param minorStr
	 * @param tagType
	 * @param tagName
	 * @param tagValue
	 */
	public void init(long idDcm, Integer idTag, short major,
			String majorStr, short minor, String minorStr, String tagType,
			String tagName, String tagValue) {
		this.idDcm = idDcm;
		this.major = major;
		this.minor = minor;
		this.majorStr = majorStr;
		this.minorStr = minorStr;
		this.idTag = idTag;
		this.tagType = tagType;
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	public long getIdDcm() {
		return idDcm;
	}

	public Integer getIdTag() {
		return idTag;
	}

	public String getTagType() {
		return tagType;
	}

	public String getTagValue() {
		return tagValue;
	}
	
	

	public short getMajor() {
		return major;
	}

	public short getMinor() {
		return minor;
	}

	public String getMajorStr() {
		return majorStr;
	}

	public String getMinorStr() {
		return minorStr;
	}

	@Override
	public String toString() {
		return "DcmTagProxy id: " + idDcm + " (" + getMajorStr() + ","
				+ getMinorStr() + ") " + tagType + ";" + tagName + ";"
				+ tagValue;
	}

}
