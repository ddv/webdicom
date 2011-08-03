package org.psystems.dicom.commons.solr.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;

public class Study {
	@Field
	public
	String id;

	@Field("studyId")
	public
	String study_Id;

	@Field
	public
	String patientName;

	@Field
	public
	String studyDescription;

	@Field("dia")
	public
	String[] diagnozis;

	@Field
	public
	List<String> services;

	@Field()
	Map<String, String> tags;
	
	@Field
	byte[] imagefull;

	@Field
	byte[] image100x100;
	
	

	public byte[] getImagefull() {
		return imagefull;
	}

	public void setImagefull(byte[] imagefull) {
		this.imagefull = imagefull;
	}
	
	

	public byte[] getImage100x100() {
		return image100x100;
	}

	public void setImage100x100(byte[] image100x100) {
		this.image100x100 = image100x100;
	}

	public String getId() {
		return id;
	}

	@Field
	public void setId(String id) {
		this.id = id;
	}

	public String getStudy_Id() {
		return study_Id;
	}

	@Field("studyId")
	public void setStudy_Id(String study_Id) {
		this.study_Id = study_Id;
	}

	public String getPatientName() {
		return patientName;
	}

	@Field
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getStudyDescription() {
		return studyDescription;
	}

	@Field
	public void setStudyDescription(String studyDescription) {
		this.studyDescription = studyDescription;
	}

	public String[] getDiagnozis() {
		return diagnozis;
	}

	@Field("dia")
	public void setDiagnozis(String[] diagnozis) {
		this.diagnozis = diagnozis;
	}

	public List<String> getServices() {
		return services;
	}

	@Field
	public void setServices(List<String> services) {
		this.services = services;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	@Field
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
	
	



	@Override
	public String toString() {
		return "Study [diagnozis=" + Arrays.toString(diagnozis) + ", id=" + id
				+ ", image100x100=" + image100x100
				+ ", imagefull=" + imagefull
				+ ", patientName=" + patientName + ", services=" + services
				+ ", studyDescription=" + studyDescription + ", study_Id="
				+ study_Id + ", tags=" + tags;
	}


	

}
