package stuba.fei.tp.cdss.service;

import java.util.ArrayList;
import java.util.List;

import stuba.fei.tp.cdss.dto.RecordsTemplate;
import stuba.fei.tp.cdss.dto.Symptom;

public class TextMiningDataServiceImpl implements TextMiningDataService {

	@Override
	public ArrayList<Symptom> getAvailableSymtpoms() {
		//test
		ArrayList<Symptom> symptoms = new ArrayList<Symptom>();
		Symptom a = new Symptom("symptom A");
		Symptom b = new Symptom("symptom B");
		Symptom c = new Symptom("symptom C");
		
		symptoms.add(a);
		symptoms.add(b);
		symptoms.add(c);
		
		return symptoms;
	}

	@Override
	public String getMedicalRecords(String[] requiredSymptoms) {
		//test
		System.out.println("Hello");
		System.out.println("Syms: " + requiredSymptoms.toString());
		String requiredSyms = "";
		for (String symptom : requiredSymptoms) {
			requiredSyms += symptom + " "; 
		}
		return "Base64 encoded xml with requested medical records for symptoms: " + requiredSyms;
	}

}
