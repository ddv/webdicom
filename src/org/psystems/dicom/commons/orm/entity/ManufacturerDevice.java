package org.psystems.dicom.commons.orm.entity;

import java.io.Serializable;

import org.psystems.dicom.commons.orm.ORMUtil;

/**
 * Медицинский аппарат
 * 
 * @author dima_d
 * 
 */
public class ManufacturerDevice implements Serializable {

	private static final long serialVersionUID = 2089006109459477701L;
	private String ManufacturerModelName; // Модель
	private String modality; // Модальность. ORMUtil.modalityList
	
	private String ManufacturerModelTypeDescription; // Описание типа исследования
	private String ManufacturerModelDescription; // Описание аппарата (на русском)

	public ManufacturerDevice() {
		super();
	}

	public String getManufacturerModelName() {
		return ManufacturerModelName;
	}

	public void setManufacturerModelName(String manufacturerModelName) {
		ManufacturerModelName = manufacturerModelName;
	}


	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getManufacturerModelTypeDescription() {
		return ManufacturerModelTypeDescription;
	}

	public void setManufacturerModelTypeDescription(
			String manufacturerModelTypeDescription) {
		ManufacturerModelTypeDescription = manufacturerModelTypeDescription;
	}

	public String getManufacturerModelDescription() {
		return ManufacturerModelDescription;
	}

	public void setManufacturerModelDescription(
			String manufacturerModelDescription) {
		ManufacturerModelDescription = manufacturerModelDescription;
	}
	
	/**
	 * Проверка всех полей.
	 * TODO Реализовать прверки !!!
	 */
	public void chechEntity() {
		
		if(modality!=null && ORMUtil.modalityList.get(modality)==null) {
			throw new IllegalArgumentException("Field ManufacturerModelType Type wrong format [" + modality + "]." +
			" argument must be: "  + ORMUtil.getAllModalities());
		}
//		if (ManufacturerModelType != null
//				&& (!diagnosisType.equals(TYPE_MAIN)
//						&& !diagnosisType.equals(TYPE_ACCOMPANYING) && !diagnosisType
//						.equals(TYPE_INVOLVEMENT))) {
//			throw new IllegalArgumentException("Field Diagnosis Type wrong format [" + diagnosisType + "]." +
//					" argument must be: "+TYPE_MAIN+"|"+TYPE_ACCOMPANYING+"|"+TYPE_INVOLVEMENT);
//		}
	}

	@Override
	public String toString() {
		return "ManufacturerDevice [ManufacturerModelName="
				+ ManufacturerModelName + ", ManufacturerModelDescription="
				+ ManufacturerModelDescription + ", ManufacturerModelType="
				+ modality + ", ManufacturerModelTypeDescription="
				+ ManufacturerModelTypeDescription + "]";
	}

}
