/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import java.util.ArrayList;
import java.util.Arrays;

import org.psystems.dicom.browser.client.ItemSuggestion;
import org.psystems.dicom.browser.client.proxy.DiagnosisProxy;
import org.psystems.dicom.browser.client.proxy.ServiceProxy;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * Панель управления диагнозами
 * 
 * @author dima_d
 * 
 */
public class ServicePanel extends VerticalPanel {

    private VerticalPanel showServicePanel;
    private ArrayList<ServiceProxy> services;
    private ServiceProxy service4Add;
    private Button addBtn;
    private boolean editMode;

    /**
     * @param editMode
     *            Режим "для редактирования"
     */
    public ServicePanel(boolean editMode) {

	this.editMode = editMode;

	// панель для вывода списка диагнозов
	showServicePanel = new VerticalPanel();
	this.add(showServicePanel);

	if (editMode) {
	    final Label labelAdd = new Label("Добавить услугу...");
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
     * панель добавления услуги
     */
    private void createToolPanel() {

	HorizontalPanel addServicePanel = new HorizontalPanel();
	this.add(addServicePanel);

	DicSuggestBox diaBox = new DicSuggestBox("services");
	diaBox.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

	    @Override
	    public void onSelection(SelectionEvent<Suggestion> event) {
		ItemSuggestion item = (ItemSuggestion) event.getSelectedItem();
		service4Add = (ServiceProxy) item.getEvent();
		addBtn.setEnabled(true);
	    }
	});

	addServicePanel.add(diaBox);

	final TextBox serviceCount = new TextBox();
	serviceCount.setText("1");

	addServicePanel.add(serviceCount);

	addBtn = new Button("Добавить");
	addBtn.setEnabled(false);
	addServicePanel.add(addBtn);
	addBtn.addClickHandler(new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {

		if (service4Add == null)
		    return;

		ServiceProxy proxy = new ServiceProxy();
		proxy.setServiceCount(Integer.valueOf(serviceCount.getText()));

		proxy.setServiceCode(service4Add.getServiceCode());
		proxy.setServiceDescription(service4Add.getServiceDescription());
		proxy.setServiceAlias(service4Add.getServiceAlias());

		services.add(proxy);

		refresh();
		addBtn.setEnabled(false);
	    }
	});
    }

    public ServiceProxy[] getServices() {
	return services.toArray(new ServiceProxy[services.size()]);
    }

    public void setServices(ServiceProxy[] srvs) {

	services = new ArrayList<ServiceProxy>();
	services.addAll(Arrays.asList(srvs));

	refresh();
    }

    private void refresh() {
	showServicePanel.clear();
	for (ServiceProxy srv : services) {
	    ServiceItem item = new ServiceItem(srv);
	    showServicePanel.add(item);
	}
    }

    /**
     * @author dima_d
     * 
     *         Панелька с диагнозом
     * 
     */
    public class ServiceItem extends HorizontalPanel {

	private ServiceProxy srvs;

	public ServiceItem(ServiceProxy d) {
	    super();
	    setSpacing(2);
	    this.srvs = d;
	    StringBuffer diaText = new StringBuffer();
	    diaText.append(srvs.getServiceCode() + " (" + srvs.getServiceAlias() + " " + srvs.getServiceDescription()
		    + ") [" + srvs.getServiceCount() + "]");
	    add(new Label(diaText.toString()));

	    if (editMode) {
		Label labelDel = new Label("Удалить");
		labelDel.addStyleName("LabelLink");
		add(labelDel);

		labelDel.addClickHandler(new ClickHandler() {

		    @Override
		    public void onClick(ClickEvent event) {
			for (ServiceProxy serviceProxy : services) {
			    if (serviceProxy.getServiceCode().equalsIgnoreCase(srvs.getServiceCode())) {
				services.remove(serviceProxy);
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
