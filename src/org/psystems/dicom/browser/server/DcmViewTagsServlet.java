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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.SpecificCharacterSet;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.StringUtils;

public class DcmViewTagsServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(DcmViewTagsServlet.class
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
						.prepareStatement("SELECT ID,  DCM_FILE_NAME "
								+ " FROM WEBDICOM.DCMFILE WHERE ID = ? ");
				psSelect.setInt(1, dcmId);
			} else {
				psSelect = connection
						.prepareStatement("SELECT ID, DCM_FILE_NAME "
								+ " FROM WEBDICOM.DCMFILE WHERE DCM_FILE_NAME = ? ");
				psSelect.setString(1, path);
			}
			ResultSet rs = psSelect.executeQuery();
			int index = 0;
			while (rs.next()) {
				String file = rs.getString("DCM_FILE_NAME");
				dcmId = rs.getInt("ID");
				fileName = dcmRootDir + File.separator + file;
				index++;
				break;
			}
			if (index == 0) {
				resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
				resp
						.sendError(HttpServletResponse.SC_NOT_FOUND,
								"Dcm file not found! id=" + dcmId + " file="
										+ fileName);
				return;
			}

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

		try {
			showTags(fileName, resp);
		} catch (FileNotFoundException ex) {
			resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Dcm not found! "
					+ fileName);
			ex.printStackTrace();
			return;
		}

	}

	private void showTags(String fileName, HttpServletResponse resp)
			throws IOException {
		DicomObject dcmObj;
		DicomInputStream din = null;
		SpecificCharacterSet cs = new Win1251CharacterSet();
		// SpecificCharacterSet cs = new SpecificCharacterSet("ISO-8859-5");

		try {

			File f = new File(fileName);
			long fileSize = f.length();
			din = new DicomInputStream(f);
			dcmObj = din.readDicomObject();
			// System.out.println("dcmObj=" + dcmObj);

			// читаем кодировку из dcm-файла
			if (dcmObj.get(Tag.SpecificCharacterSet) != null
					&& dcmObj.get(Tag.SpecificCharacterSet).length() > 0) {
				cs = SpecificCharacterSet.valueOf(dcmObj.get(
						Tag.SpecificCharacterSet).getStrings(null, false));
			}

			resp.getWriter().write("<h1> Теги: </h1>");

			resp.getWriter().write("<table border=1>");

			// DecimalFormat format = new DecimalFormat("0000");

			// Раскручиваем теги
			for (Iterator<DicomElement> it = dcmObj.iterator(); it.hasNext();) {
				DicomElement element = it.next();

				int tag = element.tag();

				// Short ma = (short) (tag >> 16);
				// String major = format.format(ma);
				// Short mi = (short) (tag);
				// String minor = format.format(mi);
				// Integer.toHexString(ma)

				StringBuffer sb = new StringBuffer();
				StringUtils.shortToHex(tag >> 16, sb);
				String major = sb.toString();

				sb = new StringBuffer();
				StringUtils.shortToHex(tag, sb);
				String minor = sb.toString();

				// sb.append('(');
				// StringUtils.shortToHex(tag >> 16, sb);
				// sb.append(',');
				// StringUtils.shortToHex(tag, sb);
				// sb.append(')');

				String type = element.vr().toString();

				int length = element.length();
				int maxLength = 200;//TODO Убрать жесткое ограничение.
				if (length > maxLength)
					length = maxLength;

				String value = "Not be converted";
				if(!element.vr().equals(VR.SQ)) {
					value = element.getValueAsString(cs, length);
				}
				
				resp.getWriter().write(
						"<tr> " + " <td>" + major + " <td> " + minor
								+ " <td> short=" + (short) (tag) + "<td>"
								+ type + "<td>" + dcmObj.nameOf(tag) + " <td> "
								+ value
								+ "</tr>");
			}
			resp.getWriter().write("</table>");

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
	}

}
