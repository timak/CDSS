package stuba.fei.tp.cdss.service;

import java.util.ArrayList;



import com.sun.org.apache.xml.internal.security.utils.Base64;

import stuba.fei.tp.cdss.dto.Patient;
import stuba.fei.tp.cdss.parser.RecordsParser;
import stuba.fei.tp.cdss.view.MainView;
import stuba.fei.tp.cdss.weka.WekaDbCoordinator;
import stuba.fei.tp.cdss.weka.WekaManager;

public class ServiceTest {

	public static void main(String[] args) throws Exception {
		//open main window
		MainView mainView = new MainView();
		mainView.createAndShowGUI();
		
		// test call 1
		/*ServiceCallHandler.getAvailableSymptoms("diabetes");
		
		
		// test call 2
		// get response
		String endocedData = ServiceCallHandler.getMedicalRecords("diabetes", new String[] {"mass", "age", "skin"});
		
		// decode data
		byte[] decodedByteData = Base64.decode(endocedData);
		String decodedXml = new String(decodedByteData);
		System.out.println(decodedXml);
		
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
		}*/
	}
}
