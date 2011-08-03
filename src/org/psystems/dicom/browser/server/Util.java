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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.exception.VersionGWTRPCException;
import org.psystems.dicom.browser.client.proxy.ARPCRequest;

/**
 * Утилитный класс
 * 
 * @author dima_d
 * 
 */
public class Util {

    // static Connection connection;
    // static String connectionStr =
    // "jdbc:derby://localhost:1527//WORKDB/WEBDICOM";

    // Версия ПО (используется для проверки на стороне сервере при обновлении
    // клиента)
    public static String version = "0.1a"; // TODO Взять из конфига?

    // Имя атрибута сессии
    public static String sessionAttrName = "dicom-browser-session";

    private static Logger logger = Logger.getLogger(Util.class.getName());

    /**
     * Журналирование Эксепшина с Получением стека
     * 
     * @param e
     * @return
     */
    public static DefaultGWTRPCException throwPortalException(String msg) {
	return throwPortalException(msg, new RuntimeException());
    }

    /**
     * Журналирование Эксепшина с Получением стека
     * 
     * @param e
     * @return
     */
    public static DefaultGWTRPCException throwPortalException(String msg, Throwable e) {

	String marker = Thread.currentThread().getId() + "_" + new Date().getTime();
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	String stack = sw.toString();
	// TODO Сделать через Log4j
	System.err.println("Portal Error [" + marker + "] " + e.getMessage() + " stack:\n" + stack);
	return new DefaultGWTRPCException(marker, msg, e, stack);

    }

    /**
     * Журналирование Эксепшина в логе файле Получением стека
     * 
     * @param e
     * @return
     */
    public static String loggingException(String msg, Throwable e) {

	String marker = Thread.currentThread().getId() + "_" + new Date().getTime();
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	e.printStackTrace(pw);
	String stack = sw.toString();
	// TODO Сделать через Log4j
	System.err.println("Portal Error [" + marker + "] " + e.getMessage() + " stack:\n" + stack);
	return e.getClass() + "[" + marker + "] " + msg;

    }

    /**
     * @param jdbcName
     *            - Имя соединения
     * @param servletContext
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(String jdbcName, ServletContext servletContext) throws SQLException {

	Connection connection = null;

	try {
	    //
	    String connectionDriver = servletContext.getInitParameter("webdicom.connection." + jdbcName + ".driver");

	    //
	    String connectionUrl = servletContext.getInitParameter("webdicom.connection." + jdbcName + ".url");

	    if (connectionUrl != null) {
		Properties props = new Properties(); // connection properties

		if (connectionDriver != null) {
		    try {
			Class.forName(connectionDriver);
		    } catch (ClassNotFoundException exx) {
			throw new SQLException("driver not found!  '" + connectionDriver + "'");
		    }
		}
		connection = DriverManager.getConnection(connectionUrl, props);
	    } else {
		// for Tomcat
		try {
		    Context initCtx = new InitialContext();
		    Context envCtx = (Context) initCtx.lookup("java:comp/env");
		    DataSource ds = (DataSource) envCtx.lookup("jdbc/" + jdbcName);
		    connection = ds.getConnection();
		} catch (NamingException e) {
		    throw new SQLException("JNDI error " + e);
		}
	    }
	} catch (SQLException ex) {
	    System.err.println("DBERROR: " + ex + " code:" + ex.getErrorCode() + " state: " + ex.getSQLState());
	    ex.printStackTrace();
	    throw ex;
	}

	return connection;
    }

    /**
     * @param servletContext
     * @return
     * @throws SQLException
     */
    public Connection getConnection2(ServletContext servletContext) throws SQLException {

	Connection connection = null;

	// for Tomcat
	try {
	    Context initCtx = new InitialContext();
	    Context envCtx = (Context) initCtx.lookup("java:comp/env");
	    DataSource ds = (DataSource) envCtx.lookup("jdbc/webdicom");
	    connection = ds.getConnection();
	} catch (NamingException e) {
	    throw new SQLException("JNDI error " + e);
	}

	return connection;
    }

    /**
     * TODO !!! Убрать !!! Проверка версии клиентског запроса
     * 
     * @param version
     * @return
     * @return
     * @throws VersionGWTRPCException
     */
    public static void checkClentVersion(String version) throws VersionGWTRPCException {
	if (!version.equalsIgnoreCase(version)) {
	    throw new VersionGWTRPCException("Версия клиента не совпадает с версией сервера! " + version + " != "
		    + version);
	}
    }

    /**
     * Проверка версии клиента (при запросе)
     * 
     * @param v
     * @return
     * @throws VersionGWTRPCException
     */
    public static void checkClentVersion(ARPCRequest req) throws VersionGWTRPCException {
	if (!version.equalsIgnoreCase(req.getVersion())) {
	    throw new VersionGWTRPCException("Версия клиента не совпадает с версией сервера! " + req.getVersion()
		    + " != " + version);
	}
    }

}
