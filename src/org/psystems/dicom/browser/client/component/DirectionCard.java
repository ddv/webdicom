package org.psystems.dicom.browser.client.component;

import java.util.Date;

import org.psystems.dicom.browser.client.Browser;
import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.proxy.DirectionProxy;
import org.psystems.dicom.browser.client.proxy.EmployeeProxy;
import org.psystems.dicom.browser.client.proxy.ManufacturerDeviceProxy;
import org.psystems.dicom.browser.client.proxy.PatientProxy;
import org.psystems.dicom.browser.client.proxy.StudyProxy;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Направление
 * 
 * @author dima_d
 * 
 */
public class DirectionCard extends Composite {

    private DirectionProxy drnProxy;
    private VerticalPanel mainPanel;
    protected StudyManagePanel studyManagePanel;
    private HorizontalPanel allStuduesPanel;
    private FlexTable ftEdit;

    // private StudyManagePanel studyManagePanel;

    public DirectionCard(DirectionProxy drnProxy) {
	this.drnProxy = drnProxy;

	mainPanel = new VerticalPanel();

	HorizontalPanel hp = new HorizontalPanel();
	mainPanel.add(hp);

	String sex = "М";
	if ("F".equals(drnProxy.getPatient().getPatientSex())) {
	    sex = "Ж";
	}

	String date = Utils.dateFormatUser.format(Utils.dateFormatSql
		.parse(drnProxy.getPatient().getPatientBirthDate()));
	Label labelPatient = new Label(drnProxy.getPatient().getPatientName() + " (" + sex + ") " + date + " - "
		+ drnProxy.getDoctorDirect().getEmployeeName() + " [" + drnProxy.getId() + "]");
	labelPatient.setStyleName("DicomItem");

	hp.add(labelPatient);

	labelPatient.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {

		showForm();

	    }

	});

	Label lblEdit = new Label(" {редактировать}");
	hp.add(lblEdit);
	lblEdit.setStyleName("DicomItem");
	lblEdit.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		showDirectionForEdit();
	    }
	});

	

	initWidget(mainPanel);
    }

    /**
     * Открытие направления на редкатирование
     */
    private void showDirectionForEdit() {

	if (ftEdit != null && ftEdit.isAttached()) {
	    ftEdit.removeFromParent();
	    ftEdit = null;
	    return;
	}

	ftEdit = new FlexTable();
	mainPanel.add(ftEdit);

	int row = 0;

	// Направление
	makeField(ftEdit, row, 0, "Направление:", new Label(drnProxy.getDirectionId() + "(" + drnProxy.getId() + ")"));

	// Аппарат
	final DicSuggestBox deviceDirrectedBox = new DicSuggestBox("devices");
	if (drnProxy.getDevice() != null)
	    deviceDirrectedBox.getSuggestBox().setText(drnProxy.getDevice().getManufacturerModelName());

	deviceDirrectedBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		ManufacturerDeviceProxy dev = (ManufacturerDeviceProxy) item.getEvent();
		drnProxy.setDevice(dev);
	    }
	});
	deviceDirrectedBox.getSuggestBox().getTextBox().addBlurHandler(new BlurHandler() {

	    @Override
	    public void onBlur(BlurEvent event) {
		if (drnProxy.getDevice() != null)
		    deviceDirrectedBox.getSuggestBox().setText(drnProxy.getDevice().getManufacturerModelName());
		else
		    deviceDirrectedBox.getSuggestBox().setText("");
	    }
	});

	makeField(ftEdit, row, 2, "Аппарат:", deviceDirrectedBox);

	// Размещение
	final TextBox directionLoction = new TextBox();
	directionLoction.setText(drnProxy.getDirectionLocation());
	directionLoction.addChangeHandler(new ChangeHandler() {

	    @Override
	    public void onChange(ChangeEvent event) {
		drnProxy.setDirectionLocation(directionLoction.getText());
	    }
	});
	makeField(ftEdit, row, 4, "Размещение:", directionLoction);

	row++;

	// Направивший врач
	final DicSuggestBox doctorDirrectedBox = new DicSuggestBox("doctors");
	if (drnProxy.getDoctorDirect() != null)
	    doctorDirrectedBox.getSuggestBox().setText(drnProxy.getDoctorDirect().getEmployeeName());

	doctorDirrectedBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		EmployeeProxy doctor = (EmployeeProxy) item.getEvent();
		drnProxy.setDoctorDirect(doctor);
	    }
	});
	doctorDirrectedBox.getSuggestBox().getTextBox().addBlurHandler(new BlurHandler() {

	    @Override
	    public void onBlur(BlurEvent event) {
		if (drnProxy.getDoctorDirect() != null)
		    doctorDirrectedBox.getSuggestBox().setText(drnProxy.getDoctorDirect().getEmployeeName());
		else
		    doctorDirrectedBox.getSuggestBox().setText("");
	    }
	});
	makeField(ftEdit, row, 0, "Направил:", doctorDirrectedBox);

	// Дата направления
	DateBox dateDirection = new DateBox();
	dateDirection.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	dateDirection.setWidth("6em");

	dateDirection.addValueChangeHandler(new ValueChangeHandler<Date>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Date> event) {
		String d = Utils.dateFormatSql.format(event.getValue());
		drnProxy.setDateDirection(d);
	    }
	});

	dateDirection.setValue(Utils.dateFormatSql.parse(drnProxy.getDateDirection()));
	makeField(ftEdit, row, 2, "Направлен:", dateDirection);

	// Случай
	final TextBox directionCode = new TextBox();
	// directionCode.setWidth("6em");
	directionCode.setText(drnProxy.getDirectionCode());
	directionCode.addChangeHandler(new ChangeHandler() {

	    @Override
	    public void onChange(ChangeEvent event) {
		// TODO Auto-generated method stub
		drnProxy.setDirectionCode(directionCode.getText());
	    }
	});

	makeField(ftEdit, row, 4, "Случай:", directionCode);

	row++;

	// Направленные диагнозы
	final DiagnosisPanel diagnosisDirrectPanel = new DiagnosisPanel(true);
	diagnosisDirrectPanel.setDiagnosis(drnProxy.getDiagnosisDirect());
	row = makeFieldSimple(ftEdit, row++, "Направленные диагнозы:", diagnosisDirrectPanel);

	// Направленные услуги
	final ServicePanel servicesDirrectPanel = new ServicePanel(true);
	servicesDirrectPanel.setServices(drnProxy.getServicesDirect());
	row = makeFieldSimple(ftEdit, row++, "Направленные услуги:", servicesDirrectPanel);

	// Принимающий врач
	final DicSuggestBox doctorPerformedBox = new DicSuggestBox("doctors");
	if (drnProxy.getDoctorPerformed() != null)
	    doctorPerformedBox.getSuggestBox().setText(drnProxy.getDoctorPerformed().getEmployeeName());

	doctorPerformedBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		EmployeeProxy doctor = (EmployeeProxy) item.getEvent();
		drnProxy.setDoctorPerformed(doctor);
	    }
	});

	doctorPerformedBox.getSuggestBox().getTextBox().addBlurHandler(new BlurHandler() {

	    @Override
	    public void onBlur(BlurEvent event) {
		if (drnProxy.getDoctorPerformed() != null)
		    doctorPerformedBox.getSuggestBox().setText(drnProxy.getDoctorPerformed().getEmployeeName());
		else
		    doctorPerformedBox.getSuggestBox().setText("");
	    }
	});

	makeField(ftEdit, row, 0, "Принял:", doctorPerformedBox);

	// if(true) return;

	// Дата выполнения
	DateBox datePerformed = new DateBox();
	datePerformed.setFormat(new DateBox.DefaultFormat(Utils.dateFormatUser));
	datePerformed.setWidth("6em");

	datePerformed.addValueChangeHandler(new ValueChangeHandler<Date>() {

	    @Override
	    public void onValueChange(ValueChangeEvent<Date> event) {
		String d = Utils.dateFormatSql.format(event.getValue());
		drnProxy.setDatePerformed(d);
	    }
	});

	if (drnProxy.getDatePerformed() != null)
	    datePerformed.setValue(Utils.dateFormatSql.parse(drnProxy.getDatePerformed()));

	makeField(ftEdit, row, 2, "Выполнено:", datePerformed);

	// Дата планируемая
	makeField(ftEdit, row, 4, "Дата план:", new Label(drnProxy.getDateTimePlanned()));

	// Направленные диагнозы
	final DiagnosisPanel diagnosisPerformedPanel = new DiagnosisPanel(true);
	diagnosisPerformedPanel.setDiagnosis(drnProxy.getDiagnosisPerformed());
	row = makeFieldSimple(ftEdit, row + 2, "Направленные диагнозы:", diagnosisPerformedPanel);

	// Подтвержденные услуги
	final ServicePanel servicesPerformedPanel = new ServicePanel(true);
	servicesPerformedPanel.setServices(drnProxy.getServicesPerformed());
	row = makeFieldSimple(ftEdit, row + 2, "Подтвержденные услуги:", servicesPerformedPanel);

	//
	// Сохранение
	final Button btn = new Button("Сохранить направление");
	btn.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {

		// proxy.getDirection().setDirectionCode("CODEтест111");
		btn.setEnabled(false);
		// diagnosisDirrectPanel.getDiagnosis();
		drnProxy.setDiagnosisDirect(diagnosisDirrectPanel.getDiagnosis());
		drnProxy.setServicesDirect(servicesDirrectPanel.getServices());
		drnProxy.setDiagnosisPerformed(diagnosisPerformedPanel.getDiagnosis());
		drnProxy.setServicesPerformed(servicesPerformedPanel.getServices());

		Browser.browserService.saveDirection(drnProxy, new AsyncCallback<Void>() {

		    @Override
		    public void onFailure(Throwable caught) {
			btn.setEnabled(true);
			Browser.showErrorDlg(caught);

		    }

		    @Override
		    public void onSuccess(Void result) {
			btn.setEnabled(true);
		    }
		});
	    }
	});

	row = makeFieldSimple(ftEdit, row + 2, "", btn);

    }

    /**
     * @param t
     * @param row
     * @param text
     * @param widget
     */
    private void makeField(FlexTable t, int row, int col, String text, Widget widget) {

	Label l = new Label(text);
	l.setStyleName("DicomItemValue");

	t.setWidget(row, col, l);
	t.getFlexCellFormatter().setAlignment(row, col, HorizontalPanel.ALIGN_RIGHT, HorizontalPanel.ALIGN_MIDDLE);

	t.setWidget(row, ++col, widget);
	t.getFlexCellFormatter().setAlignment(row, col, HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);
    }

    /**
     * @param t
     * @param row
     * @param text
     * @param widget
     */
    private int makeFieldSimple(FlexTable t, int row, String text, Widget widget) {

	int col = 0;
	Label l = new Label(text);
	l.setStyleName("DicomItemValue");

	t.setWidget(row, col, l);
	t.getFlexCellFormatter().setAlignment(row, col, HorizontalPanel.ALIGN_CENTER, HorizontalPanel.ALIGN_MIDDLE);
	t.getFlexCellFormatter().setColSpan(row, col, 6);

	t.setWidget(++row, col, widget);
	t.getFlexCellFormatter().setAlignment(row, col, HorizontalPanel.ALIGN_LEFT, HorizontalPanel.ALIGN_MIDDLE);
	t.getFlexCellFormatter().setColSpan(row, col, 6);

	return row + 2;
    }

    /**
     * 
     */
    protected void showForm() {

	boolean exiting = false;

	if (allStuduesPanel != null && allStuduesPanel.isAttached()) {
	    allStuduesPanel.removeFromParent();
	    allStuduesPanel = null;
	    exiting = true;
	}

	if (studyManagePanel != null && studyManagePanel.isAttached()) {
	    studyManagePanel.removeFromParent();
	    studyManagePanel = null;
	    exiting = true;
	}

	// TODO Передалать!!! замороченно получилось. нужно перегруппировать
	// компоненты просто чтобы label с ФИО было отдельно, а остально можно
	// было седллать .clear();
	if (exiting)
	    return;

	final Label loading = new Label("Получение данных...");
	mainPanel.add(loading);

	Browser.browserService.getStudiesByDirectionID(drnProxy.getId(), new AsyncCallback<StudyProxy[]>() {

	    @Override
	    public void onSuccess(StudyProxy[] result) {
		// TODO Auto-generated method stub
		if (result.length == 0)
		    showStudy(null);
		else
		    showStudies(result);

		loading.removeFromParent();
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		Browser.showErrorDlg(caught);
		loading.removeFromParent();
	    }
	});

    }

    /**
     * Вывод набора ислседований
     * 
     * @param studies
     */
    private void showStudies(StudyProxy[] studies) {
	// TODO Auto-generated method stub
	allStuduesPanel = new HorizontalPanel();
	for (StudyProxy studyProxy : studies) {
	    allStuduesPanel.add(new StudiesPanel(studyProxy));
	}

	Button newStudyBtn = new Button("Создать исследование");
	allStuduesPanel.add(newStudyBtn);
	newStudyBtn.addClickHandler(new ClickHandler() {
	    
	    @Override
	    public void onClick(ClickEvent event) {
		showStudy(null);
	    }
	});
	
	mainPanel.add(allStuduesPanel);
    }

    /**
     * Вывод конкретного исследования
     */
    private void showStudy(StudyProxy proxy) {
	if (studyManagePanel == null || !studyManagePanel.isAttached()) {

	    if (proxy == null) {
		proxy = new StudyProxy();
		PatientProxy pat = drnProxy.getPatient();
		proxy.setPatientId("" + pat.getPatientId());
		proxy.setPatientName(pat.getPatientName());
		proxy.setPatientSex(pat.getPatientSex());
		// proxy.setPatientBirthDate(ORMUtil.userDateStringToSQLDateString(patientProxy.getPatientBirthDate()));
		proxy.setPatientBirthDate(pat.getPatientBirthDate());
		proxy.setStudyId(drnProxy.getDirectionId());
		proxy.setDirection(drnProxy);
		//
		String manufacturerModelName = "НЕТ В НАПРАВЛЕНИИ";
		if (drnProxy.getDevice() != null && drnProxy.getDevice().getManufacturerModelName() != null)
		    manufacturerModelName = drnProxy.getDevice().getManufacturerModelName();

		proxy.setManufacturerModelName(manufacturerModelName);

		//
		if (drnProxy.getDoctorPerformed() != null && drnProxy.getDoctorPerformed().getEmployeeName() != null)
		    proxy.setStudyDoctor(drnProxy.getDoctorPerformed().getEmployeeName());

		//
		String modality = "ХЗ";
		if (drnProxy.getDevice() != null && drnProxy.getDevice().getModality() != null)
		    modality = drnProxy.getDevice().getModality();

		proxy.setStudyModality(modality);
		
		//
		proxy.setStudyDate(Utils.dateFormatSql.format(new Date()));
		//
		proxy.setStudyViewprotocolDate(Utils.dateFormatSql.format(new Date()));
		

	    } else {

	    }
	    studyManagePanel = new StudyManagePanel(null, proxy);
	    mainPanel.add(studyManagePanel);
	}

    }

    /**
     * @param title
     * @return
     */
    Widget makeItemLabel(String title) {
	Label label = new Label(title);
	// TODO Убрать в css
	DOM.setStyleAttribute(label.getElement(), "font", "1.5em/ 150% normal Verdana, Tahoma, sans-serif");
	return label;
    }

    /**
     * @author dima_d
     * 
     *         Панель с информацией об исследовании
     */
    public class StudiesPanel extends HorizontalPanel {
	private StudyProxy studyProxy = null;

	public StudiesPanel(final StudyProxy studyProxy) {
	    super();
	    this.studyProxy = studyProxy;
	    final Button b = new Button("[" + studyProxy.getId() + "] " + studyProxy.getStudyDate());
	    add(b);
	    b.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    showStudy(studyProxy);
		    b.removeFromParent();
		    StudiesPanel.this.getParent().removeFromParent();
		}
	    });
	}

    }

}
