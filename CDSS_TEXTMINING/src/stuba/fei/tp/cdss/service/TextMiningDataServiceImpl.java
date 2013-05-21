package stuba.fei.tp.cdss.service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import stuba.fei.tp.cdss.dto.Symptom;
import stuba.fei.tp.cdss.parser.XMLSymptomFilter;
import stuba.fei.tp.cdss.parser.XMLgetSymptomName;
import stuba.fei.tp.cdss.utils.XMLFileHelper;

public class TextMiningDataServiceImpl implements TextMiningDataService {

	@Override
	public ArrayList<Symptom> getAvailableSymtpoms(String disease) {
		//test
		ArrayList<Symptom> symptoms;
		 
        XMLgetSymptomName xmlGetSymptomName = new XMLgetSymptomName();
        InputStream fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/diabetes.xml");
        xmlGetSymptomName.load(fileInputStream);
        symptoms = xmlGetSymptomName.getSymptomArray();

        return symptoms;
	}

	@Override
	public String getMedicalRecords(String disease, String[] requiredSymptoms) { 
<<<<<<< HEAD
		
		String recordsXML = XMLFileHelper.convertXMLFileToString("/records.xml");
=======
		XMLSymptomFilter xmlSymptomFilter = new XMLSymptomFilter(requiredSymptoms);
		InputStream fileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/diabetes.xml");
        xmlSymptomFilter.load(fileInputStream);

        String recordsXML = xmlSymptomFilter.getXML();
        System.out.println("Debug: Records - " + recordsXML);
>>>>>>> 03015d3789031415c9c3d341df91253fbb8621f6

        // encode xml with base 64 algorithm
        String encodedXml = Base64.encode(recordsXML.getBytes());
        //System.out.println("Debug: Encoded xml - " + encodedXml);
       
        return encodedXml;
	}

}
