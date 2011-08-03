/**
 * 
 */
package org.psystems.dicom.browser.client.component;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Панель отображения результатов поиска
 * 
 * @author dima_d
 * 
 */
/**
 * @author dima_d
 * 
 */
public class SearchResultPanel extends Composite {

	VerticalPanel resultPanel;
//	private FlowPanel toolsPanel;

	/**
	 * @param application
	 */
	public SearchResultPanel() {

//		DecoratorPanel decorator = new DecoratorPanel();
		VerticalPanel mainPanel = new VerticalPanel();
//		decorator.setWidget(mainPanel);

		
//		_makeToolsPanel(mainPanel);

		resultPanel = new VerticalPanel();
		mainPanel.add(resultPanel);
		
		//TODO Убрать в css
		DOM.setStyleAttribute(resultPanel.getElement(), "background", "#E9EDF5");
		resultPanel.setSpacing(10);
		resultPanel.setWidth("100%");

		showHelpPage();
		
		
		initWidget(mainPanel);
		//TODO Убрать в css
		setWidth("100%");
		
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
//		reLayout();
	}

//	private void _makeToolsPanel(VerticalPanel mainPanel) {
//		
////		toolsPanel = new FlowPanel();
////		mainPanel.add(toolsPanel);
//		
//		Window.addResizeHandler(new ResizeHandler() {
//			
//			@Override
//			public void onResize(ResizeEvent event) {
//				// TODO Auto-generated method stub
////				System.out.println("!!! resize"+event.getWidth());
//				reLayout();
//			}
//		});
//		
//		//TODO Убрать в css
////		DOM.setStyleAttribute(toolsPanel.getElement(), "background", "#E9EDF5");
////		DOM.setStyleAttribute(toolsPanel.getElement(), "marginBottom", "50px");
//		
////		for(int i=0; i< 20; i++) {
////		
////		Button b = new Button("!!! "+i);
////		DOM.setStyleAttribute(b.getElement(), "margin", "3px");
////		toolsPanel.add(b);
////		
////		}
//	}

//	private void reLayout() {
//		int width = Window.getClientWidth();
//		int left = toolsPanel.getAbsoluteLeft();
//		toolsPanel.setWidth((width - left*2)+"px");
//		for(int i=0; i<resultPanel.getWidgetCount(); i++){
//			Widget widget = resultPanel.getWidget(i);
//			widget.setWidth((width - left*2 - 20)+"px");
//		}
//		
//	}
	
	public void clear() {
		resultPanel.clear();
//		showHelpPage();
	}

	public void add(Widget w) {
		resultPanel.add(w);
	}

	private void showHelpPage() {
		
		
		
		HTML html = new HTML(
				"Тут будут подсказки по механизмам поиска информации");
		resultPanel.add(html);
		
//		IntroPanel intro = new IntroPanel();
//		resultPanel.add(intro);
		// DOM.setStyleAttribute(html.getElement(), "width", "1000px");

	}

}
