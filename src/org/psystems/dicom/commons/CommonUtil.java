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
package org.psystems.dicom.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.component.StudyManagePanel;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.server.DcmSnd;
import org.psystems.dicom.browser.server.Jpg2Dcm;
import org.psystems.dicom.browser.server.Pdf2Dcm;

/**
 * Утилитный класс
 * 
 * @author dima_d
 * 
 */
public class CommonUtil {

	// static Connection connection;
	// static String connectionStr =
	// "jdbc:derby://localhost:1527//WORKDB/WEBDICOM";

	// Версия ПО (используется для проверки на стороне сервере при обновлении
	// клиента)
	public static String version = "0.1a"; // TODO Взять из конфига?
	

	private static Logger logger = Logger.getLogger(CommonUtil.class.getName());

	/**
	 * Получение соединения внутри сервлета
	 * 
	 * @param servletContext
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(ServletContext servletContext)
			throws SQLException {

		Connection connection = null;

		//
		String connectionUrl = servletContext
				.getInitParameter("webdicom.connection.url");
		if (connectionUrl != null) {
			Properties props = new Properties(); // connection properties
			props.put("user", "user1"); // FIXME взять из конфига
			props.put("password", "user1"); // FIXME взять из конфига

			connection = DriverManager.getConnection(connectionUrl
					+ ";create=true", props);
		} else {
			// for Tomcat
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				DataSource ds = (DataSource) envCtx.lookup("jdbc/webdicom");
				connection = ds.getConnection();
			} catch (NamingException e) {
				throw new SQLException("JNDI error " + e);
			}
		}

		return connection;
	}
	
	
	

	/**
	 * Создание и отправка dicom-файла в архив
	 * 
	 * @param context
	 * @param props
	 * @throws IOException
	 * @throws DefaultGWTRPCException
	 */
	public static void makeSendDicomFile(ServletContext context,
			Properties props) throws IOException, DefaultGWTRPCException {
		makeSendDicomFile(context, props, null);

	}

	/**
	 * 
	 * Создание и отправка dicom-файла в архив
	 * 
	 * @param context
	 * @param props
	 * @param attachStream null -  если нет вложений
	 * @throws IOException
	 * @throws DefaultGWTRPCException
	 */
	public static void makeSendDicomFile(ServletContext context,
			Properties props, InputStream attachStream) throws IOException,
			DefaultGWTRPCException {
		System.out.println("!!!! making dcm...");

		//FIXME Небольшой хак. приходится передавать content_type
		String contentType = (String) props.get("content_type");
		props.remove("content_type");

		//FIXME Небольшой хак. приходится создавать пустой стриам.
		if(attachStream==null) attachStream = new InputStream() {
			
			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return -1;
			}
		};
		
		// TODO Убрать дублирование кода в этих условиях!!!

		File dcmFileTmp = null;

		try {

			String prefix = "" + new Date().getTime() + "_"
					+ (int) (Math.random() * 10000000l);
			if (contentType!=null && contentType.equals("application/pdf")) {

				Pdf2Dcm pfg2Dcm = new Pdf2Dcm();
				String cfg = context
						.getInitParameter("webdicom.dir.newdcm.pdf.cfg");
				pfg2Dcm.loadConfiguration(new File(cfg));

				for (Iterator<Object> iter = props.keySet().iterator(); iter
						.hasNext();) {
					String key = (String) iter.next();
					String value = props.getProperty(key);
					
					
					if(!isValidTagname(key)) {
						logger.warn("Not dicom standart tag name! tag["+key+"]");
						continue; 
					}
					

					// Заточка пот значение тега = 'норма'
					if (key.equals("00102000")
							&& value != null
							&& value
									.equals(StudyManagePanel.studyResultTitle)) {
						value = "";
					}
					pfg2Dcm.setCfgProperty(key, value);
					System.out.println("!!!! key=" + key + "; value=" + value);

				

				}

				// TODO Сделать сохранение даты через attrs.putDate(...)
				// jpg2Dcm.setCfgProperty("00100030", "20010329");
				pfg2Dcm.setCharset("ISO_IR 144");
				// jpg2Dcm.setCharset("ISO_IR 192");//UTF-8

				// String dcmDir = getServletContext().getInitParameter(
				// "webdicom.dir.newdcm");

				String dcmTmpDir = context
						.getInitParameter("webdicom.dir.newdcm.tmp");

				// String dcmFileName = dcmDir + "/" + prefix + ".dcm";
				String tmpFileName = dcmTmpDir + "/" + prefix + ".dcm";
				// TODO Задать в конфиге
				dcmFileTmp = new File(tmpFileName);

				pfg2Dcm.convert(attachStream, dcmFileTmp);

				// dcmFile = new File(dcmFileName);

				// dcmFileTmp.renameTo(dcmFile);
				System.out.println("!!!! making PDF dcm SUCCESS!");

			}

			// -------------------------------

			// TODO Убрать дублирование кода в этих условиях!!!
			if (contentType !=null && contentType.equals("image/jpg")) {

				Jpg2Dcm jpg2Dcm = new Jpg2Dcm();
				String cfg = context
						.getInitParameter("webdicom.dir.newdcm.jpg.cfg");
				jpg2Dcm.loadConfiguration(new File(cfg), true);

				for (Iterator<Object> iter = props.keySet().iterator(); iter
						.hasNext();) {
					String key = (String) iter.next();
					String value = props.getProperty(key);

					// Заточка пот значение тега = 'норма'
					if (key.equals("00102000")
							&& value != null
							&& value
									.equals(StudyManagePanel.studyResultTitle)) {
						value = "";
					}
					jpg2Dcm.setCfgProperty(key, value);
					System.out.println("!!!! key=" + key + "; value=" + value);
				}

				// TODO Сделать сохранение даты через attrs.putDate(...)
				// jpg2Dcm.setCfgProperty("00100030", "20010329");
				jpg2Dcm.setCharset("ISO_IR 144");
				// jpg2Dcm.setCharset("ISO_IR 192");//UTF-8

				// String dcmDir = getServletContext().getInitParameter(
				// "webdicom.dir.newdcm");

				String dcmTmpDir = context
						.getInitParameter("webdicom.dir.newdcm.tmp");

				// String dcmFileName = dcmDir + "/" + prefix + ".dcm";
				String tmpFileName = dcmTmpDir + "/" + prefix + ".dcm";
				
				// TODO Задать в конфиге
				dcmFileTmp = new File(tmpFileName);

				jpg2Dcm.convert(attachStream, dcmFileTmp);

				// dcmFile = new File(dcmFileName);

				// dcmFileTmp.renameTo(dcmFile);
				System.out.println("!!!! making IMAGE dcm SUCCESS!");

			}

			
			String connectionStr = context
					.getInitParameter("webdicom.archive.connection");

			DcmSnd.sendToArchive(connectionStr, dcmFileTmp);

		}catch(Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		finally {
			if (dcmFileTmp != null)
				dcmFileTmp.delete();
		}

		System.out.println("!!!! Sending IMAGE dcm SUCCESS!");

	}


	/**
	 * Проверка на валидносить имени тега
	 * @param key
	 * @return
	 */
	public static boolean isValidTagname(String key) {
		Matcher matcher = Pattern.compile("^\\p{XDigit}{8}$").matcher(key);
		return matcher.matches();
	}
	

	
	
	
	
	

}
