package org.psystems.dicom.commons.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;


public class ORMUtil {
	
	public static SimpleDateFormat dateFormatSQL = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	public static SimpleDateFormat dateTimeFormatSQL = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat dateFormatUser = new SimpleDateFormat(
	"dd.MM.yyyy");
	
	public static SimpleDateFormat dateTimeFormatUser = new SimpleDateFormat(
	"dd.MM.yyyy HH:mm:ss");
	
	public final static HashMap<String, String> modalityList = new HashMap<String, String>();
	static {
	modalityList.put("AS", "Angioscopy-Retired");
	modalityList.put("BI", "Biomagnetic Imaging");
	modalityList.put("CD", "Color Flow Doppler-Retired");
	modalityList.put("CF", "Cinefluorography (retired)");
	modalityList.put("CP", "Colposcopy Retired");
	modalityList.put("CR", "Computed Radiography");
	modalityList.put("CS", "CystoscopyRetired");
	modalityList.put("CT", "Computed Tomography");
	modalityList.put("DD", "Duplex Doppler Retired");
	modalityList.put("DF", "Digital Fluoroscopy (retired)");
	modalityList.put("DG", "Diaphanography");
	modalityList.put("DM", "Digital Microscopy");
	modalityList.put("DS", "Digital Subtraction Angiography Retired");
	modalityList.put("DX", "Digital radiography");
	modalityList.put("EC", "Echocardiography Retired");
	modalityList.put("ES", "Endoscopy");
	modalityList.put("FA", "Fluorescein Angiography Retired");
	modalityList.put("FS", "Fundoscopy Retired");
	modalityList.put("HC", "Hard Copy");
	modalityList.put("LP", "Laparoscopy Retired");
	modalityList.put("LS", "Laser Surface Scan");
	modalityList.put("MA", "Magnetic resonance angiography Retired");
	modalityList.put("MG", "Mammography");
	modalityList.put("MR", "Magnetic Resonance");
	modalityList.put("MS", "Magnetic Resonance Spectroscopy Retired");
	modalityList.put("NM", "Nuclear Medicine");
	modalityList.put("OT", "Other");
	modalityList.put("PT", "Positron Emission Tomography (PET)");
	modalityList.put("RF", "Radio Fluoroscopy");
	modalityList.put("RG", "Radiographic Imaging (conventional film screen)");
	modalityList.put("RTDOSE", "Radiotherapy Dose (a.k.a. RD)");
	modalityList.put("RTIMAGE", "Radiotherapy Image");
	modalityList.put("RTPLAN", "Radiotherapy Plan (a.k.a. RP)");
	modalityList.put("RTSTRUCT", "Radiotherapy Structure Set (a.k.a. RS)");
	modalityList.put("SR", "Structured Reporting");
	modalityList.put("ST", "Single-photon Emission Computed Tomography Retired");
	modalityList.put("TG", "Thermography");
	modalityList.put("US", "Ultrasound");
	modalityList.put("VF", "Videofluorography (retired)");
	modalityList.put("XA", "X-Ray Angiography");
	modalityList.put("XC", "eXternal Camera");
	modalityList.put("ECG", "Electrocardiograms");
	}
	
	
	/**
	 * Конвертация Util даты
	 * 
	 * @param date
	 * @return
	 */
	public static String utilDateToSQLDateString(Date date) {
		if(date==null) return null;
		return dateFormatSQL.format(date);
	}
	
	
	
	/**
	 * @param date
	 * @return
	 */
	public static String userDateStringToSQLDateString(String date) {
		if(date==null) return null;
		try {
			
			return dateFormatSQL.format(dateFormatUser.parse(date));
		} catch (ParseException e) {
			throw new IllegalArgumentException("string with a date has wrong format " +
					dateFormatUser.toPattern() + " input string: [" + date + "]", e);
		}
	}
	
	
	/**
	 * Конвертация SQL даты с проверкой строки с датой на валидность
	 * @param dateStr
	 * @return
	 */
	public static Date dateSQLToUtilDate(String dateStr) {
		if(dateStr==null) return null;
		try {
			return dateFormatSQL.parse(dateStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException("string with a date has wrong format " +
					dateFormatSQL.toPattern() + " input string: [" + dateStr + "]", e);
		}
	}
	
	/**
	 * Конвертация Util даты+время
	 * 
	 * @param date
	 * @return
	 */
	public static String utilDateTimeToSQLDateTimeString(Date date) {
		if(date==null) return null;
		return dateTimeFormatSQL.format(date);
	}
	
	/**
	 * Конвертация SQL даты+время с проверкой строки с датой на валидность
	 * @param dateStr
	 * @return
	 */
	public static Date dateTimeSQLToUtilDate(String dateStr) {
		if(dateStr==null) return null;
		try {
			return dateTimeFormatSQL.parse(dateStr);
		} catch (ParseException e) {
			throw new IllegalArgumentException("string with a date and time has wrong format " +
					dateTimeFormatSQL.toPattern() + " input string: [" + dateStr + "]", e);
		}
	}
	
	
	
	/**
	 * Получение КБП
	 * @param PatientName
	 * @param PatientBirthDate
	 * @return
	 */
	public static String makeShortName (String PatientName, String PatientBirthDate) {
		
		if(PatientName==null || PatientBirthDate==null) return null;
		String result = null;
		Matcher matcher = Pattern.compile("\\s*(...).*?\\s+(.).*?\\s+(.).*?").matcher(PatientName.toUpperCase());
		if (matcher.matches()) {
			Calendar cal = Calendar.getInstance();
			Date date = dateSQLToUtilDate(PatientBirthDate);
			cal.setTime(date );
			
			int year = (cal.get(Calendar.YEAR) - 1900);
			if( year >= 100) year -= 100;
			String yearS = "" + year;
			if(year<10) yearS = "0" + yearS;
				
			result = matcher.group(1)+matcher.group(2)+matcher.group(3)+yearS; 
		}
		return result;
		
	}
	
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
	 * Список модальностей одной строкой
	 * @return
	 */
	public static String getAllModalities () {
		StringBuffer sb = new StringBuffer();
		for (String mod : modalityList.keySet()) {
			if(sb.length()!=0) sb.append("|");
			sb.append(mod);
		}
		return sb.toString();
	}
	
	

}
