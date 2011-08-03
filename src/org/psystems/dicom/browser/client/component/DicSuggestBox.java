package org.psystems.dicom.browser.client.component;

import org.psystems.dicom.browser.client.ItemSuggestOracle;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;

/**
 * @author dima_d
 * 
 *         Компонент выбора значения из словаря
 */
public class DicSuggestBox extends Composite {

    private SuggestBox box;

    public DicSuggestBox(String dicName) {
	ItemSuggestOracle oracle = new ItemSuggestOracle(dicName);
	box = new SuggestBox(oracle);
	initWidget(box);
    }

    /**
     * Получить внутренний SuggestBox
     * 
     * @return
     */
    public SuggestBox getSuggestBox() {
	return box;
    }

}
