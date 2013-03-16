package stuba.fei.tp.cdss.service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetAvailableSymtpoms;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetAvailableSymtpomsResponse;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetMedicalRecords;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetMedicalRecordsResponse;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.Symptom;

public class ServiceTest {


	
	public static void main(String[] args)  {
		
		// test call 1
		try {
			testGetAvailableSymptoms();
		} catch (Exception e) {
			System.out.println("Error calling getAvailableSymptoms: " + e.toString());
		}
		
		//test call 2
		try {
			testGetMedicalRecords();
		} catch (Exception e) {
			System.out.println("Error calling getAvailableSymptoms: " + e.toString());
		}
	}
	
	private static void testGetAvailableSymptoms() throws RemoteException {
		TextMiningDataServiceImplStub stub = new TextMiningDataServiceImplStub();
		GetAvailableSymtpoms request = new GetAvailableSymtpoms();
		GetAvailableSymtpomsResponse response = stub.getAvailableSymtpoms(request);
		Symptom[] symptoms = response.get_return();
		System.out.println("Response: ");
		for (Symptom s : symptoms) {
			System.out.println(s.getName());
		}
	}
	
	private static void testGetMedicalRecords() throws RemoteException {
		TextMiningDataServiceImplStub stub = new TextMiningDataServiceImplStub();
		GetMedicalRecords request = new GetMedicalRecords();
		
		ArrayList<Symptom> requiredSymptoms = new ArrayList<Symptom>();
		Symptom s1 = new Symptom();
		Symptom s2 = new Symptom();
		requiredSymptoms.add(s1);
		requiredSymptoms.add(s2);
		request.setRequiredSymptoms(new String[] {"sym1", "sym2", "sym3"});
		
		GetMedicalRecordsResponse response = stub.getMedicalRecords(request);
		String xmlData = response.get_return();
		System.out.println("Response: " + xmlData);
	}

}
