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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class AttachementServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(AttachementServlet.class
			.getName());

	static {
		// PropertyConfigurator.configure("WEB-INF/log4j.properties");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// String teamColor = getServletConfig().getInitParameter("teamColor");
		// System.out.println("!!! teamColor "+teamColor);

		// System.out.println("!!! prop "+System.getProperty("myapp.notify-url"));
		// System.out.println("!!! end "+System.getenv("DEFAULT_ENCODING_DDV"));

		String path = req.getPathInfo().replaceFirst("/", "");
		String fileName = null;
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("image/jpeg");// По умолчанию

		String imagesRootDir = getServletContext().getInitParameter(
				"webdicom.dir.dst");

		
		int imageId = 0;
		String type = "fullsize";
		
		Matcher matcher = Pattern.compile("^(.*)\\.(.*)$").matcher(path);
		if (matcher.matches()) {
			String id = matcher.group(1);
			type = matcher.group(2);
//			System.out.println("!!! " + id+ "=" + type);
			try {
				imageId = Integer.valueOf(id);
			} catch (NumberFormatException ex) {
				throw new IOException("Image not found! id="+id);
//				fileName = imagesRootDir + File.separator + path;
			}
		}
		
		// Смотрим, если передан Integer, зачит ищем по ID
		
//		try {
//			imageId = Integer.valueOf(path);
//		} catch (NumberFormatException ex) {
//			fileName = imagesRootDir + File.separator + path;
//		}

		PreparedStatement psSelect = null;
		try {

			Connection connection = Util.getConnection("main",getServletContext());

			if (fileName == null) {
				// ищем по ID
				psSelect = connection
						.prepareStatement("SELECT ID, TYPE, DCM_FILE_NAME "
								+ " FROM WEBDICOM.DCMFILE WHERE ID = ? ");
				psSelect.setInt(1, imageId);
			} else {
				psSelect = connection
						.prepareStatement("SELECT ID, TYPE, DCM_FILE_NAME "
								+ " FROM WEBDICOM.DCMFILE WHERE DCM_FILE_NAME = ? ");
				psSelect.setString(1, path);
			}
			ResultSet rs = psSelect.executeQuery();
			int index = 0;
			while (rs.next()) {
				// String contentType = rs.getString("TYPE");
				String file = rs.getString("DCM_FILE_NAME");
				imageId = rs.getInt("ID");
				fileName = imagesRootDir + File.separator + file + ".images" + File.separator
						+type+".jpg";//+ "fullsize.jpg";
				resp.setContentType("image/jpeg");
				index++;
				break;
			}
	
//			System.out.println("fileName="+fileName);
			
			if (index == 0) {
				resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
				resp.sendError(HttpServletResponse.SC_NOT_FOUND,
						"Image not found! id=" + imageId + " file=" + fileName);
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

		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
		} catch (FileNotFoundException ex) {
			resp.setCharacterEncoding("utf-8");// FIXME Не работает!!!
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found! "
					+ fileName);
			ex.printStackTrace();
			return;
		}
		BufferedOutputStream out = new BufferedOutputStream(resp
				.getOutputStream());

		byte b[] = new byte[8];
		int count;
		while ((count = in.read(b)) != -1) {
			out.write(b, 0, count);

		}
		out.flush();
		out.close();
		in.close();
	}

}
