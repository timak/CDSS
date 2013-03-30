package stuba.fei.tp.cdss.dto;

import java.util.HashMap;


/*
 * Represents one record
 */
public class Patient {

	private HashMap<String, String> symptoms;
	
	private String diagnosis;
	

	public HashMap<String, String> getSymptoms() {
		return symptoms;
	}

	public void setSymptoms(HashMap<String, String> symptoms) {
		this.symptoms = symptoms;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
}
