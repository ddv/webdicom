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
package org.psystems.dicom.browser.server.stat;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.psystems.dicom.browser.server.Util;

public class UseagStoreChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(UseagStoreChartServlet.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();

		JFreeChart chart = getChart();
		int width = 800;
		int height = 400;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);

	}

	public JFreeChart getChart() {

		PreparedStatement psSelect = null;
		
		try {

			Connection connection = Util.getConnection("main",getServletContext());
			long dcmSizes = 0;
			long imgSizes = 0;
			//
			// ALL_IMAGE_SIZE
			// ALL_DCM_SIZE

			// psSelect = connection
			// .prepareStatement("SELECT ID, METRIC_NAME, METRIC_DATE, METRIC_VALUE_LONG "
			// + " FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ?");

			psSelect = connection
					.prepareStatement("SELECT SUM(METRIC_VALUE_LONG) S "
							+ " FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ?");

			psSelect.setString(1, "ALL_DCM_SIZE");
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				dcmSizes = rs.getLong("S");
			}
			rs.close();
			
			psSelect.setString(1, "ALL_IMAGE_SIZE");
			rs = psSelect.executeQuery();
			while (rs.next()) {
				imgSizes = rs.getLong("S");
			}
			rs.close();
			
			
			String dcmRootDir = getServletContext().getInitParameter(
			"webdicom.dir.src");
			long totalSize  = new File(dcmRootDir).getTotalSpace(); //TODO Убрать!
			long freeSize  = new File(dcmRootDir).getFreeSpace();
			long busySize = totalSize - freeSize;
			
//			System.out.println("!!! totalSize=" + totalSize + " freeSize="+freeSize);
			
		
			long diskEmpty = freeSize - imgSizes - dcmSizes;
			
//			double KdiskEmpty = (double)diskEmpty/(double)totalSize;
//			System.out.println("!!! " + KdiskEmpty);
			
			double KdiskBusi = (double)busySize/(double)totalSize * 100;
//			System.out.println("!!! KdiskBusi=" + KdiskBusi);
			
			double KdcmSizes= (double)dcmSizes/(double)totalSize * 100;
//			System.out.println("!!! KdcmSizes=" + KdcmSizes);
			
			double KimgSizes = (double)imgSizes/(double)totalSize * 100;
//			System.out.println("!!! KimgSizes=" + KimgSizes);
			
			double KdiskFree = (double)freeSize/(double)(totalSize  - KdcmSizes - KimgSizes) * 100;
//			System.out.println("!!! KdiskFree=" + KdiskFree);
			
			
			
			DefaultPieDataset dataset = new DefaultPieDataset();
			dataset.setValue("Исследования (DCM-файлы) " + dcmSizes / 1000 + " кб.", KdcmSizes);
			dataset.setValue("Изображения (JPG-файлы) " + imgSizes / 1000 + " кб.", KimgSizes);
			dataset.setValue("Занянто другими " + busySize / 1000 + " кб.", KdiskBusi);
			dataset.setValue("Свободно " + freeSize / 1000 + " кб.", KdiskFree);
			

			boolean legend = true;
			boolean tooltips = false;
			boolean urls = false;

			JFreeChart chart = ChartFactory.createPieChart(
					"Использование дискового пространства на сервере", dataset,
					legend, tooltips, urls);

			chart.setBorderPaint(Color.GREEN);
			chart.setBorderStroke(new BasicStroke(5.0f));
			// chart.setBorderVisible(true);
			// chart.setPadding(new RectangleInsets(20 ,20,20,20));

			
			return chart;

		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		} finally {

			try {
				if (psSelect != null)
					psSelect.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		return null;

	
	}

}
