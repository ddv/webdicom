package org.psystems.dicom.browser.client;

import org.psystems.dicom.browser.client.exception.DefaultGWTRPCException;
import org.psystems.dicom.browser.client.service.DicSuggestBoxService;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;

public class ItemSuggestOracle extends MultiWordSuggestOracle {

    private String dicName = null;// название словаря

    public ItemSuggestOracle(String dicName) {
	super();
	this.dicName = dicName;
    }

    public boolean isDisplayStringHTML() {
	return true;
    }

    public void requestSuggestions(SuggestOracle.Request req, SuggestOracle.Callback callback) {
	try {
	    long searchTransactionID = 0;
	    DicSuggestBoxService.Util.getInstance().getSuggestions(searchTransactionID, Browser.version, dicName, req,
		    new ItemSuggestCallback(req, callback));
	} catch (DefaultGWTRPCException e) {
	    Browser.showErrorDlg(e);
	    e.printStackTrace();
	}
    }

}