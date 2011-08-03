/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import org.psystems.dicom.browser.client.service.ManageStydyServiceAsync;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

/**
 * @author dima_d
 * 
 */
public class IntroPanel extends Composite {

	public IntroPanel() {

		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		HTML intro = new HTML();
		intro.setWidth("800px");
		// intro.setStyleName("DicomItemValue");
		intro
				.setHTML(" <br><p> Добро пожаловать в экспериментальную версию проекта"
						+ " по работе с исследованиями полученных с аппаратов поддерживающих стандарт DICOM </p>"
						+ " <p> Начните свою работу с поиска необходимого вам исследования."
						+ " Просто начните в поисковой строке набирать фамилию пациента и нажмите [enter]."
						+ " В качестве дополнения можно использовать маску 'групповой символ' процент (%) или подчеркивание (_)"
						+ " Для начала поиска можно также нажать кнопу 'Поиск'  </p>"
						+ " <p> Дополнительная информация по проекту"
						+ " <a href='http://code.google.com/p/web-dicom/' target='new'>"
						+ " http://code.google.com/p/web-dicom/</a>"
						+ " (откроется в новом окне) </p>" + "<br><br>");

		mainPanel.add(intro);

		FlexTable statanel = new FlexTable();
		mainPanel.add(statanel);

		Image image = new Image("stat/chart/clientreqs/");
		image.setTitle("Поисковые запросы");
		statanel.setWidget(1, 0, image);
		statanel.getFlexCellFormatter().setColSpan(0, 0, 2);

		image = new Image("stat/chart/dailyload/");
		image.setTitle("Загрузка данных");
		statanel.setWidget(2, 0, image);
		statanel.getFlexCellFormatter().setColSpan(1, 0, 2);

		// image = new Image("stat/chart/usagestore/");
		// image.setTitle("Использование дискового пространства");
		// statanel.setWidget(3,0,image);
		// statanel.getFlexCellFormatter().setColSpan(2, 0, 2);

		
		initWidget(mainPanel);

	}

}
