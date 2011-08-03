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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.StringUtils;
import org.dcm4che2.util.TagUtils;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.proxy.DcmTagProxy;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCRequest;
import org.psystems.dicom.browser.client.proxy.DcmTagsRPCResponse;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.OOTemplateProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.PatientsRPCRequest;
import org.psystems.dicom.browser.client.proxy.PatientsRPCResponse;
import org.psystems.dicom.browser.client.proxy.QueryDirectionProxy;
import org.psystems.dicom.browser.client.proxy.RPCDcmProxyEvent;
import org.psystems.dicom.browser.client.proxy.Session;
import org.psystems.dicom.browser.client.proxy.StudyProxy;
import org.psystems.dicom.browser.client.service.BrowserService;
import org.psystems.dicom.browser.server.drv.Storage;
import org.psystems.dicom.commons.orm.PersistentManagerDerby;
import org.psystems.dicom.commons.orm.entity.Direction;
import org.psystems.dicom.commons.orm.entity.ManufacturerDevice;
import org.psystems.dicom.commons.orm.entity.QueryStudy;
import org.psystems.dicom.commons.orm.entity.Study;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BrowserServiceImpl extends RemoteServiceServlet implements BrowserService {

    private int maxReturnRecords = 20; // Максимальное количество возвращаемых
    // записей

    private static Logger logger = Logger.getLogger(BrowserServiceImpl.class);

    // static {
    // PropertyConfigurator.configure("WEB-INF/log4j.properties");}//TODO Убрать
    // !!!

    @Override
    public RPCDcmProxyEvent findStudy(long transactionId, String version, String queryStr, HashMap<String, String> attrs)
	    throws DefaultGWTRPCException {

	// проверка версии клиента
	Util.checkClentVersion(version);

	PreparedStatement psSelect = null;

	try {

	    Connection connection = Util.getConnection("main", getServletContext());

	    ArrayList<StudyProxy> data = new ArrayList<StudyProxy>();

	    // TODO ТУт костыль под worklist !!!
	    String studyDB = null;
	    String studyDE = null;
	    String manufacturerModelName = null;
	    String studyResult = null;
	    String sortOrder = null;
	    if (attrs != null) {
		studyDB = attrs.get("beginStudyDate");
		studyDE = attrs.get("endStudyDate");
		studyResult = attrs.get("studyResult");
		manufacturerModelName = attrs.get("manufacturerModelName");
		sortOrder = attrs.get("sortOrder");
	    }

	    ArrayList<Study> studies = new ArrayList<Study>();

	    Matcher matcher = Pattern.compile(
		    "^\\s{0,}(\\D+\\s+\\D+\\s+\\D+)\\s(\\d{1,2})\\.(\\d{1,2})\\.(\\d{4})\\s{0,}$").matcher(queryStr);
	    boolean fullSearch = matcher.matches();

	    String fio = null, day = null, month = null, year = null;
	    if (fullSearch) {
		fio = matcher.group(1);
		day = matcher.group(2);
		month = matcher.group(3);
		year = matcher.group(4);
		// where =
		// " UPPER(PATIENT_NAME) = UPPER(?) AND PATIENT_BIRTH_DATE = ?";
	    }

	    if (fullSearch) {

		QueryStudy query = new QueryStudy();
		query.setManufacturerModelName(manufacturerModelName);
		query.setPatientName(fio);
		query.setPatientBirthDate(year + "-" + month + "-" + day);
		query.setBeginStudyDate(studyDB);
		query.setEndStudyDate(studyDE);

		query.setStudyResult(studyResult);// TODO атавизм
		query.setSortOrder(sortOrder);// TODO атавизм

		PersistentManagerDerby pm = new PersistentManagerDerby(connection);
		studies = pm.queryStudies(query);

		// studies = Study.getStudues(connection, null, null, null,
		// manufacturerModelName, null, fio,
		// null, year+"-"+month+"-"+day, null, studyDB, studyDE,
		// studyResult, sortOrder);

	    } else if (queryStr.matches("^\\D{5}\\d{2}$")) { // Если поиск по
		// КБП

		QueryStudy query = new QueryStudy();
		query.setManufacturerModelName(manufacturerModelName);
		query.setPatientShortName(queryStr);
		query.setBeginStudyDate(studyDB);
		query.setEndStudyDate(studyDE);

		query.setStudyResult(studyResult);// TODO атавизм
		query.setSortOrder(sortOrder);// TODO атавизм

		PersistentManagerDerby pm = new PersistentManagerDerby(connection);
		studies = pm.queryStudies(query);

		// studies = Study.getStudues(connection, null, null, null,
		// manufacturerModelName, null, null,
		// queryStr, null, null, studyDB, studyDE, studyResult,
		// sortOrder);
	    } else {

		QueryStudy query = new QueryStudy();
		query.setManufacturerModelName(manufacturerModelName);
		query.setPatientName(queryStr);
		query.setBeginStudyDate(studyDB);
		query.setEndStudyDate(studyDE);

		query.setStudyResult(studyResult);// TODO атавизм
		query.setSortOrder(sortOrder);// TODO атавизм

		PersistentManagerDerby pm = new PersistentManagerDerby(connection);
		studies = pm.queryStudies(query);

		// studies = Study.getStudues(connection, null, null, null,
		// manufacturerModelName, null, queryStr,
		// null, null, null, studyDB, studyDE, studyResult, sortOrder);
	    }

	    for (Study study : studies) {
		StudyProxy studyProxy = ORMHelpers.getStudyProxy(study);
		// Получаем список файлов
		// Сделана раздельная загрузка исследования и файлов для
		// экономии траффика
		studyProxy.setFiles(PersistentManagerDerby.getDcmFileProxies(connection, studyProxy.getId()));
		data.add(studyProxy);
	    }

	    // DcmFileProxyCortege[] result = data.toArray(new
	    // DcmFileProxy[data.size()]);

	    Calendar calendar = Calendar.getInstance();

	    int tzoffset = calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
	    // System.out.println("!!!!! "+tzoffset );

	    long time = calendar.getTimeInMillis();
	    time = time - (time % (60 * 60 * 24 * 1000)) - tzoffset;
	    calendar.setTimeInMillis(time);

	    Date sqlDate = new java.sql.Date(time);
	    // updateDayStatInc(sqlDate, "CLIENT_CONNECTIONS", (long) 1);
	    // System.out.println("!!! sqlDate="+sqlDate);

	    RPCDcmProxyEvent event = new RPCDcmProxyEvent();
	    event.init(transactionId, data);
	    return event;

	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("Can't find study: ", e);
	} finally {

	    try {
		if (psSelect != null)
		    psSelect.close();
	    } catch (SQLException e) {
		logger.error(e);
		throw Util.throwPortalException("Can't find study: ", e);
	    }
	}

    }

    /**
     * Обновление метрики дневной статистики (инкремент)
     * 
     * @param date
     * @param metric
     * @param value
     * @throws SQLException
     */
    private void updateDayStatInc(Date date, String metric, long value) throws SQLException {

	Connection connection = Util.getConnection("main", getServletContext());

	PreparedStatement stmt = null;

	// Calendar calendar = Calendar.getInstance();
	// long time = calendar.getTimeInMillis();
	// time = time - (time % (60 * 60 * 24 * 1000));
	// // calendar.setTimeInMillis(time);

	long time = date.getTime();
	time = time - (time % (60 * 60 * 24 * 1000));
	date = new Date(time);

	logger.info(metric + "=" + value + " of " + date);

	// Проверка на наличии этого файла в БД
	try {
	    long valueOld = checkDayMetric(metric, date);
	    logger.info("metric already in database [" + metric + "][" + date + "][" + valueOld + "]");

	    stmt = connection.prepareStatement("update WEBDICOM.DAYSTAT " + " SET METRIC_VALUE_LONG = ? "
		    + " where METRIC_NAME = ? AND METRIC_DATE = ?");

	    long sumVal = value + valueOld;
	    stmt.setLong(1, sumVal);
	    stmt.setString(2, metric);
	    stmt.setDate(3, date);
	    stmt.executeUpdate();

	    // System.out.println("!!!! [U] [" + date + "][" + metric + "]="+
	    // sumVal + " valueOld="+valueOld);

	} catch (NoDataFoundException ex) {
	    // Делаем вставку
	    logger.info("insert data in database [" + metric + "][" + date + "][" + value + "]");
	    stmt = connection.prepareStatement("insert into WEBDICOM.DAYSTAT "
		    + " (METRIC_NAME, METRIC_DATE, METRIC_VALUE_LONG)" + " values (?, ?, ?)");

	    stmt.setString(1, metric);
	    stmt.setDate(2, date);
	    stmt.setLong(3, value);
	    stmt.executeUpdate();

	    // System.out.println("!!!! [I]  [" + date + "][" + metric + "]="+
	    // value);
	}

    }

    /**
     * Проверка на наличии этого файла в БД TODO Как-то заморочено
     * получилось....
     * 
     * @param dcm_file_name
     * @return
     * @throws SQLException
     */
    private long checkDayMetric(String metric, Date date) throws SQLException {

	Connection connection = Util.getConnection("main", getServletContext());

	PreparedStatement psSelect = connection
		.prepareStatement("SELECT METRIC_VALUE_LONG FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ? and METRIC_DATE =? ");
	try {
	    psSelect.setString(1, metric);
	    psSelect.setDate(2, date);
	    ResultSet rs = psSelect.executeQuery();
	    while (rs.next()) {
		return rs.getLong("METRIC_VALUE_LONG");
	    }

	} finally {
	    psSelect.close();
	}
	throw new NoDataFoundException("No data");
    }

    @Override
    public DcmTagsRPCResponse getDcmTags(DcmTagsRPCRequest req) throws DefaultGWTRPCException {

	try {
	    // проверка версии клиента TODO сделать MOCK?
	    Util.checkClentVersion(req);
	    long idDcm = req.getIdDcm();
	    PreparedStatement psSelect = null;

	    try {
		ArrayList<DcmTagProxy> data = getTags(idDcm);
		DcmTagsRPCResponse responce = new DcmTagsRPCResponse();
		responce.setTagList(data);

		return responce;

	    } finally {

		try {
		    if (psSelect != null)
			psSelect.close();
		} catch (SQLException e) {
		    logger.error(e);
		    throw Util.throwPortalException("Can't getting tags: ", e);
		}
	    }
	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("Can't getting tags: ", e);
	}
    }

    @Override
    public ArrayList<DcmTagProxy> getDcmTagsFromFile(long transactionId, String version, long idDcmFile)
	    throws DefaultGWTRPCException {

	try {

	    // проверка версии клиента
	    Util.checkClentVersion(version);

	    String dcmRootDir = getServletContext().getInitParameter("webdicom.dir.src");
	    PreparedStatement psSelect = null;

	    try {
		String fileName = null;
		Connection connection = Util.getConnection("main", getServletContext());
		psSelect = connection.prepareStatement("SELECT ID,  DCM_FILE_NAME "
			+ " FROM WEBDICOM.DCMFILE WHERE ID = ? ");
		psSelect.setLong(1, idDcmFile);
		ResultSet rs = psSelect.executeQuery();
		int index = 0;
		while (rs.next()) {
		    String file = rs.getString("DCM_FILE_NAME");
		    fileName = dcmRootDir + File.separator + file;
		    index++;
		    break;
		}
		rs.close();
		if (index == 0) {
		    throw Util.throwPortalException("Dcm file not found! id=" + idDcmFile);

		}

		return getTagsFromFile(idDcmFile, fileName);

	    } finally {

		try {
		    if (psSelect != null)
			psSelect.close();
		} catch (Throwable e) {
		    logger.error(e);
		    throw Util.throwPortalException("getDcmTagsFromFile error! id=" + idDcmFile, e);
		}
	    }

	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("getDcmTagsFromFile error! id=" + idDcmFile, e);
	}
    }

    /**
     * @param idDcm
     * @param fileName
     * @return
     * @throws IOException
     */
    private ArrayList<DcmTagProxy> getTagsFromFile(long idDcm, String fileName) throws IOException {
	DicomObject dcmObj;
	DicomInputStream din = null;
	SpecificCharacterSet cs = new SpecificCharacterSet("ISO-8859-5");
	ArrayList<DcmTagProxy> data = new ArrayList<DcmTagProxy>();

	try {

	    File f = new File(fileName);
	    long fileSize = f.length();
	    din = new DicomInputStream(f);
	    dcmObj = din.readDicomObject();
	    // System.out.println("dcmObj=" + dcmObj);

	    // читаем кодировку из dcm-файла
	    if (dcmObj.get(Tag.SpecificCharacterSet) != null) {
		SpecificCharacterSet cs1 = SpecificCharacterSet.valueOf(dcmObj.get(Tag.SpecificCharacterSet)
			.getStrings(null, false));
	    }

	    // DecimalFormat format = new DecimalFormat("0000");

	    // Раскручиваем теги
	    for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
		DicomElement element = it.next();

		int tag = element.tag();
		short major = (short) (tag >> 16);
		short minor = (short) (tag);

		StringBuffer sb = new StringBuffer();
		StringUtils.shortToHex(tag >> 16, sb);
		String majorStr = sb.toString();

		sb = new StringBuffer();
		StringUtils.shortToHex(tag, sb);
		String minorStr = sb.toString();

		String type = element.vr().toString();

		int length = element.length();
		int maxLength = 200;
		if (length > maxLength)
		    length = maxLength;

		DcmTagProxy proxy = new DcmTagProxy();
		proxy.init(idDcm, tag, major, majorStr, minor, minorStr, type, dcmObj.nameOf(tag), element
			.getValueAsString(cs, length));
		data.add(proxy);

	    }

	} catch (org.dcm4che2.data.ConfigurationError e) {
	    if (e.getCause() instanceof UnsupportedEncodingException) {
		logger.fatal("Unsupported character set" + cs + " " + e);
	    }
	    logger.fatal("" + e);
	} catch (IOException e) {
	    e.printStackTrace();
	    logger.fatal("" + e);
	} finally {
	    try {
		if (din != null)
		    din.close();
	    } catch (IOException ignore) {
	    }
	}
	return data;
    }

    /**
     * Получение тегов
     * 
     * @param idDcm
     * @return
     * @throws SQLException
     */
    private ArrayList<DcmTagProxy> getTags(long idDcm) throws SQLException {

	PreparedStatement psSelect = null;

	try {
	    Connection connection = Util.getConnection("main", getServletContext());
	    psSelect = connection
		    .prepareStatement("SELECT TAG, TAG_TYPE, VALUE_STRING FROM WEBDICOM.DCMFILE_TAG WHERE FID_DCMFILE = ?");

	    psSelect.setLong(1, idDcm);
	    ResultSet rs = psSelect.executeQuery();
	    ArrayList<DcmTagProxy> data = new ArrayList<DcmTagProxy>();
	    // DecimalFormat format = new DecimalFormat("0000");
	    while (rs.next()) {
		DcmTagProxy proxy = new DcmTagProxy();

		int tag = rs.getInt("TAG");
		short major = (short) (tag >> 16);
		short minor = (short) (tag);

		StringBuffer sb = new StringBuffer();
		StringUtils.shortToHex(tag >> 16, sb);
		String majorStr = sb.toString();

		sb = new StringBuffer();
		StringUtils.shortToHex(tag, sb);
		String minorStr = sb.toString();

		// StringBuffer sb = new StringBuffer();
		// StringUtils.shortToHex(tag >> 16, sb);
		// String major = sb.toString();
		//
		// sb = new StringBuffer();
		// StringUtils.shortToHex(tag, sb);
		// String minor = sb.toString();

		proxy.init(idDcm, tag, major, majorStr, minor, minorStr, rs.getString("TAG_TYPE"), TagUtils.toString(rs
			.getInt("TAG")), rs.getString("VALUE_STRING"));
		data.add(proxy);
	    }
	    rs.close();
	    return data;

	} finally {
	    if (psSelect != null)
		psSelect.close();
	}
    }

    @Override
    public PatientsRPCResponse getPatients(PatientsRPCRequest req) throws DefaultGWTRPCException {

	try {
	    // проверка версии клиента TODO сделать MOCK?
	    Util.checkClentVersion(req);
	    ArrayList<PatientProxy> data;

	    data = Storage.getPatients(getServletContext(), req.getQueryStr(), req.getLimit());

	    PatientsRPCResponse resp = new PatientsRPCResponse();
	    resp.init(req);
	    resp.setPatients(data);

	    return resp;
	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("getPatients error! ", e);
	}
    }

    @Override
    public StudyProxy getStudyByID(long transactionId, String version, Long id) throws DefaultGWTRPCException {

	// проверка версии клиента
	Util.checkClentVersion(version);

	PreparedStatement psSelect = null;

	try {

	    Connection connection = Util.getConnection("main", getServletContext());
	    PersistentManagerDerby pm = new PersistentManagerDerby(connection);

	    Study study = pm.getStudyByID(id);
	    if (study == null)
		return null;

	    StudyProxy studyProxy = ORMHelpers.getStudyProxy(study);
	    // Сделана раздельная загрузка исследования и файлов для экономии
	    // траффика
	    studyProxy.setFiles(PersistentManagerDerby.getDcmFileProxies(connection, studyProxy.getId()));

	    return studyProxy;

	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("Find study error! ", e);

	} finally {

	    try {
		if (psSelect != null)
		    psSelect.close();
	    } catch (Throwable e) {
		logger.error(e);
		throw Util.throwPortalException("Find study error! ", e);
	    }
	}

    }
    
    

    @Override
    public StudyProxy[] getStudiesByDirectionID(Long id) throws DefaultGWTRPCException {
	PreparedStatement psSelect = null;

	try {

	    Connection connection = Util.getConnection("main", getServletContext());
	    PersistentManagerDerby pm = new PersistentManagerDerby(connection);
	    Study[] studies = pm.getStudiesByDirectionID(id);
	    ArrayList<StudyProxy> proxies = new ArrayList<StudyProxy>();
	    if (studies == null)
		return proxies.toArray(new StudyProxy[0]);

	    for (Study study : studies) {
		StudyProxy studyProxy = ORMHelpers.getStudyProxy(study);
		// Сделана раздельная загрузка исследования и файлов для
		// экономии траффика
		studyProxy.setFiles(PersistentManagerDerby.getDcmFileProxies(connection, studyProxy.getId()));
		proxies.add(studyProxy);
	    }

	    return proxies.toArray(new StudyProxy[studies.length]);

	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("get study error! ", e);

	} finally {

	    try {
		if (psSelect != null)
		    psSelect.close();
	    } catch (Throwable e) {
		logger.error(e);
		throw Util.throwPortalException("get study error! ", e);
	    }
	}

    }

    @Override
    public Session getSessionObject() throws DefaultGWTRPCException {

	try {
	    HttpServletRequest req = perThreadRequest.get();
	    HttpServletResponse resp = perThreadResponse.get();
	    Session sessionObject = (Session) req.getSession().getAttribute(Util.sessionAttrName);
	    return sessionObject;
	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("getSessionObject error! ", e);
	}
    }

    @Override
    public ArrayList<OOTemplateProxy> getOOTemplates(String modality) throws DefaultGWTRPCException {

	try {
	    ArrayList<OOTemplateProxy> result = new ArrayList<OOTemplateProxy>();

	    String ootmplRootDir = getServletContext().getInitParameter("webdicom.dir.ootmpl");

	    File[] files = new File(ootmplRootDir).listFiles();
	    for (int i = 0; i < files.length; i++) {
		if (files[i].isDirectory()) {

		    String dirName = files[i].getName();
		    // System.out.println(">> DIR: ["+dirName+"] modality=["+modality+"]");

		    if (modality == null || modality.equalsIgnoreCase(dirName)) {

			File[] tmpls = files[i].listFiles();
			for (int j = 0; j < tmpls.length; j++) {
			    File tmpl = tmpls[j];
			    String fileName = tmpl.getName();
			    // System.out.println(">>>>> TMPL: [" + fileName +
			    // "]"+modality);
			    OOTemplateProxy tmplProxy = new OOTemplateProxy();
			    tmplProxy.setModality(dirName);
			    tmplProxy.setTitle(tmpl.getName());
//			    tmplProxy.setUrl("ootmpl/" + dirName + "/" + fileName);
			    tmplProxy.setUrl("makepdf/" + dirName + "/" + fileName);
			    result.add(tmplProxy);
			}
		    }
		}
	    }

	    return result;
	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("getOOTemplates error! ", e);
	}
    }

    /**
     * Получение списка доступных аппаратов TODO !!! Реализовать!!!
     * 
     * @return
     */
    public static ManufacturerDevice[] getAllManufacturerDevices() {

	// TODO Взять из БД!!!
	ManufacturerDevice device = new ManufacturerDevice();
	device.setManufacturerModelName("testdevice");
	device.setManufacturerModelDescription("Тестовы девайс");
	device.setModality("CR");
	device.setManufacturerModelTypeDescription("Флюорограф");

	ArrayList<ManufacturerDevice> result = new ArrayList<ManufacturerDevice>();
	result.add(device);
	return result.toArray(new ManufacturerDevice[result.size()]);
    }

    @Override
    public ArrayList<DirectionProxy> getDirections(QueryDirectionProxy query) throws DefaultGWTRPCException {

	try {
	    Connection connection = Util.getConnection("main", getServletContext());
	    PersistentManagerDerby pm = new PersistentManagerDerby(connection);
	    ArrayList<DirectionProxy> drns = new ArrayList<DirectionProxy>();
	    for (Direction direction : pm.queryDirections(ORMHelpers.getQuerydirection(query))) {
		drns.add(ORMHelpers.getDirectionProxy(direction));
	    }
	    return drns;

	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("getDirections study error! ", e);
	}

    }

    @Override
    public void saveDirection(DirectionProxy drn) throws DefaultGWTRPCException {
	try {
	    Connection connection = Util.getConnection("main", getServletContext());
	    PersistentManagerDerby pm = new PersistentManagerDerby(connection);
	    pm.pesistentDirection(ORMHelpers.getDirection(drn));

	} catch (Throwable e) {
	    logger.error(e);
	    throw Util.throwPortalException("saveDirection error! ", e);
	}

    }

}
