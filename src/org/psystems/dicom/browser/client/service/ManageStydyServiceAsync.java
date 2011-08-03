package org.psystems.dicom.browser.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ManageStydyServiceAsync {

	void newStudy(String patientName, AsyncCallback<Void> callback);

	void studyRemoveRestore(long idStudy, boolean removed,
			AsyncCallback<Void> callback);

	void dcmFileRemoveRestore(long idDcmFile, boolean removed,
			AsyncCallback<Void> callback);

	

}
