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
package org.psystems.dicom.browser.client;

import org.psystems.dicom.browser.client.component.DirectionsPanel;
import org.psystems.dicom.browser.client.component.HeaderPanel;
import org.psystems.dicom.browser.client.component.IntroPanel;
import org.psystems.dicom.browser.client.component.SearchPanel;
import org.psystems.dicom.browser.client.component.SearchResultPanel;
import org.psystems.dicom.browser.client.component.WorkListPanel;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.service.BrowserService;
import org.psystems.dicom.browser.client.service.BrowserServiceAsync;
import org.psystems.dicom.browser.client.service.ManageStydyService;
import org.psystems.dicom.browser.client.service.ManageStydyServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Browser implements EntryPoint {

	// Версия ПО (используется для проверки на стороне сервере при обновлении
	// клиента)
	//TODO !!!! Пернести в конфиг !!!!!
	//TODO В классе ARPCRequest тоже есть эта переменная !!!
	public static String version = "0.1a";
	


	// Create a remote service proxy
	public final static BrowserServiceAsync browserService = GWT
			.create(BrowserService.class);

	public static final ManageStydyServiceAsync manageStudyService = GWT
			.create(ManageStydyService.class);

	
	private static DialogBox errorDialogBox;
	private static HTML errorResponseLabel;

	// панель состояния работы запросов
	private static PopupPanel workStatusPopup;
	private static FlowPanel workStatusPanel;

	// private VerticalPanel bodyPanel;

	public boolean showPageIntro = true;// Показ страницы с приглашением

	private SearchPanel searchPanel;



	private static Label errorResponseMsg;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		_workStatusPopup();
		createErorrDlg();

		HeaderPanel headerPanel = new HeaderPanel();
		RootPanel.get("headerContainer").add(headerPanel);

		searchPanel = new SearchPanel(this);
		RootPanel.get("searchContainer").add(searchPanel);

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				if (event.getValue().equals("")) {

					RootPanel.get("bodyContainer").clear();
					SearchResultPanel searchResultPanel = new SearchResultPanel();
					RootPanel.get("bodyContainer").add(searchResultPanel);
					searchPanel.setResultPanel(searchResultPanel);
					searchPanel.setType("study");

				} else if (event.getValue().equals("newstudy")) {

					RootPanel.get("bodyContainer").clear();

					// StudyManagePanel panel = new StudyManagePanel(
					// manageStudyService,browserService,null);
					// RootPanel.get("bodyContainer").add(panel);
					SearchResultPanel searchResultPanel = new SearchResultPanel();
					RootPanel.get("bodyContainer").add(searchResultPanel);
					searchPanel.setResultPanel(searchResultPanel);

					searchPanel.setType("patient");

				} else if (event.getValue().equals("showintro")) {

					RootPanel.get("bodyContainer").clear();
					IntroPanel intro = new IntroPanel();
					RootPanel.get("bodyContainer").add(intro);

				} else if (event.getValue().equals("workliststudy")) {

					RootPanel.get("bodyContainer").clear();
					WorkListPanel wlpanel = new WorkListPanel(
							Browser.this);
					RootPanel.get("bodyContainer").add(wlpanel);

				} else if (event.getValue().equals("directions")) {

					RootPanel.get("bodyContainer").clear();
					DirectionsPanel drnpanel = new DirectionsPanel(
							Browser.this);
					RootPanel.get("bodyContainer").add(drnpanel);

				}
			}
		});

		History.fireCurrentHistoryState();

	}
	
    /**
     * Диалог выдачи сообщения об ошибке
     */
    private void createErorrDlg() {
	
	errorDialogBox = new DialogBox();
	errorDialogBox.setText("Ошибка!");
	errorDialogBox.setAnimationEnabled(true);
	final Button closeButton = new Button("Закрыть окно сообщения");
	// We can set the id of a widget by accessing its Element
	closeButton.getElement().setId("closeButton");
	errorResponseMsg = new Label();
	errorResponseLabel = new HTML();
	errorResponseLabel.setVisible(false);
	VerticalPanel dialogVPanel = new VerticalPanel();
	dialogVPanel.addStyleName("dialogVPanel");

	dialogVPanel.add(closeButton);
	dialogVPanel.add(new HTML("<b>Сообщение:</b>"));
	dialogVPanel.add(errorResponseMsg);

	Button showtraceBtn = new Button("Показать/скрыть подробности");
	showtraceBtn.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub
		if (errorResponseLabel.isVisible())
		    errorResponseLabel.setVisible(false);
		else
		    errorResponseLabel.setVisible(true);
		
		errorDialogBox.center();
	    }
	});

	dialogVPanel.add(showtraceBtn);

	dialogVPanel.add(errorResponseLabel);
	dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

	errorDialogBox.setWidget(dialogVPanel);

	// Add a handler to close the DialogBox
	closeButton.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {
		errorDialogBox.hide();
	    }
	});

    }

	/**
	 * Выдача сообщения об ошибке
	 * @param e
	 */
	public static void showErrorDlg(Throwable e) {

		if(e instanceof DefaultGWTRPCException) {
			DefaultGWTRPCException ex = (DefaultGWTRPCException)e;
			errorResponseMsg.setText(e.getMessage());
			errorResponseLabel.setHTML(ex.getMessage()+" <br><pre>Ошибка [" + ex.getLogMarker()+ "]\n"+ ex.getStack()+"</pre>");
		} else {
		    errorResponseMsg.setText(e.getMessage());
		}
		errorDialogBox.show();
		errorDialogBox.center();
	}

	

	/**
	 * создание диалога состояния поцесса работы
	 */
	private void _workStatusPopup() {

		workStatusPopup = new PopupPanel();
		workStatusPopup.hide();
		workStatusPopup.setStyleName("msgPopupPanel");
		// workStatusPanel.setAnimationEnabled(false);
		workStatusPanel = new FlowPanel();
		// workMsg = new HTML("");
		workStatusPanel.addStyleName("msgPopupPanelItem");
		workStatusPopup.add(workStatusPanel);
	}

	/**
	 * Показ панели состояния процесса
	 * 
	 * @param html
	 *            HTML сообщение
	 */
	public void showWorkStatusMsg(String html) {

		workStatusPanel.add(new HTML(html));
		workStatusPopuppopupCentering();
	}

	/**
	 * Центровка сообщения
	 */
	private void workStatusPopuppopupCentering() {
		workStatusPopup.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {

				workStatusPopup.setPopupPosition(offsetWidth, offsetHeight);
				int left = (Window.getClientWidth() - offsetWidth) >> 1;
				int top = 0;

				workStatusPopup.setPopupPosition(Window.getScrollLeft() + left,
						Window.getScrollTop() + top);
			}

		});

	}

	/**
	 * Добавление в сообщение строки
	 * 
	 * @param html
	 */
	public void addToWorkStatusMsg(String html) {
		workStatusPanel.add(new HTML(html));
		workStatusPopuppopupCentering();
	}

	/**
	 * Добавление в сообщение виджета
	 * 
	 * @param html
	 */
	public void addToWorkStatusWidget(Widget widget) {
		workStatusPanel.add(widget);
		workStatusPopuppopupCentering();
	}

	/**
	 * Скрытие панели состояния процесса
	 */
	public static void hideWorkStatusMsg() {
		workStatusPanel.clear();
		workStatusPopup.hide();
	}

}
