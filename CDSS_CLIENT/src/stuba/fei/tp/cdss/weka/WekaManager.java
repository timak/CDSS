package stuba.fei.tp.cdss.weka;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.Random;
import java.util.Enumeration;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.classifiers.Evaluation;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.LMT;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

public class WekaManager {
	
	
	
	public static void klasifikujData() {
		DecisionTable dt = new DecisionTable();
		JRip jrip = new JRip();			
		LMT lmt = new LMT();
		
		try {
			Instances inst = OpenDatabase();
			inst = diskretizuj(inst);

			inst.setClassIndex(inst.numAttributes() - 1);		                                                                                                                                                                                        
			nastav_klasifikatory(inst, dt, lmt, jrip);

			
			
			String[][] hodnoty = mozne_hodnoty_atr(inst);
			
			//novy pacient 
			Instance i = new DenseInstance(inst.numAttributes());
			i.setDataset(inst);
			
			for(int j=0; j<=inst.numAttributes()-1;j++){
				i.setValue(j, hodnoty[j][0]);
				
			}
			
			//tieto udaje ma vybrat lekar...
			/*i.setValue(0, "\'(-inf-28.25]\'");
			i.setValue(1, "\'(-inf-2]\'");
			i.setValue(2, "\'(-inf-104.25]\'");
			i.setValue(3, "\'(-inf-48.5]\'");
			i.setValue(4, "\'(-inf-8.75]\'");
			i.setValue(5, "\'(-inf-42]\'");
			i.setValue(6, "\'(-inf-28.25]\'");
			i.setValue(7, "\'(-inf-0.69725]\'");	*/
			
			
			//klasifikacia
			System.out.println("Klasifikacia podla jrip: " + jrip.classifyInstance(i));
			System.out.println("Klasifikacia podla dt: " + dt.classifyInstance(i));
			System.out.println("Klasifikacia podla lmt: " + lmt.classifyInstance(i));
		} catch (Exception e) {
			System.out.println("Unable to clasificate data: " + e.toString());
		}
	}
	
	public static Instances OpenDatabase() throws Exception {
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));

		String dbUrl = prop.getProperty("database");
		String dbUser = prop.getProperty("dbuser");
		String dbPwd = prop.getProperty("dbpassword");
		
		InstanceQuery query = new InstanceQuery();
		
		query.setDatabaseURL(dbUrl);
		query.setUsername(dbUser);
		query.setPassword(dbPwd);
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
	
	public static String getpresnost(Classifier clasif, Instances instances) throws Exception{
	    
	    
	    int runs  = 3;
	                    
	    final int numInstance = instances.numInstances();
	                    
	    int folds = (int)Math.floor(instances.numInstances()/3);
	                    
	    Random rand = new Random(1);                           
	                    
	    Evaluation[] evaluations = new Evaluation[runs];
	    

	                        
	    for (int i = 1; i <= runs; i++) {

	                            
	        Evaluation tTest1 = new Evaluation(instances);                                    
	        tTest1.crossValidateModel(clasif, instances, folds*i, rand);
	                            
	        evaluations[i-1]=tTest1;

	                            
	        System.out.println("Run "+i+", NumInstance: "+numInstance+", Folds: "+folds*i + ", Correct: "+tTest1.correct()+" Incorrect: "+tTest1.incorrect());

	                        
	    }

	                     
	                        
	    double avgTcorrect = evaluations[0].correct();
	                        
	    double avgTincorrect =evaluations[0].incorrect();
	                        
	    double sucetC = evaluations[0].correct();
	                        
	    double sucetI = evaluations[0].incorrect();   
	                        
	    int iplusjeden;

	                        
	    for(int i = 1; i < evaluations.length; i++) {
	                            
	                                   
	        iplusjeden=(int)(i+1);
	                            
	        sucetC = sucetC + evaluations[i].correct();
	                            
	        sucetI = sucetI + evaluations[i].incorrect();
	                           
	        avgTincorrect =(sucetI )/iplusjeden;
	                            
	        avgTcorrect =(sucetC )/iplusjeden;
	                            
	    }
	  
	                        
	    double avgTinc1 = avgTincorrect;
	                        
	    double avgTc1 = avgTcorrect;                             
	                                                       
	    return  avgTc1 +","+avgTinc1;



	}
	
	public static String[][] mozne_hodnoty_atr(Instances instances){
		 	
		
		String[][] mozne_hodn_atr = new String[instances.numAttributes()][150];
		int h=0;

	    for (int j=0;j<instances.numAttributes();j++){

	        Attribute attr= instances.attribute(j);
	        Enumeration d= attr.enumerateValues();
	        System.out.print(instances.attribute(j).name()+": ");
	        while(d.hasMoreElements()==true){

	            String x=d.nextElement().toString();
	            mozne_hodn_atr[j][h]=x;
	            System.out.print(mozne_hodn_atr[j][h]+", ");
	            h++;

	        }                       
	        System.out.println();
	        h=0;


	    }	
		
		return mozne_hodn_atr;
	}


	public static void nastav_klasifikatory(Instances i, DecisionTable dt, LMT lmt, JRip jrip )throws Exception{
		
		//decision table - rozhodovacia tabulka
				
				dt.buildClassifier(i);
				dt.setDisplayRules(true);
				System.out.println(dt);
				
				String tmp = getpresnost(dt, i);            
	            double avgDt = Double.parseDouble(tmp.split(",")[0]);                
	            double avgDtInc = Double.parseDouble(tmp.split(",")[1]);			
	            System.out.println( avgDt+"/"+avgDtInc);
				//lmt - rozhodovaci strom
				
				lmt.buildClassifier(i);
				System.out.println(lmt);
				tmp = getpresnost(dt, i);            
	            double avgLmt = Double.parseDouble(tmp.split(",")[0]);                
	            double avgLmtInc = Double.parseDouble(tmp.split(",")[1]);			
	            System.out.println( avgLmt+"/"+avgLmtInc);
				
				// JRip - rozhodovacie pravidlo
				
				jrip.buildClassifier(i);
				System.out.println(jrip);
				tmp = getpresnost(jrip, i);  
	            double avgJrip = Double.parseDouble(tmp.split(",")[0]);                
	            double avgJripInc = Double.parseDouble(tmp.split(",")[1]);			
	            System.out.println( avgJrip+"/"+avgJripInc);
	            
	            String[][] pr= new String[3][2];
	            pr[0][0] = "DecisionTable";
	            Double tmp2 = (avgDt*100)/(avgDt+avgDtInc);
	            pr[0][1] = tmp2.toString();      


	            pr[1][0] = "LMT";
	            tmp2 = (avgLmt*100)/(avgLmt+avgLmtInc);
	            pr[1][1] = tmp2.toString();
	            
	            pr[2][0] = "Jrip";
	            tmp2 = (avgJrip*100)/(avgJrip+avgJripInc);
	            pr[2][1] = tmp2.toString();
	            
	            Arrays.sort(pr,new Comparator< String[]>() {

	                public int compare(String[] o1, String[] o2) {

	                    if(Double.parseDouble(o1[1])>Double.parseDouble(o2[1])) return -1;

	                    if(Double.parseDouble(o1[1])<Double.parseDouble(o2[1])) return 1;

	                    else return 0;

	                }
	            });
	            
	            System.out.println("Hierarchia klasifikatorov podla ich presnosti: ");
	            for(int g=0; g<pr.length  ;g++){
	            	int b = g+1;
	            	System.out.println(b + ".: " + pr[g][0]+ "s presnostou :"+ pr[g][1] );
	            	
	            }
		
	}
}
