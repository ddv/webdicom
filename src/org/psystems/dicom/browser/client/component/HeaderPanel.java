/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;

/**
 * Панель заголовка проекта
 * 
 * @author dima_d
 * 
 */
public class HeaderPanel extends HorizontalPanel {

	/**
	 * @param application
	 */
	public HeaderPanel() {
		
		HorizontalPanel itemsPanel = new HorizontalPanel();
		this.add(itemsPanel);
		itemsPanel.setSpacing(5);
		
		_applayStyles();
		Hyperlink mainPage = new Hyperlink("Исследования", "");
		itemsPanel.add(mainPage);
		itemsPanel.add(new Label("|"));
		Hyperlink newStudy = new Hyperlink("Создать исследование", "newstudy");
		itemsPanel.add(newStudy);
		itemsPanel.add(new Label("|"));
		Hyperlink worklistStudy = new Hyperlink("Список исследований", "workliststudy");
		itemsPanel.add(worklistStudy);
		itemsPanel.add(new Label("|"));
		Hyperlink directions = new Hyperlink("Направления", "directions");
		itemsPanel.add(directions);
		itemsPanel.add(new Label("|"));
		Hyperlink showIntro = new Hyperlink("Помощь", "showintro");
		itemsPanel.add(showIntro);
		
//		itemsPanel.add(new DicSuggestBox("diagnosis"));
//		itemsPanel.add(new DicSuggestBox("services"));
	}
	
	/**
	 * Применение стилей
	 */
	private void _applayStyles() {
//		DOM.setStyleAttribute(this.getElement(), "marginLeft", "50");
		DOM.setStyleAttribute(this.getElement(), "width", "100%");
		DOM.setStyleAttribute(this.getElement(), "borderBottom", "1px solid #44639C");
		
		this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);		
	}

}
