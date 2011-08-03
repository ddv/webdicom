package org.psystems.dicom.commons.solr.entity;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Словарь услуг
 * 
 * @author dima_d
 */
public class Service {

	@Field
	public String id;

	@Field
	public String dicName = "service";// Имя словаря

	@Field
	public String serviceCode;// Код

	@Field
	public String serviceAlias;// краткий код

	@Field
	public String serviceDescription;// Описание

	@Field
	public String modality;// модальность

	public String getId() {
		return id;
	}

	@Field
	public void setId(String id) {
		this.id = id;
	}

	public String getModality() {
		return modality;
	}

	@Field
	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	@Field
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceAlias() {
		return serviceAlias;
	}

	@Field
	public void setServiceAlias(String serviceAlias) {
		this.serviceAlias = serviceAlias;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	@Field
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getDicName() {
		return dicName;
	}

	@Field
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	@Override
	public String toString() {
		return "Service [dicName=" + dicName + ", id=" + id + ", serviceAlias="
				+ serviceAlias + ", serviceCode=" + serviceCode
				+ ", serviceDescription=" + serviceDescription + ", modality="
				+ modality + "]";
	}

}
