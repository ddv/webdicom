package org.psystems.dicom.browser.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.commons.orm.ORMUtil;
import org.psystems.dicom.commons.orm.PersistentManagerDerby;
import org.psystems.dicom.commons.orm.entity.DataException;
import org.psystems.dicom.commons.orm.entity.Study;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PatternColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPatternPainter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.AcroFields.Item;

/**
 * @author dima_d
 * 
 *         Сервлет управления формирования PDF шаблонов
 * 
 */
public class ManagePdfServlet extends HttpServlet {

    private static final long serialVersionUID = 8911247236211732365L;
    private static Logger logger = Logger.getLogger(ManagePdfServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	changePDFContent(req, resp, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	req.setCharacterEncoding("cp1251");

	changePDFContent(req, resp, true);
    }

    /**
     * Преообразование PDF-ки
     * 
     * @param req
     * @param resp
     * @param finalPhase
     *            - финальная стадия
     * @throws IOException
     */
    private void changePDFContent(HttpServletRequest req, HttpServletResponse resp, boolean finalPhase)
	    throws IOException {

	String dcmTmpDir = getServletContext().getInitParameter("webdicom.dir.newdcm.tmp");
	String pdfTmpFilename = dcmTmpDir + "/" + new Date().getTime() + "_" + (int) (Math.random() * 10000000l)
		+ ".pdf";
	File pdfTmpFile = null;
	// TODO Поменять имя переменной webdicom.dir.ootmpl на webdicom.dir.pdf
	String tmplDir = getServletContext().getInitParameter("webdicom.dir.ootmpl");
	String file = tmplDir + req.getPathInfo();

	try {

	    long id = Long.valueOf(req.getParameter("id")).longValue();

	    Connection connection = Util.getConnection("main", getServletContext());
	    PersistentManagerDerby pm = new PersistentManagerDerby(connection);

	    Study study = pm.getStudyByID(id);

	    FileInputStream fis = new FileInputStream(file);
	    PdfReader reader = new PdfReader(fis);
	    pdfTmpFile = new File(pdfTmpFilename);
	    // OutputStream out = resp.getOutputStream();
	    // PdfStamper stamper = new PdfStamper(reader, out);
	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfTmpFile));

	    // Пробегаем по полям формы.
	    // Если поле READ_ONLY - заменяем его на текст
	    AcroFields form = stamper.getAcroFields();

	    String fName;

	    // ФИО Пациента
	    fName = "PatientName";
	    if (form.getField(fName) != null)
		form.setField(fName, study.getPatientName());

	    // ДР Пациента
	    fName = "PatientBirthDate";
	    if (form.getField(fName) != null)
		form.setField(fName, ORMUtil.dateFormatUser.format(ORMUtil.dateFormatSQL.parse(study
			.getPatientBirthDate())));

	    // Аппарат
	    fName = "ManufacturerModelName";
	    if (form.getField(fName) != null)
		form.setField(fName, study.getManufacturerModelName());

	    // Протокол осмотра
	    fName = "StudyViewprotocol";
	    if (req.getParameter(fName) != null)
		study.setStudyViewprotocol(req.getParameter(fName));
	    if (form.getField(fName) != null)
		form.setField(fName, study.getStudyViewprotocol());

	    // Дата протокола осмотра
	    fName = "StudyViewprotocolDate";
	    if (form.getField(fName) != null)
		form.setField(fName, ORMUtil.dateFormatUser.format(ORMUtil.dateFormatSQL.parse(study
			.getStudyViewprotocolDate())));

	    // ===================================================================
	    // TODO !!!======== дополнить остальные поля ==================!!!!!
	    // ===================================================================

	    for (String fieldName : form.getFields().keySet()) {

		// пропускаем "радиобаттоны"
		if (form.getFieldType(fieldName) == AcroFields.FIELD_TYPE_RADIOBUTTON)
		    continue;

		// Установка значений полей из БД

		// Установка значений полей из QUERY_STRING
		if (req.getParameter(fieldName) != null) {
		    form.setField(fieldName, req.getParameter(fieldName));
		}

		// Свойства поля

		// boolean readOnly = fieldIsREADONLY(form, fieldName);
		// System.out.println("!!! field " + fieldName + " RO is " +
		// readOnly);

		// ------------

		// //Задаем доп. флаги для поля
		// fields.setField(fieldName, "Тестимся");
		// fields.setFieldProperty(fieldName, "setfflags",
		// TextField.READ_ONLY, null);
	    }

	    // Удаляем поля
	    replaceFields(reader, stamper, !finalPhase);

	    if (!finalPhase) {
		// Добавляем кнопку Submit
		int btnWidth = 100;
		int btnHeight = 30;
		PushbuttonField button = new PushbuttonField(stamper.getWriter(), new Rectangle(90, 60, 90 + btnWidth,
			60 + btnHeight), "submit");
		button.setText("Передать в архив...");
		button.setBackgroundColor(new GrayColor(0.7f));

		// button.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
		PdfFormField submit = button.getField();
		submit.setAction(PdfAction.createSubmitForm(req.getServletPath(), null, PdfAction.SUBMIT_HTML_FORMAT));

		
//		System.out.println("!!!!!!!!!!! getRequestURI "+req.getRequestURI());
		String url = req.getRequestURI()+ "?final=" + finalPhase + "&" + req.getQueryString();
		submit.setAction(PdfAction.createSubmitForm(url, null, PdfAction.SUBMIT_HTML_FORMAT));

		stamper.addAnnotation(submit, 1);
	    } else {
		// Добавляем кнопку "Закрыть"
		int btnWidth = 300;
		int btnHeight = 30;
		PushbuttonField button = new PushbuttonField(stamper.getWriter(), new Rectangle(90, 60, 90 + btnWidth,
			60 + btnHeight), "submit");
		button.setText("Данные сохранены. [закрыть]");
		
		button.setBackgroundColor(BaseColor.YELLOW);

		button.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
		PdfFormField submit = button.getField();
		submit.setAction(PdfAction.createSubmitForm(req.getServletPath(), null, PdfAction.SUBMIT_HTML_FORMAT));

		
//		System.out.println("!!!!!!!!!!! getRequestURI "+req.getRequestURI());
		String url = req.getRequestURI()+ "?final=" + finalPhase + "&" + req.getQueryString();
		
		 PdfAction action = PdfAction.javaScript(
		            "app.alert('This day is reserved for people with an accreditation "
		            + "or an invitation.');", stamper.getWriter());
		 
		 submit.setAction(action);


		stamper.addAnnotation(submit, 1);
	    }

	    stamper.close();
	    resp.setContentType("application/pdf; charset=UTF-8");

	    // stamper.close();
	    // Передача результата в броузер
	    FileInputStream in = new FileInputStream(pdfTmpFilename);
	    BufferedOutputStream out = new BufferedOutputStream(resp.getOutputStream());

	    byte b[] = new byte[8];
	    int count;
	    while ((count = in.read(b)) != -1) {
		out.write(b, 0, count);

	    }
	    out.flush();
	    out.close();
	    in.close();

	    // Отправка данных в архив
	    if (finalPhase)
		sendToArchive(study, pdfTmpFile);

	} catch (DocumentException e) {
	    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    resp.setContentType("text/html; charset=UTF-8");
	    String msg = Util.loggingException("<b>Bad PDF !</b> " + file, e);
	    resp.getWriter().print(msg);
	    logger.warn(msg);
	} catch (FileNotFoundException e) {
	    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    resp.setContentType("text/html; charset=UTF-8");
	    String msg = Util.loggingException("<b>PDF template not found!</b> " + file + " Не найден!", e);
	    resp.getWriter().print(msg);
	    logger.warn(msg);
	    return;
	} catch (Exception e) {
	    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    resp.setContentType("text/html; charset=UTF-8");
	    String msg = Util.loggingException(e.getMessage(), e);
	    resp.getWriter().print(msg);
	    logger.warn(msg);
	} catch (DataException e) {
	    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    resp.setContentType("text/html; charset=UTF-8");
	    String msg = Util.loggingException(e.getMessage(), e);
	    resp.getWriter().print(msg);
	    logger.warn(msg);
	} finally {
	    if (pdfTmpFile != null)
		pdfTmpFile.delete();
	}
    }

    /**
     * Отправка в архив PDF-ки
     * 
     * @param study
     * @param pdf
     * @throws DefaultGWTRPCException
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void sendToArchive(Study study, File pdf) throws FileNotFoundException, IOException, DefaultGWTRPCException {

	// TODO Коряво все это... переделать. логику сюда перенести.

	java.util.Properties props = new java.util.Properties();

	// TODO Костылек, указвывает тип контента
	props.put("content_type", "application/pdf");

	// раскручивает теги:

	// Hidden studyInstanceUID = new Hidden();
	// studyInstanceUID.setName("0020000D");
	// studyInstanceUID.setValue(proxy.getStudyInstanceUID());
	// formDataPanel.add(studyInstanceUID);
	if (study.getStudyInstanceUID() != null)
	    props.put("0020000D", study.getStudyInstanceUID());

	// Hidden studySeriesUID = new Hidden();
	// studySeriesUID.setName("0020000E");
	// studySeriesUID.setValue(proxy.getStudyInstanceUID() + "." + new
	// Date().getTime());
	// formDataPanel.add(studySeriesUID);
	if (study.getStudyInstanceUID() != null)
	    props.put("0020000E", study.getStudyInstanceUID() + "." + new Date().getTime());

	// Hidden studyId = new Hidden();
	// studyId.setName("00200010");
	// studyId.setValue(proxy.getStudyId());
	// formDataPanel.add(studyId);
	if (study.getStudyId() != null)
	    props.put("00200010", study.getStudyId());

	//

	// Hidden patientId = new Hidden();
	// patientId.setName("00100021");
	// patientId.setValue(proxy.getPatientId());
	// formDataPanel.add(patientId);
	if (study.getPatientId() != null)
	    props.put("00100021", study.getPatientId());

	// Hidden patientName = new Hidden();
	// patientName.setName("00100010");
	// patientName.setValue(proxy.getPatientName());
	// formDataPanel.add(patientName);
	if (study.getPatientName() != null)
	    props.put("00100010", study.getPatientName());

	// Hidden patientSex = new Hidden();
	// patientSex.setName("00100040");
	// patientSex.setValue(proxy.getPatientSex());
	// formDataPanel.add(patientSex);
	if (study.getPatientSex() != null)
	    props.put("00100040", study.getPatientSex());

	// Hidden patientBirthDateHidden = new Hidden();
	// patientBirthDateHidden.setName("00100030");
	// patientBirthDateHidden.setValue(proxy.getPatientBirthDate());
	// formDataPanel.add(patientBirthDateHidden);
	if (study.getPatientBirthDate() != null)
	    props.put("00100030", study.getPatientBirthDate());

	//

	// Hidden manufacturerModelName = new Hidden();
	// manufacturerModelName.setName("00081090");
	// manufacturerModelName.setValue(proxy.getManufacturerModelName());
	// formDataPanel.add(manufacturerModelName);
	if (study.getManufacturerModelName() != null)
	    props.put("00081090", study.getManufacturerModelName());

	// Hidden modality = new Hidden();
	// modality.setName("00080060");
	// // modality.setValue(proxy.getStudyModality());
	// modality.setValue(lbModality.getValue(lbModality.getSelectedIndex()));
	// formDataPanel.add(modality);
	if (study.getStudyModality() != null)
	    props.put("00080060", study.getStudyModality());

	// Hidden studyDateHidden = new Hidden();
	// studyDateHidden.setName("00080020");
	// studyDateHidden.setValue(proxy.getStudyDate());
	// formDataPanel.add(studyDateHidden);
	if (study.getStudyDate() != null)
	    props.put("00080020", study.getStudyDate());

	// // Study Completion Date
	// Hidden studyViewProtocolDateHidden = new Hidden();
	// studyViewProtocolDateHidden.setName("00321050");
	// studyViewProtocolDateHidden.setValue(proxy.getStudyViewprotocolDate());
	// formDataPanel.add(studyViewProtocolDateHidden);
	if (study.getStudyViewprotocolDate() != null)
	    props.put("00321050", study.getStudyViewprotocolDate());

	// Hidden studyDoctorHidden = new Hidden();
	// studyDoctorHidden.setName("00080090");
	// studyDoctorHidden.setValue(proxy.getStudyDoctor());
	// formDataPanel.add(studyDoctorHidden);
	if (study.getStudyDoctor() != null)
	    props.put("00080090", study.getStudyDoctor());

	// Hidden studyOperatorHidden = new Hidden();
	// studyOperatorHidden.setName("00081070");
	// studyOperatorHidden.setValue(proxy.getStudyOperator());
	// formDataPanel.add(studyOperatorHidden);
	if (study.getStudyOperator() != null)
	    props.put("00081070", study.getStudyOperator());

	//
	// proxy.setStudyDescription(studyDescription.getText());
	//
	// Hidden studyDescriptionHidden = new Hidden();
	// studyDescriptionHidden.setName("00081030");
	// studyDescriptionHidden.setValue(proxy.getStudyDescription());
	// formDataPanel.add(studyDescriptionHidden);
	if (study.getStudyDescription() != null)
	    props.put("00081030", study.getStudyDescription());

	//
	// proxy.setStudyResult(studyResult.getText());
	//
	// Hidden studyResultHidden = new Hidden();
	// studyResultHidden.setName("00102000");
	// studyResultHidden.setValue(proxy.getStudyResult());
	// formDataPanel.add(studyResultHidden);
	if (study.getStudyResult() != null)
	    props.put("00102000", study.getStudyResult());

	//
	// proxy.setStudyViewprotocol(studyComments.getText());
	//
	// // Tag.StudyComments
	// Hidden studyCommentsHidden = new Hidden();
	// studyCommentsHidden.setName("00324000");
	// studyCommentsHidden.setValue(proxy.getStudyViewprotocol());
	// formDataPanel.add(studyCommentsHidden);
	if (study.getStudyViewprotocol() != null)
	    props.put("00324000", study.getStudyViewprotocol());

	//

	FileInputStream fis = new FileInputStream(pdf);
	org.psystems.dicom.commons.CommonUtil.makeSendDicomFile(getServletContext(), props, fis);
	fis.close();
    }

    /**
     * Замена полей на текст
     * 
     * @param reader
     * @param stamper
     * @param onlyROfields
     *            удалять только поля которые READ_ONLY
     * @throws IOException
     * @throws DocumentException
     */
    private void replaceFields(PdfReader reader, PdfStamper stamper, boolean onlyROfields) throws IOException,
	    DocumentException {
	Set<String> parameters = stamper.getAcroFields().getFields().keySet();
	AcroFields form = stamper.getAcroFields();

	String[] fields = parameters.toArray(new String[parameters.size()]);

	for (String fieldName : fields) {

	    // Замещаем только READ_ONLY поля
	    if (onlyROfields && !fieldIsREADONLY(form, fieldName))
		continue;

	    Item field = form.getFieldItem(fieldName);
	    PdfDictionary widgetDict = field.getWidget(0);
	    // pdf rectangles are stored as [llx, lly, urx, ury]
	    PdfArray rectArr = widgetDict.getAsArray(PdfName.RECT); // should
	    float llX = rectArr.getAsNumber(0).floatValue();
	    float llY = rectArr.getAsNumber(1).floatValue();
	    float urX = rectArr.getAsNumber(2).floatValue();
	    float urY = rectArr.getAsNumber(3).floatValue();

	    String value = form.getField(fieldName);
	    // Удаляем поле
	    form.removeField(fieldName);

	    PdfContentByte canvas = stamper.getOverContent(1);

	    // FIXME Сделать конфигуриремым или засунуть шоифт в CLASSPATH
	    // Сейчас просто закинул на продуктиве в папку tomcat/lib
	    String fontPath = "fonts/arial.ttf";
	    Font font = new Font(BaseFont.createFont(fontPath, "Cp1251", BaseFont.NOT_EMBEDDED), 14);

	    Phrase phrase = new Phrase(value, font);

	    ColumnText columnText = new ColumnText(canvas);
	    columnText.setSimpleColumn(llX, llY, urX, urY, columnText.getLeading(), Element.ALIGN_LEFT);
	    columnText.setText(phrase);

	    columnText.go();
	}

    }

    /**
     * Проверка у поля аттрибута READ_ONLY
     * 
     * @param form
     * @param fieldName
     */
    private boolean fieldIsREADONLY(AcroFields form, String fieldName) {

	Map<String, AcroFields.Item> fieldsMap = form.getFields();
	AcroFields.Item item;
	PdfDictionary dict;
	int flags = 0;
	for (Map.Entry<String, AcroFields.Item> entry : fieldsMap.entrySet()) {
	    String key = entry.getKey();

	    if (fieldName.equals(key)) {

		item = entry.getValue();
		dict = item.getMerged(0);

		if (dict.getAsNumber(PdfName.FF) != null)
		    flags = dict.getAsNumber(PdfName.FF).intValue();

		if ((flags & BaseField.READ_ONLY) > 0)
		    return true;
		else
		    return false;
	    }
	}
	return false;
    }

}