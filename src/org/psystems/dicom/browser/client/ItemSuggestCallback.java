package org.psystems.dicom.browser.client;

import org.psystems.dicom.browser.client.proxy.SuggestTransactedResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

class ItemSuggestCallback implements AsyncCallback<SuggestTransactedResponse> {
    private SuggestOracle.Request req;
    private SuggestOracle.Callback callback;

    public ItemSuggestCallback(SuggestOracle.Request _req, SuggestOracle.Callback _callback) {
	req = _req;
	callback = _callback;
    }

    public void onFailure(Throwable error) {
	Browser.showErrorDlg(error);
	callback.onSuggestionsReady(req, new SuggestOracle.Response());
	Browser.showErrorDlg(error);
    }

    @Override
    public void onSuccess(SuggestTransactedResponse result) {
	// TODO Auto-generated method stub
	callback.onSuggestionsReady(req, (SuggestOracle.Response) result);
    }
}