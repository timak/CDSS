package stuba.fei.tp.cdss.weka;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import stuba.fei.tp.cdss.dto.Patient;

public class WekaDbCoordinator {
	
	private Connection conn = null;
	
	public static final String DB_TABLE_NAME = "diabetes_tmp";  
	
	
	public void createWekaData(ArrayList<Patient> recordsData) throws Exception {
		createConnection();
		createTable(recordsData);
		insertData(recordsData);
	}
	
	/**
	 * Method creates connection for DB
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	private void createConnection() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));

		String dbUrl = prop.getProperty("database");
		String dbUser = prop.getProperty("dbuser");
		String dbPwd = prop.getProperty("dbpassword");
		
		Class.forName("com.mysql.jdbc.Driver");
		conn = (Connection) DriverManager.getConnection(dbUrl, dbUser, dbPwd);
	}
	
	/**
	 * Closes connection
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("Unable to close connection: " + e.toString());
		}
	}
	
	/**
	 * Creates temporary table for weka
	 * @param recordsData
	 * @throws SQLException 
	 */
	private void createTable(ArrayList<Patient> recordsData) throws SQLException {
		Patient pZero = null;
		if (recordsData.size() > 0) {
			pZero = recordsData.iterator().next();
		}
		if (pZero == null) {
			System.out.println("No patient in records");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + DB_TABLE_NAME + " ");
		sb.append("(id INTEGER not NULL AUTO_INCREMENT, ");
		for (Entry<String, String> entry : pZero.getSymptoms().entrySet()) {
			sb.append(" " + entry.getKey() + " VARCHAR(255), ");
		}
		sb.append(" diagnosis VARCHAR(255), ");
		sb.append(" PRIMARY KEY ( id ))");
		
		System.out.println("Debug: query: " + sb.toString());
		
		Statement stmt = (Statement) conn.createStatement();
		stmt.executeUpdate(sb.toString());
	}
	
	/**
	 * Method drops table diabetes
	 * @throws SQLException
	 */
	public void dropTable() throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE " + DB_TABLE_NAME);
		Statement stmt = (Statement) conn.createStatement();
		stmt.executeUpdate(sb.toString());
	}
	
	/**
	 * Fills db table with data from Patient
	 * 
	 * @param recordsData
	 * @throws SQLException
	 */
	private void insertData(ArrayList<Patient> recordsData)  throws SQLException {
		for (Patient patient : recordsData) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO " + DB_TABLE_NAME + " ");
			
			String fieldNames = "( ";
			String values = "( ";
			
			for (Entry<String, String> entry : patient.getSymptoms().entrySet()) {
				fieldNames += " " + entry.getKey() + ", ";
				values += " '" + entry.getValue() + "', ";
			}
			
			fieldNames += " diagnosis)";
			values += "'" + patient.getDiagnosis() + "' )";
			
			sb.append(fieldNames);
			sb.append(" VALUES ");
			sb.append(values);
			
			System.out.println("Debug: query - " + sb.toString());
			
			Statement stmt = (Statement) conn.createStatement();
			stmt.executeUpdate(sb.toString());
		}
	}

}
