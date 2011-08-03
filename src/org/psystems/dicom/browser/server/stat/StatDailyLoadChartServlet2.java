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

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarPainter;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.psystems.dicom.browser.server.Util;

public class StatDailyLoadChartServlet2 extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Font labelFont = new Font("Helvetica", Font.PLAIN, 20);
	private static Font dateFont = new Font("Verdana", Font.PLAIN, 9);
	
	private static Logger logger = Logger
			.getLogger(StatDailyLoadChartServlet2.class);

	private Paint color1 = new Color(68,99,156);
	private Paint color2 = new Color(176,202,250);
	
	

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		response.setContentType("image/png");

		OutputStream outputStream = response.getOutputStream();

		CategoryDataset dataset = createDataset();
		JFreeChart chart = getChart(dataset);
		int width = 800;
		int height = 250;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);

	}

	
	
	private JFreeChart getChart(CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createStackedBarChart(
            "Stacked Bar Chart Demo 4",  // chart title
            "Category",                  // domain axis label
            "",                     // range axis label
            dataset,                     // data
            PlotOrientation.VERTICAL,    // the plot orientation
            true,                        // legend
            true,                        // tooltips
            false                        // urls
        );
        
        
        TextTitle title =  new TextTitle("Динамика загрузки данных (кб)",labelFont);
		title.setPaint(new Color(68,99,156));
		chart.setTitle(title);
		
		
		
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap("G1");
        map.mapKeyToGroup("product 1 (US)", "G1");
        map.mapKeyToGroup("product 1 (Europe)", "G1");
   
        renderer.setSeriesToGroupMap(map); 
        
        
        BarPainter b = new StandardBarPainter();
//        BarPainter b = new GradientBarPainter(0,0.9,0.9);
        
        renderer.setBarPainter(b);
        
        renderer.setSeriesPaint(0, color1 );
        renderer.setSeriesPaint(1, color2 ); 
        
       
        
        SubCategoryAxis domainAxis = new SubCategoryAxis("DCM файлы / JPG файлы");
        domainAxis.setCategoryMargin(0.05);
        domainAxis.setTickLabelFont(dateFont);
//        domainAxis.addSubCategory("Product 1");
//        domainAxis.addSubCategory("Product 2");
//        domainAxis.addSubCategory("Product 3");
  
        
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainAxis(domainAxis);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.red);
		plot.setRangeGridlinePaint(Color.lightGray);
		
		ValueAxis v = plot.getRangeAxis();
		v.setTickLabelFont(dateFont);
//		plot.setRangeAxis(ValueAxis);
        
        //plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        plot.setRenderer(renderer);
        plot.setFixedLegendItems(createLegendItems());
        return chart;
        
    }

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        Calendar calendarEnd = Calendar.getInstance();
		int tzoffset = calendarEnd.getTimeZone().getOffset(
				calendarEnd.getTimeInMillis());

		long timeEnd = calendarEnd.getTimeInMillis();
		timeEnd = timeEnd - (timeEnd % (60 * 60 * 24 * 1000)) - tzoffset;
		calendarEnd.setTimeInMillis(timeEnd);
		
		
        
    	try {
			Connection connection = Util.getConnection("main",getServletContext());

			Calendar calendarBegin = (Calendar) calendarEnd.clone();
			calendarBegin.add(Calendar.DAY_OF_MONTH, -7);

			getMetrics(connection, "Исследования (DCM-файлы)", "ALL_DCM_SIZE",
					calendarBegin, calendarEnd, dataset);

			getMetrics(connection, "Изображения (JPG-файлы)", "ALL_IMAGE_SIZE",
					calendarBegin, calendarEnd, dataset);

		} catch (SQLException e) {
			logger.error(e);
			e.printStackTrace();
		}

//        result.addValue(20.3, "product 1 (US)", "01.01.2010");
//        result.addValue(27.2, "product 1 (US)", "02.01.2010");
//        result.addValue(19.7, "product 1 (US)", "03.01.2010");
//        result.addValue(19.7, "product 1 (US)", "04.01.2010");
//        result.addValue(19.7, "product 1 (US)", "05.01.2010");
//        result.addValue(19.7, "product 1 (US)", "06.01.2010");
//        result.addValue(19.7, "product 1 (US)", "07.01.2010");
//        
//        result.addValue(19.4, "product 1 (Europe)", "01.01.2010");
//        result.addValue(10.9, "product 1 (Europe)", "02.01.2010");
//        result.addValue(18.4, "product 1 (Europe)", "03.01.2010");
//        result.addValue(19.7, "product 1 (Europe)", "04.01.2010");
//        result.addValue(19.7, "product 1 (Europe)", "05.01.2010");
//        result.addValue(19.7, "product 1 (Europe)", "06.01.2010");
//        result.addValue(19.7, "product 1 (Europe)", "07.01.2010");
        

   

        return dataset;
    }
	
	private LegendItemCollection createLegendItems() {
        LegendItemCollection result = new LegendItemCollection();
        LegendItem item1 = new LegendItem("DCM файлы", color1);
        LegendItem item2 = new LegendItem("JPG файлы", color2);

        result.add(item1);
        result.add(item2);
      
        return result;
    }

	/**
	 * @param connection
	 * @param series
	 * @param metrica
	 * @param timeBegin
	 * @param timeEnd
	 * @param dataset
	 * @throws SQLException
	 */
	private void getMetrics(Connection connection, String series,
			String metrica, Calendar calendarBegin, Calendar calendarEnd,
			DefaultCategoryDataset dataset) throws SQLException {

		PreparedStatement stmt = null;
		try {

			
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
			
			Calendar calendar1 = (Calendar) calendarBegin.clone();
			String dateStr = format.format(calendar1.getTime());
			dataset.addValue(0, series, dateStr);
			for(int i=0; i<6; i++) {
				calendar1.add(Calendar.DAY_OF_MONTH, 1);
				dateStr = format.format(calendar1.getTime());
				dataset.addValue(0, series, dateStr);
			}

			stmt = connection
					.prepareStatement("SELECT METRIC_VALUE_LONG, METRIC_DATE"
							+ " FROM WEBDICOM.DAYSTAT WHERE METRIC_NAME = ? and "
							+ " METRIC_DATE BETWEEN ? AND ? ");

			stmt.setString(1, metrica);
			stmt.setDate(2, new java.sql.Date(calendarBegin.getTimeInMillis()));
			stmt.setDate(3, new java.sql.Date(calendarEnd.getTimeInMillis()));
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				long value = rs.getLong("METRIC_VALUE_LONG") / 1000;
				Date date = rs.getDate("METRIC_DATE");
				dateStr = format.format(date.getTime());
				String category = dateStr;
				dataset.addValue(value, series, category);
//				System.out.println(value + " = " + series + " = "+ category);
			}
			rs.close();

		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

}
