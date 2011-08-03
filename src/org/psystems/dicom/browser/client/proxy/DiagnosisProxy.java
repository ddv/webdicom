package org.psystems.dicom.browser.client.proxy;

import java.io.Serializable;

public class DiagnosisProxy implements Serializable {

	private static final long serialVersionUID = -4407818270231331520L;

	public static final String TYPE_MAIN = "ОСНОВНОЙ";
	public static final String TYPE_INVOLVEMENT = "ОСЛОЖНЕНИЕ";
	public static final String TYPE_ACCOMPANYING = "СОПУТСТВУЮЩИЙ";

	// Предварительный, Уточненный, Выписки, Направления, Приемного отделения,
	// Клинический, Смерти, Паталогоанатомический.
	// TODO Сделать остальные типы
	public static final String SUBTYPE_MAIN = "";

	private String diagnosisType;// Тип. возможные варианты:
									// ОСНОВНОЙ|ОСЛОЖНЕНИЕ|СОПУТСТВУЮЩИЙ
	private String diagnosisSubType;// ПодтипТип (TODO пока проверок на этот тип
									// нету)
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

	@Override
	public String toString() {
		return "DiagnosisProxy [diagnosisCode=" + diagnosisCode
				+ ", diagnosisDescription=" + diagnosisDescription
				+ ", diagnosisSubType=" + diagnosisSubType + ", diagnosisType="
				+ diagnosisType + "]";
	}

}
