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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ViewStudyImagesServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(ViewStudyImagesServlet.class
			.getName());

	static {
		// PropertyConfigurator.configure("WEB-INF/log4j.properties");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String path = req.getPathInfo().replaceFirst("/", "");

		Matcher matcher = Pattern.compile("^(.*).dcm$").matcher(path);
		if (matcher.matches()) {
			path = matcher.group(1);
		}

		String fileName = null;
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html");

		String dcmRootDir = getServletContext().getInitParameter(
				"webdicom.dir.src");

		// Смотрим, если передан Integer, зачит ищем по ID
		int dcmId = 0;
		try {
			dcmId = Integer.valueOf(path);
		} catch (NumberFormatException ex) {
			fileName = dcmRootDir + File.separator + path;
		}

		PreparedStatement psSelect = null;
		try {

			Connection connection = Util.getConnection("main",getServletContext());

			if (fileName == null) {
				// ищем по ID
				psSelect = connection
						.prepareStatement("SELECT * "
								+ " FROM WEBDICOM.DCMFILE WHERE FID_STUDY = ? ");
				psSelect.setInt(1, dcmId);
			} else {
				psSelect = connection
						.prepareStatement("SELECT ID, DCM_FILE_NAME "
								+ " FROM WEBDICOM.DCMFILE WHERE DCM_FILE_NAME = ? ");
				psSelect.setString(1, path);
			}
			ResultSet rs = psSelect.executeQuery();
			

			
//			resp.getWriter().write("<script language=\"javascript\">");
//			resp.getWriter().write("</script>");
			
			resp.getWriter().write("<table border='0' cellpadding='10'><tr>");
			while (rs.next()) {
				String file = rs.getString("DCM_FILE_NAME");
				dcmId = rs.getInt("ID");
				fileName = dcmRootDir + File.separator + file;
				printImage(resp,dcmId);
				
			}
			resp.getWriter().write("</tr></table>");
		

		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			try {
				if (psSelect != null)
					psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}

//		try {
//		} catch (FileNotFoundException ex) {
//			resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
//			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Dcm not found! "
//					+ fileName);
//			ex.printStackTrace();
//			return;
//		}

	}

	private void printImage(HttpServletResponse resp, int dcmId) throws IOException {
		resp.getWriter().write("<td>");
		String href = "../images/"+dcmId+".fullsize";
		
//		resp.getWriter().write("<a href='"+href +"' target='_blank'><image src='../images/"+dcmId+".100x100' </image> </a>");
		resp.getWriter().write("<a href='"+href+"' onclick=\"window.open('"+href+"','name1','left=0,top=0,width=800,height=600,toolbar=0,location=0,directories=0,menubar=0,status=0,resizable=1'); return false\"> <image src='../images/"+dcmId+".100x100' </image> </a>");
		resp.getWriter().write("</td>");
		
	}

	

}
