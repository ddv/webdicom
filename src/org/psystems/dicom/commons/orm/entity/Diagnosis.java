package org.psystems.dicom.commons.orm.entity;

import java.io.Serializable;

/**
 * Сущность "Диагноз"
 * 
 * @author dima_d
 */
public class Diagnosis implements Serializable {

	private static final long serialVersionUID = -2133521701334810608L;
	
	public static final String TYPE_MAIN = "ОСНОВНОЙ";
	public static final String TYPE_INVOLVEMENT = "ОСЛОЖНЕНИЕ";
	public static final String TYPE_ACCOMPANYING = "СОПУТСТВУЮЩИЙ";

	// Предварительный, Уточненный, Выписки, Направления, Приемного отделения,
	// Клинический, Смерти, Паталогоанатомический.
	//TODO Сделать остальные типы
	public static final String SUBTYPE_MAIN = "";

	private String diagnosisType;// Тип. возможные варианты: ОСНОВНОЙ|ОСЛОЖНЕНИЕ|СОПУТСТВУЮЩИЙ
	private String diagnosisSubType;// ПодтипТип (TODO пока проверок на этот тип нету)
	private String diagnosisCode;// Код (по МКБ)
	private String diagnosisDescription;// Описание


	public String getDiagnosisType() {
		return diagnosisType;
	}

	public void setDiagnosisType(String diagnosisType) {
		this.diagnosisType = diagnosisType;
	}

	public String getDiagnosisSubType() {
		return diagnosisSubType;
	}

	public void setDiagnosisSubType(String diagnosisSubType) {
		this.diagnosisSubType = diagnosisSubType;
	}

	public String getDiagnosisCode() {
		return diagnosisCode;
	}

	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}

	public String getDiagnosisDescription() {
		return diagnosisDescription;
	}

	public void setDiagnosisDescription(String diagnosisDescription) {
		this.diagnosisDescription = diagnosisDescription;
	}
	
	/**
	 * Проверка всех полей.
	 */
	public void chechEntity() {
		
		if (diagnosisType != null
				&& (!diagnosisType.equals(TYPE_MAIN)
						&& !diagnosisType.equals(TYPE_ACCOMPANYING) && !diagnosisType
						.equals(TYPE_INVOLVEMENT))) {
			throw new IllegalArgumentException("Field Diagnosis Type wrong format [" + diagnosisType + "]." +
					" argument must be: "+TYPE_MAIN+"|"+TYPE_ACCOMPANYING+"|"+TYPE_INVOLVEMENT);
		}
	}

	@Override
	public String toString() {
		return "Diagnosis [diagnosisCode=" + diagnosisCode
				+ ", diagnosisDescription=" + diagnosisDescription
				+ ", diagnosisSubType=" + diagnosisSubType + ", diagnosisType="
				+ diagnosisType + "]";
	}

}
