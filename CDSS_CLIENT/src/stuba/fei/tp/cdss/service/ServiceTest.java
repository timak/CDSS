package stuba.fei.tp.cdss.service;

import java.util.ArrayList;



import com.sun.org.apache.xml.internal.security.utils.Base64;

import stuba.fei.tp.cdss.dto.Patient;
import stuba.fei.tp.cdss.parser.RecordsParser;
import stuba.fei.tp.cdss.weka.WekaDbCoordinator;
import stuba.fei.tp.cdss.weka.WekaManager;

public class ServiceTest {

	public static void main(String[] args) throws Exception {
		// test call 1
		ServiceCallHandler.getAvailableSymptoms();
		
		
		// test call 2
		// get response
		String endocedData = ServiceCallHandler.getMedicalRecords(new String[] {"sym1", "sym2", "sym3"});
		
		// decode data
		byte[] decodedByteData = Base64.decode(endocedData);
		String decodedXml = new String(decodedByteData);
		
		// parse xml
		RecordsParser parser = new RecordsParser();
		ArrayList<Patient> recordsData = parser.parseRecords(decodedXml);
		
		WekaDbCoordinator wekaCoor = new WekaDbCoordinator();
		try {
			// create temp table for weka
			wekaCoor.createWekaData(recordsData);
			
			// classify data
			WekaManager.klasifikujData();
			
			// remove temp table
			wekaCoor.dropTable();
			wekaCoor.closeConnection();
			
		} catch (Exception e) {
			System.out.println("Unable to create temp table for weka: " + e.toString());
			wekaCoor.closeConnection();
		}
	}
}
