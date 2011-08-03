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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.commons.orm.ORMUtil;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author dima_d
 * 
 *         Драйвер для работы с БД ОМИТЦ
 * 
 */
public class StorageOMITSImpl extends Storage {

	private static Logger logger = Logger.getLogger(StorageOMITSImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.psystems.dicom.browser.server.StorageDrv#getSuggestions(javax.servlet
	 * .ServletContext, java.lang.String, int)
	 */
	@Override
	public List<Suggestion> getSuggestionsImpl(ServletContext context,
			String queryStr, int limit) throws SQLException {

		List<Suggestion> suggestions = new ArrayList<Suggestion>(limit);
		
		//если < 3-х символов
		if(queryStr.length()<3) return suggestions;
		
		PreparedStatement psSelect = null;
		Connection connection = null;

		try {

			connection = org.psystems.dicom.browser.server.Util
					.getConnection("omits", context);
			
			String where = "UPPER(SUR_NAME || ' ' || FIRST_NAME || ' ' || PATR_NAME) like UPPER(? || '%') AND CODE like substr(?, 1, 3) || '%' ";
			
			//Если поиск по КБП
			if(queryStr.matches("^\\D{5}\\d{2}$")) {
				where = " CODE = UPPER(?)  AND CODE like substr(?, 1, 3) || '%' ";
			}

			psSelect = connection
					.prepareStatement("select v.ID, v.FIRST_NAME, v.SUR_NAME, v.PATR_NAME, v.CODE, v.BIRTHDAY, v.SEX "
							+ " from  lpu.v_patient v "
							+ "WHERE " + where
							+ " order by SUR_NAME ");

			psSelect.setString(1, queryStr.toUpperCase());
			psSelect.setString(2, queryStr.substring(0, 2).toUpperCase());
			ResultSet rs = psSelect.executeQuery();
			int index = 0;

			while (rs.next()) {

				String name = rs.getString("SUR_NAME") + " "
						+ rs.getString("FIRST_NAME") + " "
						+ rs.getString("PATR_NAME");
				String date = "" + rs.getDate("BIRTHDAY");
				suggestions.add(new ItemSuggestion(name + " (" + date + ")",
						name));

				if (index++ > limit) {
					break;
				}

			}
			rs.close();
			

		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
				if(connection!=null)
					connection.close();
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
		

		ArrayList<PatientProxy> result = new ArrayList<PatientProxy>();
		//если < 3-х символов
		if(queryStr.length()<3) return result;
		PreparedStatement psSelect = null;
		Connection connection = null;

		try {

			connection = org.psystems.dicom.browser.server.Util
					.getConnection("omits", context);
			
			String where = "UPPER(SUR_NAME || ' ' || FIRST_NAME || ' ' || PATR_NAME) like UPPER(? || '%') AND CODE like substr(?, 1, 3) || '%' ";
			
			if(queryStr.matches("^\\D{5}\\d{2}$")) {
				where = " CODE = UPPER(?) AND CODE like substr(?, 1, 3) || '%' ";
			}

			psSelect = connection
					.prepareStatement("select v.ID, v.FIRST_NAME, v.SUR_NAME, v.PATR_NAME, v.CODE, v.BIRTHDAY, v.SEX "
							+ " from  lpu.v_patient v "
							+ "WHERE " + where
							+ " order by SUR_NAME ");

			psSelect.setString(1, queryStr.toUpperCase());
			psSelect.setString(2, queryStr.substring(0, 2).toUpperCase());
			ResultSet rs = psSelect.executeQuery();
			int index = 0;

			while (rs.next()) {

				String name = rs.getString("SUR_NAME") + " "
						+ rs.getString("FIRST_NAME") + " "
						+ rs.getString("PATR_NAME");

				PatientProxy proxy = new PatientProxy();
				proxy.setId(rs.getLong("ID"));
				proxy.setPatientName(name);
				proxy.setPatientSex(rs.getString("SEX"));
				proxy.setPatientBirthDate(ORMUtil.dateFormatSQL.format(rs.getDate("BIRTHDAY")));
				result.add(proxy);

				if (index++ > limit) {
					break;
				}
				

			}
			rs.close();

		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
				if(connection!=null)
					connection.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		return result;

	}

}