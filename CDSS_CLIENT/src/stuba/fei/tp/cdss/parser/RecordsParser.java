package stuba.fei.tp.cdss.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import stuba.fei.tp.cdss.dto.Patient;

/*
 * Parser processes input xml and returns arraylist of objects of type patient, 
 * which contains map name_of_symptom:value_of_symptom and diagnosis 
 * 
 * Format of xml:
 * 	<records>
 * 		<patient>
 *			<symptom name="A">12</symptom>
 *			<symptom name="B">5</symptom>
 *			<symptom name="C">0</symptom>
 *			<diagnosis>positive</diagnosis>
 *		</patient>
 *		<patient>
 *			<symptom name="A">4</symptom>
 *			<symptom name="B">7</symptom>
 *			<symptom name="C">1</symptom>
 *			<diagnosis>negative</diagnosis>
 *		</patient>
 *  </records>
 */
public class RecordsParser {
	
	// xml elements and attributes names
	public static String ELEMENT_NAME_SYMTPOM = "symptom";
	public static String ELEMENT_NAME_PATIENT = "patient";
	public static String ELEMENT_NAME_DIAGNOSIS = "diagnosis";
	public static String ATT_NAME_SYMPTOM_NAME = "name";
	
	private ArrayList<Patient> records = new ArrayList<Patient>();
	
	public ArrayList<Patient> parseRecords(String recordsString) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(recordsString));
		Document doc = dBuilder.parse(is);
		Element root = doc.getDocumentElement();
		root.normalize();
		
		NodeList patientsList = root.getChildNodes();
		
		// for each node patient
		for (int i = 0; i < patientsList.getLength(); i++) {
			Node patientNode = patientsList.item(i);
			if (patientNode.getNodeType() == Node.ELEMENT_NODE) {
				
				Patient patient = new Patient();
				HashMap<String, String> symptoms = new HashMap<String, String>();
				String diagnosis = "";
				
				Element patientE = (Element) patientNode;
				
				NodeList symptomsList = patientE.getElementsByTagName(ELEMENT_NAME_SYMTPOM);
				// for each symptom
				for (int j = 0; j < symptomsList.getLength(); j++) {
					Node symptomNode = symptomsList.item(j);
					if (symptomNode.getNodeType() == Node.ELEMENT_NODE) {
						Element symptomE = (Element) symptomNode;
						
						String symptomName = symptomE.getAttribute(ATT_NAME_SYMPTOM_NAME);
						String symptomValue = symptomE.getTextContent();
						symptoms.put(symptomName, symptomValue);
					}
				}
				// get diagnosis
				NodeList diagnosisList = patientE.getElementsByTagName(ELEMENT_NAME_DIAGNOSIS);
				if (diagnosisList.getLength() > 0 && diagnosisList.item(0).getNodeType() == Node.ELEMENT_NODE) {
					Element diagnosisE = (Element) diagnosisList.item(0);
					diagnosis = diagnosisE.getTextContent();
				}
				
				patient.setSymptoms(symptoms);
				patient.setDiagnosis(diagnosis);
				records.add(patient);
			}
		}
		return records;
	}

}
