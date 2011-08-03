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
package org.psystems.dicom.browser.server.drv;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.proxy.PatientProxy;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author dima_d
 * 
 * Основной драйвер
 *
 */
public class StorageWebDicomImpl extends Storage {

	private static Logger logger = Logger.getLogger(Storage.class.getName());

	/* (non-Javadoc)
	 * @see org.psystems.dicom.browser.server.StorageDrv#getSuggestions(javax.servlet.ServletContext, java.lang.String, int)
	 */
	@Override
	public List<Suggestion> getSuggestionsImpl(ServletContext context,
			String queryStr, int limit) throws SQLException {

		List<Suggestion> suggestions = new ArrayList<Suggestion>(limit);
		PreparedStatement psSelect = null;

		try {

			Connection connection = org.psystems.dicom.browser.server.Util
					.getConnection("main", context);
			
			String where = " UPPER(PATIENT_NAME) like UPPER(? || '%')";
			
			boolean codeSearch = false; 
				
			//Если поиск по КБП
			if(queryStr.matches("^\\D{5}\\d{2}$")) {
				where = " PATIENT_SHORTNAME = UPPER(?) ";
				codeSearch = true;
			}
			
//			Артемьева Наталья Александровна 14.12.1978
			
			Matcher matcher = Pattern.compile("^\\s{0,}(\\D+\\s+\\D+\\s+\\D+)\\s(\\d{1,2})\\.(\\d{1,2})\\.(\\d{4})\\s{0,}$").matcher(queryStr);
			boolean fullSearch = matcher.matches();
			
			
			
			String fio = null,day = null,month = null,year = null;
			if (fullSearch) {
				fio = matcher.group(1);
				day = matcher.group(2);
				month = matcher.group(3);
				year = matcher.group(4);
				where = " UPPER(PATIENT_NAME) = UPPER(?) AND PATIENT_BIRTH_DATE = ?";
			}


			psSelect = connection
					.prepareStatement("SELECT ID, PATIENT_NAME, PATIENT_BIRTH_DATE "
							+ " FROM WEBDICOM.STUDY WHERE "
							+ where
							+ " order by PATIENT_NAME ");

			if(fullSearch) {
				psSelect.setString(1, fio);
				psSelect.setDate(2, java.sql.Date.valueOf(year+"-"+month+"-"+day));
			} else {
				psSelect.setString(1, queryStr);
			}
			
			
			ResultSet rs = psSelect.executeQuery();
			int index = 0;
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			while (rs.next()) {

				String name = rs.getString("PATIENT_NAME");
				String date = format.format(rs.getDate("PATIENT_BIRTH_DATE"));
				
				String msgAddon = "";
				if(fullSearch) {
					msgAddon = "(поиск по ФИО+ДР)";
				}
				if(codeSearch) {
					msgAddon = "(поиск по КБП)";
				}
				
				
				suggestions.add(new ItemSuggestion(name + " " + date + " " + msgAddon,
						name + " " + date));

				if (index++ > limit) {
					break;
				}

			}
			rs.close();

		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
				// throw new DefaultGWTRPCException(e.getMessage());
			}
		}

		return suggestions;
	}

	@Override
	public List<PatientProxy> getPatientsImpl(ServletContext context,
			String queryStr, int limit) throws SQLException {
		// TODO Auto-generated method stub
		return new ArrayList<PatientProxy>();
	}

	

}