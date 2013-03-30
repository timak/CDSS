package stuba.fei.tp.cdss.service;

import java.rmi.RemoteException;

import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetAvailableSymtpoms;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetAvailableSymtpomsResponse;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetMedicalRecords;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetMedicalRecordsResponse;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.Symptom;

public class ServiceCallHandler {
	
	public static Symptom[] getAvailableSymptoms() throws RemoteException {
		TextMiningDataServiceImplStub stub = new TextMiningDataServiceImplStub();
		GetAvailableSymtpoms request = new GetAvailableSymtpoms();
		GetAvailableSymtpomsResponse response = stub.getAvailableSymtpoms(request);
		Symptom[] symptoms = response.get_return();
		System.out.println("Response: ");
		for (Symptom s : symptoms) {
			System.out.println("Debug: " + s.getName());
		}
		
		return symptoms;
	}
	
	public static String getMedicalRecords(String[] symptoms) throws RemoteException {
		TextMiningDataServiceImplStub stub = new TextMiningDataServiceImplStub();
		GetMedicalRecords request = new GetMedicalRecords();
		
		request.setRequiredSymptoms(symptoms);
		
		GetMedicalRecordsResponse response = stub.getMedicalRecords(request);
		String xmlData = response.get_return();
		System.out.println("Debug: Response: " + xmlData);
		
		return xmlData;
	}
}
