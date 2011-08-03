package org.psystems.dicom.browser.client.service;

import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;

public interface DicSuggestBoxServiceAsync {

    void getSuggestions(long transactionId, String version, String dicName, Request req,
	    AsyncCallback<SuggestTransactedResponse> callback);

}
