package org.psystems.dicom.browser.client.component;

import com.google.gwt.i18n.client.DateTimeFormat;

public class Utils {

	public static DateTimeFormat dateFormatUser = DateTimeFormat
			.getFormat("dd.MM.yyyy");
	public static DateTimeFormat dateFormatDicom = DateTimeFormat
			.getFormat("yyyyMMdd");
	public static DateTimeFormat dateFormatSql = DateTimeFormat
			.getFormat("yyyy-MM-dd");
	public static DateTimeFormat dateTimeFormatSql = DateTimeFormat
			.getFormat("yyyy-MM-dd HH:mm:ss");
	public static DateTimeFormat dateTimeFormatUser = DateTimeFormat
			.getFormat("dd.MM.yyyy HH:mm:ss");
	public static DateTimeFormat dateFormatYEARONLY = DateTimeFormat
			.getFormat("yyyy");

	/**
	 * Вывод коллекции в строку
	 * 
	 * @param a
	 * @return
	 */
	public static String collectionToString(Object[] a) {
		if (a == null)
			return "null";
		int iMax = a.length - 1;
		if (iMax == -1)
			return "[]";

		StringBuilder b = new StringBuilder();
		b.append('[');
		for (int i = 0;; i++) {
			b.append(String.valueOf(a[i]));
			if (i == iMax)
				return b.append(']').toString();
			b.append(", ");
		}
	}

}
