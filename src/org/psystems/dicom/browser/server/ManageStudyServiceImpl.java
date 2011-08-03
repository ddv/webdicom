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
package org.psystems.dicom.browser.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.service.ManageStydyService;
import org.psystems.dicom.commons.orm.PersistentManagerDerby;
import org.psystems.dicom.commons.orm.entity.DataException;
import org.psystems.dicom.commons.orm.entity.Study;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ManageStudyServiceImpl extends RemoteServiceServlet implements
		ManageStydyService {
	
	private static Logger logger = Logger.getLogger(ManageStudyServiceImpl.class);

	@Override
	public void newStudy(String patientName) throws DefaultGWTRPCException {
		// TODO Auto-generated method stub
		System.out.println("!!!! making dcm...");

		Jpg2Dcm jpg2Dcm = new Jpg2Dcm();
		try {
			jpg2Dcm
					.loadConfiguration(
							new File(
									"C:\\WORK\\workspace\\dicom-browser\\test\\data\\jpg2dcm.cfg"),
							true);
		} catch (IOException e1) {
			throw org.psystems.dicom.browser.server.Util.throwPortalException("Config file not found! ",e1);
		}
		

//		try {
//			patientName = new String(patientName.getBytes("UTF-8"), "Cp1251");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			throw new DefaultGWTRPCException(
//					"Не могу создать файл с исследованием! " + e1);
//		}

	
		
		jpg2Dcm.setCfgProperty("00100010", patientName);
		//TODO Сделать сохранение даты через attrs.putDate(...)
		jpg2Dcm.setCfgProperty("00100030", "20010329");
		jpg2Dcm.setCharset("ISO_IR 144");
//		jpg2Dcm.setCharset("ISO_IR 192");//UTF-8

		File jpgFile = new File(
				"C:\\WORK\\workspace\\dicom-browser\\test\\data\\test.JPG");
		File dcmFile = new File(
				"C:\\WORK\\workspace\\dicom-browser\\test\\data\\test.dcm");

//		try {
//			jpg2Dcm.convert(jpgFile, dcmFile);
//			System.out.println("!!!! making dcm SUCCESS!");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw new DefaultGWTRPCException(
//					"Не могу создать файл с исследованием! " + e);
//		}
	}

	
	@Override
	public void studyRemoveRestore(long idStudy, boolean removed)
			throws DefaultGWTRPCException {
		try {
			Connection connection = Util.getConnection("main",getServletContext());
			PersistentManagerDerby.studyRemoveRestore(connection, idStudy, removed);
		} catch (SQLException e) {
			logger.error(e);
			throw Util.throwPortalException("Study Remove/Restore! ",e);
		} catch (DataException e) {
			logger.error(e);
			throw Util.throwPortalException("Study Remove/Restore! ",e);
		}
		
	}
	
	@Override
	public void dcmFileRemoveRestore(long idDcmFile, boolean removed)
			throws DefaultGWTRPCException {
		try {
			Connection connection = Util.getConnection("main",getServletContext());
			PersistentManagerDerby.dcmFileRemoveRestore(connection, idDcmFile, removed);
		} catch (SQLException e) {
			logger.error(e);
			throw Util.throwPortalException("Study DCM file Remove/Restore! ",e);
		} catch (DataException e) {
			logger.error(e);
			throw Util.throwPortalException("Study DCM file Remove/Restore! ",e);
		}
		
		
	}

	



}
