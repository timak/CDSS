package stuba.fei.tp.cdss.service;

import java.util.ArrayList;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import stuba.fei.tp.cdss.dto.Symptom;
import stuba.fei.tp.cdss.utils.XMLFileHelper;

public class TextMiningDataServiceImpl implements TextMiningDataService {

	@Override
	public ArrayList<Symptom> getAvailableSymtpoms(String disease) {
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
	public String getMedicalRecords(String disease, String[] requiredSymptoms) { 
		
		String recordsXML = XMLFileHelper.convertXMLFileToString("/records.xml");

		// encode xml with base 64 algorithm
		String encodedXml = Base64.encode(recordsXML.getBytes());
		System.out.println("Debug: Encoded xml - " + encodedXml);
		
		return encodedXml;
	}

}
