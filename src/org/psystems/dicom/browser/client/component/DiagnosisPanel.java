/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Arrays;

import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.proxy.DiagnosisProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * Панель управления диагнозами
 * 
 * @author dima_d
 * 
 */
public class DiagnosisPanel extends VerticalPanel {

    private VerticalPanel showDiagnosisPanel;
    private ArrayList<DiagnosisProxy> diagnosis;
    private DiagnosisProxy diagnosis4Add;
    private Button addBtn;
    private boolean editMode;

    /**
     * @param editMode
     *            Режим "для редактирования"
     */
    public DiagnosisPanel(boolean editMode) {

	this.editMode = editMode;
	// панель для вывода списка диагнозов
	showDiagnosisPanel = new VerticalPanel();
	this.add(showDiagnosisPanel);

	if (editMode) {
	    final Label labelAdd = new Label("Добавить диагноз...");
	    labelAdd.addStyleName("LabelLink");
	    add(labelAdd);

	    labelAdd.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    labelAdd.removeFromParent();
		    createToolPanel();
		}
	    });
	}

    }

    /**
     * панель добавления диагноза
     */
    private void createToolPanel() {

	HorizontalPanel addDiagnosisPanel = new HorizontalPanel();
	this.add(addDiagnosisPanel);

	final ListBox lbDiaType = new ListBox();
	lbDiaType.addItem(DiagnosisProxy.TYPE_MAIN);
	lbDiaType.addItem(DiagnosisProxy.TYPE_ACCOMPANYING);
	lbDiaType.addItem(DiagnosisProxy.TYPE_INVOLVEMENT);
	addDiagnosisPanel.add(lbDiaType);

	final ListBox lbDiaSubType = new ListBox();
	// Предварительный, Уточненный, Выписки, Направления, Приемного
	// отделения,
	// Клинический, Смерти, Паталогоанатомический.
	lbDiaSubType.addItem("Предварительный");
	lbDiaSubType.addItem("Уточненный");
	lbDiaSubType.addItem("Выписки");
	lbDiaSubType.addItem("Направления");
	lbDiaSubType.addItem("Приемного отделения");
	lbDiaSubType.addItem("Клинический");
	lbDiaSubType.addItem("Смерти");
	lbDiaSubType.addItem("Паталогоанатомический");
	addDiagnosisPanel.add(lbDiaSubType);

	DicSuggestBox diaBox = new DicSuggestBox("diagnosis");
	diaBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		diagnosis4Add = (DiagnosisProxy) item.getEvent();
		addBtn.setEnabled(true);
	    }
	});

	addDiagnosisPanel.add(diaBox);
	// itemsPanel.add(new DicSuggestBox("services"));

	addBtn = new Button("Добавить");
	addBtn.setEnabled(false);
	addDiagnosisPanel.add(addBtn);
	addBtn.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {

		if (diagnosis4Add == null)
		    return;

		DiagnosisProxy proxy = new DiagnosisProxy();
		proxy.setDiagnosisCode(diagnosis4Add.getDiagnosisCode());
		proxy.setDiagnosisDescription(diagnosis4Add.getDiagnosisDescription());
		proxy.setDiagnosisType(lbDiaType.getItemText(lbDiaType.getSelectedIndex()));
		proxy.setDiagnosisSubType(lbDiaSubType.getItemText(lbDiaSubType.getSelectedIndex()));

		diagnosis.add(proxy);

		refresh();
		addBtn.setEnabled(false);
	    }
	});
    }

    public DiagnosisProxy[] getDiagnosis() {
	return diagnosis.toArray(new DiagnosisProxy[diagnosis.size()]);
    }

    public void setDiagnosis(DiagnosisProxy[] dias) {

	diagnosis = new ArrayList<DiagnosisProxy>();
	diagnosis.addAll(Arrays.asList(dias));

	refresh();
    }

    private void refresh() {
	showDiagnosisPanel.clear();
	for (DiagnosisProxy dia : diagnosis) {
	    DiagnosisItem item = new DiagnosisItem(dia);
	    showDiagnosisPanel.add(item);
	}
    }

    /**
     * @author dima_d
     * 
     *         Панелька с диагнозом
     * 
     */
    public class DiagnosisItem extends HorizontalPanel {

	private DiagnosisProxy dias;

	public DiagnosisItem(DiagnosisProxy d) {
	    super();
	    setSpacing(2);
	    this.dias = d;
	    StringBuffer diaText = new StringBuffer();
	    diaText.append(dias.getDiagnosisCode() + " (" + dias.getDiagnosisType() + ";" + dias.getDiagnosisSubType()
		    + ") [" + dias.getDiagnosisDescription() + "]");
	    add(new Label(diaText.toString()));

	    if (editMode) {
		Label labelDel = new Label("Удалить");
		labelDel.addStyleName("LabelLink");
		add(labelDel);

		labelDel.addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			for (DiagnosisProxy diagnosisProxy : diagnosis) {
			    if (diagnosisProxy.getDiagnosisCode().equalsIgnoreCase(dias.getDiagnosisCode())) {
				diagnosis.remove(diagnosisProxy);
				refresh();
				return;
			    }
			}
		    }
		});
	    }
	}

    }

}
