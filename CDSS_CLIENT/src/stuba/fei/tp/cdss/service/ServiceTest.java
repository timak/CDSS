package stuba.fei.tp.cdss.service;

import java.rmi.RemoteException;
import java.util.ArrayList;

import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetAvailableSymtpoms;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetAvailableSymtpomsResponse;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetMedicalRecords;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.GetMedicalRecordsResponse;
import stuba.fei.tp.cdss.service.TextMiningDataServiceImplStub.Symptom;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.LMT;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

public class ServiceTest {

	
	
	public static void main(String[] args) throws Exception  {
		
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
			System.out.println("Error calling testGetMedicalRecords: " + e.toString());
		}
		
		DecisionTable dt = new DecisionTable();
		JRip jrip = new JRip();			
		LMT lmt = new LMT();
		
			Instances inst = OpenDatabase();
			inst = diskretizuj(inst);

			inst.setClassIndex(inst.numAttributes() - 1);		                                                                                                                                                                                        
			nastav_klasifikatory(inst, dt, lmt, jrip);

			
			//novy pacient 
			Instance i = new DenseInstance(inst.numAttributes());
			i.setDataset(inst);
			
			//tieto udaje ma vybrat lekar...
			i.setValue(0, "\'(-inf-28.25]\'");
			i.setValue(1, "\'(-inf-2]\'");
			i.setValue(2, "\'(-inf-104.25]\'");
			i.setValue(3, "\'(-inf-48.5]\'");
			i.setValue(4, "\'(-inf-8.75]\'");
			i.setValue(5, "\'(-inf-42]\'");
			i.setValue(6, "\'(-inf-28.25]\'");
			i.setValue(7, "\'(-inf-0.69725]\'");		
			
			
			//klasifikacia
			System.out.println("Klasifikacia podla jrip: " + jrip.classifyInstance(i));
			System.out.println("Klasifikacia podla dt: " + dt.classifyInstance(i));
			System.out.println("Klasifikacia podla lmt: " + lmt.classifyInstance(i));
			
		
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
		
		request.setRequiredSymptoms(new String[] {"sym1", "sym2", "sym3"});
		
		GetMedicalRecordsResponse response = stub.getMedicalRecords(request);
		String xmlData = response.get_return();
		System.out.println("Response: " + xmlData);
	}
	
	public static Instances OpenDatabase() throws Exception{
		InstanceQuery query = new InstanceQuery();
		
		query.setDatabaseURL("jdbc:mysql://localhost:3306/timak");
		query.setUsername("root");
		query.setPassword("");
		query.setQuery("select * from diabetes");
		
		 Instances data = query.retrieveInstances();
		 
		 return data;
	}	

	public static Instances diskretizuj(Instances inst) throws Exception{
		
		String[] opt = new String[1];   

	    double cislo;
		int pom;
		

		Discretize diskretizacia= new Discretize();
		for(int i=0; i< inst.numAttributes();i++)
		{ 
			if(inst.attribute(i).isNumeric()){

	            cislo = inst.numDistinctValues(i);          
	            
	            pom=i+1;
	            opt[0]=pom+"";
	            diskretizacia.setOptions(opt);
	            diskretizacia.setBins((int)(cislo/1.25));
	            
	            diskretizacia.setInputFormat(inst);
	            inst = Filter.useFilter(inst,diskretizacia);

	            System.out.println("Data po predspracovani:\n"+inst);
			}
		}	
		return inst;
		
	}

	public static void nastav_klasifikatory(Instances i, DecisionTable dt, LMT lmt, JRip jrip )throws Exception{
		
		//decision table - rozhodovacia tabulka
				
				dt.buildClassifier(i);
				dt.setDisplayRules(true);
				System.out.println(dt);
				
				//lmt - rozhodovaci strom
				
				lmt.buildClassifier(i);
				System.out.println(dt);
				
				// JRip - rozhodovacie pravidlo
				
				jrip.buildClassifier(i);
				System.out.println(jrip);
		
	}

}
