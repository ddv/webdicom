package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

/**
 * @author dima_d
 * 
 *         Шаблон для openoffice
 */
public class OOTemplateProxy implements Serializable {

	private static final long serialVersionUID = -6624806493304345239L;

	// Название шаблона
	String title;
	// URL
	String url;
	// Тип шаблона (CR,ES,..)
	String modality;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

}
