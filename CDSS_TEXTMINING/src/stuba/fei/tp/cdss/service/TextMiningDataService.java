package stuba.fei.tp.cdss.service;

import java.util.ArrayList;
import java.util.List;

import stuba.fei.tp.cdss.dto.RecordsTemplate;
import stuba.fei.tp.cdss.dto.Symptom;

public interface TextMiningDataService {

	public ArrayList<Symptom> getAvailableSymtpoms(String disease);
	
	public String getMedicalRecords(String disease, String[] requiredSymptoms);
}
