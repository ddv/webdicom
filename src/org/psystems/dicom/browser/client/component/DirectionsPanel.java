/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Date;

import org.psystems.dicom.browser.client.Browser;
import org.psystems.dicom.browser.client.TransactionTimer;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.QueryDirectionProxy;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Панель со списком текущих исследований
 * 
 * @author dima_d
 * 
 */
public class DirectionsPanel extends Composite {

    VerticalPanel resultPanel;
    private DateBox studyDateBoxBegin;
    private long searchTransactionID;
    private Browser Application;
    protected String dateBegin;
    protected String dateEnd;
    private DateBox studyDateBoxEnd;
    protected String manufacturerModelName = null;
    protected String studyResult = "all";

    protected String sortOrder = null;
    public final static int maxResultCount = 300;

    /**
     * @param application
     *            TODO Убрать и вызывать через static методы???
     */
    public DirectionsPanel(Browser application) {

	this.Application = application;

	dateBegin = Utils.dateFormatSql.format(new Date()) + " 00:00:00";
	dateEnd = Utils.dateFormatSql.format(new Date()) + " 23:59:59";
	;

	VerticalPanel mainPanel = new VerticalPanel();

	HorizontalPanel toolPanel = new HorizontalPanel();
	toolPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
	mainPanel.add(toolPanel);

	Label label = new Label("Направления: с ");
	label.addStyleName("DicomItemValue");
	toolPanel.add(label);

	//FIXME !!!!!!!!!!!!!!!!!!!!!!!!! Убать!!!!
//	dateBegin =  "2011-04-01 00:00:00"; 
	
	studyDateBoxBegin = new DateBox();
	studyDateBoxBegin.setWidth("80px");
	studyDateBoxBegin.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	studyDateBoxBegin.setValue(new Date());
	

	studyDateBoxBegin.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		dateBegin = Utils.dateFormatSql.format(Utils.dateFormatUser.parse(event.getValue())) + " 00:00:00";
		queryDirections();
	    }
	});

	studyDateBoxBegin.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Date> event) {
		dateBegin = Utils.dateFormatSql.format(event.getValue()) + " 00:00:00";
		queryDirections();
	    }
	});

	toolPanel.add(studyDateBoxBegin);
	

	label = new Label(" по ");
	label.addStyleName("DicomItemValue");
	toolPanel.add(label);

	studyDateBoxEnd = new DateBox();
	studyDateBoxEnd.setWidth("80px");
	studyDateBoxEnd.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	studyDateBoxEnd.setValue(new Date());

	
	studyDateBoxEnd.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<String> event) {
		dateEnd = Utils.dateFormatSql.format(Utils.dateFormatUser.parse(event.getValue())) + " 00:00:00";
		queryDirections();
	    }
	});

	studyDateBoxEnd.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Date> event) {
		dateEnd = Utils.dateFormatSql.format(event.getValue()) + " 00:00:00";
		queryDirections();
	    }
	});

	toolPanel.add(studyDateBoxEnd);

	label = new Label("аппарат:");
	label.addStyleName("DicomItemValue");
	toolPanel.add(label);

	//
	final ListBox lbManufacturerModelName = new ListBox();
	// lbCommentsTemplates.setName("00100040");
	lbManufacturerModelName.addItem("Все", "");
	lbManufacturerModelName.addItem("RENEXFLUORO3", "RENEXFLUORO3");
	lbManufacturerModelName.addItem("КРТ-Электрон", "КРТ-Электрон");
	lbManufacturerModelName.addItem("Маммограф (LORAD AFFINITY)", "LORAD AFFINITY");
	lbManufacturerModelName.addItem("Эндоскоп (FUJINON EG WR5)", "FUJINON EG WR5");
	lbManufacturerModelName.addItem("Рентген (CLINOMAT)", "CLINOMAT");
	lbManufacturerModelName.addItem("Рентген (РДК 50/6)", "РДК 50/6");
	lbManufacturerModelName.addItem("Рентген (DUODiagnost)", "DUODiagnost");

	lbManufacturerModelName.addItem("УЗИ-1 (SonoScape) ДП 523 каб.", "SonoScape-1");
	lbManufacturerModelName.addItem("УЗИ-2 (SonoScape)", "SonoScape-2");
	lbManufacturerModelName.addItem("УЗИ-3 (SonoScape)", "SonoScape-3");
	lbManufacturerModelName.addItem("УЗИ (VIVID) ДП 527 каб.", "VIVID 3");
	lbManufacturerModelName.addItem("VOLUSON 730 BT04 EXPERT", "УЗИ (VOLUSON)");
	lbManufacturerModelName.addItem("УЗИ (Acuson Sequoia) каб.408", "Acuson Sequoia");

	// TODO Уточнить имена и кабинеты
	lbManufacturerModelName.addItem("SSI-1000");
	lbManufacturerModelName.addItem("Эхоэнцефалоскоп-ЭЭС-25-ЭМА");
	lbManufacturerModelName.addItem("Электроэнцефалограф-Alliance");
	lbManufacturerModelName.addItem("Companion III");
	lbManufacturerModelName.addItem("Microvit");
	lbManufacturerModelName.addItem("СМАД, Schiller AG");
	lbManufacturerModelName.addItem("ЭКГ-Schiller Medical S.A.");
	lbManufacturerModelName.addItem("ЭКГ-Cardiovit AT-2 plus C");
	lbManufacturerModelName.addItem("Велоэргометрия-АТ- 104 Schiller");
	lbManufacturerModelName.addItem("Voluson 730 Expert");
	lbManufacturerModelName.addItem("SSD-3500");
	lbManufacturerModelName.addItem("Спирометр-Spirovit SP-1");
	lbManufacturerModelName.addItem("Спиро-Спектр 2");

	lbManufacturerModelName.addItem("Aloka alfa");

	lbManufacturerModelName.addChangeHandler(new ChangeHandler() {

	    @Override
	    public void onChange(ChangeEvent event) {
		int i = lbManufacturerModelName.getSelectedIndex();
		manufacturerModelName = lbManufacturerModelName.getValue(i);
		if (manufacturerModelName != null && manufacturerModelName.length() == 0) {
		    manufacturerModelName = null;
		}
		queryDirections();
	    }
	});

	toolPanel.add(lbManufacturerModelName);

	label = new Label("максимум - " + maxResultCount);
	label.addStyleName("DicomItemValue");
	toolPanel.add(label);

	resultPanel = new VerticalPanel();
	mainPanel.add(resultPanel);
	// TODO Убрать в css
	resultPanel.setSpacing(10);
	DOM.setStyleAttribute(resultPanel.getElement(), "background", "#E9EDF5");
	resultPanel.setWidth("100%");

	initWidget(mainPanel);
	// TODO Убрать в css
	setWidth("100%");
	queryDirections();

    }

    public void clear() {
	resultPanel.clear();
    }

    public void add(Widget w) {
	resultPanel.add(w);
    }

    /**
     * Поиск напрвлений
     */
    private void queryDirections() {

	Date d = new Date();
	searchTransactionID = d.getTime();
	Application.showWorkStatusMsg("");

	// resultPanel.clear();

	TransactionTimer t = new TransactionTimer() {

	    private int counter = 0;

	    public void run() {

		if (getTransactionId() != searchTransactionID) {
		    cancel();
		    return;
		}

		if (counter == 0) {

		    Button b = new Button("Остановить поиск");
		    b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			    transactionInterrupt();
			}
		    });

		    // TODO Вынести логику в Application
		    Application.addToWorkStatusWidget(b);
		    // TODO Вынести логику в Application
		    Application
			    .addToWorkStatusMsg(" Возможно имеется <i>проблема</i> со связью. Вы <b>всегда</b> можете остановить поиск...");
		}
		counter++;

		Application.addToWorkStatusMsg(" Поиск продолжается " + counter * 2 + " сек.");

	    }
	};
	t.setTransactionId(searchTransactionID);
	t.scheduleRepeating(3000);

	transactionStarted();

	QueryDirectionProxy query = new QueryDirectionProxy();
	query.setManufacturerDevice(manufacturerModelName);
	query.setDateTimePlannedBegin(dateBegin);
	query.setDateTimePlannedEnd(dateEnd);

	Application.browserService.getDirections(query, new AsyncCallback<ArrayList<DirectionProxy>>() {

	    @Override
	    public void onFailure(Throwable caught) {
		transactionFinished();
		Application.showErrorDlg(caught);

	    }

	    @Override
	    public void onSuccess(ArrayList<DirectionProxy> result) {

		Application.hideWorkStatusMsg();

		Label l = new Label("Количество Направлений: " + result.size());
		l.addStyleName("DicomItem");
		resultPanel.add(l);

		for (DirectionProxy directionProxy : result) {

		    DirectionCard drn = new DirectionCard(directionProxy);
		    resultPanel.add(drn);

		}

		if (result.size() >= maxResultCount) {
		    HTML emptyStr = new HTML();
		    emptyStr.setWidth("900px");
		    emptyStr.setStyleName("DicomItemValue");
		    emptyStr.setHTML("Показаны только первые " + maxResultCount
			    + " строк! Чтобы посмотреть все - сужайте критерий поиска.");

		    resultPanel.add(emptyStr);
		}

		if (result.size() == 0) {
		    showNotFound();
		}

		transactionFinished();

	    }
	});

    }

    /**
     * старт транзакции
     */
    private void transactionStarted() {
	resultPanel.clear();
	// sendButton.setEnabled(false);
	// clearButton.setEnabled(false);
    }

    /**
     * Завершение транзакции
     */
    private void transactionFinished() {

	Application.hideWorkStatusMsg();
	// sendButton.setEnabled(true);
	// clearButton.setEnabled(true);
	// nameField.setFocus(true);
	searchTransactionID = new Date().getTime();
    }

    /**
     * Прерывание транзакции
     */
    private void transactionInterrupt() {
	transactionFinished();
    }

    protected void showNotFound() {
	HTML emptyStr = new HTML();
	emptyStr.setWidth("400px");
	// emptyStr.setStyleName("DicomItemValue");
	emptyStr.setHTML("Ничего не найдено... Попробуйте выбрать другие даты...");

	resultPanel.add(emptyStr);

	// emptyStr = new HTML();
	// emptyStr.setWidth("800px");
	// emptyStr
	// .setHTML(
	//
	// " <p> Попробуйте выбрать другую дату... </p>");
	//
	// resultPanel.add(emptyStr);
    }
}
