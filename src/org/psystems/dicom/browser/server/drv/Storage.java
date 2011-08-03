package org.psystems.dicom.browser.server.drv;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.proxy.PatientProxy;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public abstract class Storage {

	private static ArrayList<Storage> drivers = new ArrayList<Storage>();

	// TODO Сделать конфигурабильным (типо загрузка драйвера)
	static {
		drivers.add(new StorageWebDicomImpl());
		drivers.add(new StorageOMITSImpl());
	}

	/**
	 * Метод возврата результатов поиска в выпадающем поисковом списке
	 * 
	 * @param context
	 * @param queryStr
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	public static List<Suggestion> getSearchStudiesSuggestions(ServletContext context,
			String queryStr, int limit) throws SQLException {

		List<Suggestion> result = new ArrayList<Suggestion>();
		result.add(new ItemSuggestion(queryStr + "...", queryStr));
		for (Iterator<Storage> iter = drivers.iterator(); iter.hasNext();) {
			Storage drv = iter.next();
			
			//TODO сделать конфигурабельным перечень драйверов для поиска
			if(drv instanceof StorageWebDicomImpl) {
				result.addAll(drv.getSuggestionsImpl(context, queryStr, limit));
			}
			
		}
		return result;
	}
	
	/**
	 * Метод возврата результатов поиска в выпадающем поисковом списке
	 * 
	 * @param context
	 * @param queryStr
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	public static List<Suggestion> getSearchPatientsSuggestions(ServletContext context,
			String queryStr, int limit) throws SQLException {

		List<Suggestion> result = new ArrayList<Suggestion>();
		result.add(new ItemSuggestion(queryStr + "...", queryStr));
		for (Iterator<Storage> iter = drivers.iterator(); iter.hasNext();) {
			Storage drv = iter.next();
			
			//TODO сделать конфигурабельным перечень драйверов для поиска
			if(drv instanceof StorageOMITSImpl) {
				result.addAll(drv.getSuggestionsImpl(context, queryStr, limit));
			}
		}
		return result;
	}

	/**
	 * Поиск пациентов
	 * 
	 * @param context
	 * @param queryStr
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<PatientProxy> getPatients(ServletContext context,
			String queryStr, int limit) throws SQLException {

		ArrayList<PatientProxy> result = new ArrayList<PatientProxy>();
		for (Iterator<Storage> iter = drivers.iterator(); iter.hasNext();) {
			Storage drv = iter.next();
			
			//TODO сделать конфигурабельным перечень драйверов для поиска
			if(drv instanceof StorageOMITSImpl) {
				result.addAll(drv.getPatientsImpl(context, queryStr, limit));
			}
		}
		return result;
	}

	/**
	 * Возврат элементов в выпадающий поисковый Suggestion-список
	 * 
	 * @param context
	 * @param queryStr
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	public abstract List<Suggestion> getSuggestionsImpl(ServletContext context,
			String queryStr, int limit) throws SQLException;

	/**
	 * @param context
	 * @param queryStr
	 * @param limit
	 * @return
	 * @throws SQLException
	 */
	public abstract List<PatientProxy> getPatientsImpl(ServletContext context,
			String queryStr, int limit) throws SQLException;

}