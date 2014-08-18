package com.sample.healthcost.webapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	private static String connectionString = "jdbc:hive2://localhost:10000/default";
	private static String userid = "hive";
	private static String password = "";

	public static Map<String, String> codesAndDesc = new HashMap<String, String>();
	public static Map<String, CostByCode> costByCode = new HashMap<String, CostByCode>();
	public static Map<String, CostByState> costByState = new HashMap<String, CostByState>();
	public static Map<String, CostByCity> costByCity = new HashMap<String, CostByCity>();
	public static String stateSymbols[] = { "AL", "AK", "AZ", "AR", "CA", "CO",
			"CT", "DE", "DC", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
			"KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE",
			"NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA",
			"RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI",
			"WY" };
	public static Map<String, String> stateCodeAndAbbrev = new HashMap<String, String>();
	public static Map<String, List<String>> stateCityMap = new HashMap<String, List<String>>();
	
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet res = null;

	Application() {
		initialize();
	}

	private void initialize() {
    	
    	stateCodeAndAbbrev.put("AL","Alabama");
    	stateCodeAndAbbrev.put("AK","Alaska");
    	stateCodeAndAbbrev.put("AZ","Arizona");
    	stateCodeAndAbbrev.put("AR","Arkansas");
    	stateCodeAndAbbrev.put("CA","California");
    	stateCodeAndAbbrev.put("CO","Colorado");
    	stateCodeAndAbbrev.put("CT","Connecticut");
    	stateCodeAndAbbrev.put("DE","Delaware");
    	stateCodeAndAbbrev.put("DC","District Of Columbia");
    	stateCodeAndAbbrev.put("FL","Florida");
    	stateCodeAndAbbrev.put("GA","Georgia");
    	stateCodeAndAbbrev.put("HI","Hawaii");
    	stateCodeAndAbbrev.put("ID","Idaho");
    	stateCodeAndAbbrev.put("IL","Illinois");
    	stateCodeAndAbbrev.put("IN","Indiana");
    	stateCodeAndAbbrev.put("IA","Iowa");
    	stateCodeAndAbbrev.put("KS","Kansas");
    	stateCodeAndAbbrev.put("KY","Kentucky");
    	stateCodeAndAbbrev.put("LA","Louisiana");
    	stateCodeAndAbbrev.put("ME","Maine");
    	stateCodeAndAbbrev.put("MD","Maryland");
    	stateCodeAndAbbrev.put("MA","Massachusetts");
    	stateCodeAndAbbrev.put("MI","Michigan");
    	stateCodeAndAbbrev.put("MN","Minnesota");
    	stateCodeAndAbbrev.put("MS","Mississippi");
    	stateCodeAndAbbrev.put("MO","Missouri");
    	stateCodeAndAbbrev.put("MT","Montana");
    	stateCodeAndAbbrev.put("NE","Nebraska");
    	stateCodeAndAbbrev.put("NV","Nevada");
    	stateCodeAndAbbrev.put("NH","New Hampshire");
    	stateCodeAndAbbrev.put("NJ","New Jersey");
    	stateCodeAndAbbrev.put("NM","New Mexico");
    	stateCodeAndAbbrev.put("NY","New York");
    	stateCodeAndAbbrev.put("NC","North Carolina");
    	stateCodeAndAbbrev.put("ND","North Dakota");
    	stateCodeAndAbbrev.put("OH","Ohio");
    	stateCodeAndAbbrev.put("OK","Oklahoma");
    	stateCodeAndAbbrev.put("OR","Oregon");
    	stateCodeAndAbbrev.put("PA","Pennsylvania");
    	stateCodeAndAbbrev.put("RI","Rhode Island");
    	stateCodeAndAbbrev.put("SC","South Carolina");
    	stateCodeAndAbbrev.put("SD","South Dakota");
    	stateCodeAndAbbrev.put("TN","Tennessee");
    	stateCodeAndAbbrev.put("TX","Texas");
    	stateCodeAndAbbrev.put("UT","Utah");
    	stateCodeAndAbbrev.put("VT","Vermont");
    	stateCodeAndAbbrev.put("VA","Virginia");
    	stateCodeAndAbbrev.put("WA","Washington");
    	stateCodeAndAbbrev.put("WV","West Virginia");
    	stateCodeAndAbbrev.put("WI","Wisconsin");
    	stateCodeAndAbbrev.put("WY","Wyoming");		
		
		String code;
		String desc;
		String avg;
		String low;
		String high;
		String state;
		String city;
		
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection(connectionString, userid,
					password);
			stmt = con.createStatement();
			
			ResultSet res = stmt.executeQuery("SELECT * FROM treatmentcostbycode");
			while (res.next()) {
				code = res.getString(2);
				desc = res.getString(3);
				avg = res.getString(4);
				low = res.getString(5);
				high = res.getString(6);
				codesAndDesc.put(desc, code);
				costByCode.put(code, new CostByCode(code, desc, avg, low, high));
			}
			
			res = stmt.executeQuery("SELECT * FROM treatmentcostbycodeandstate");
			while (res.next()) {
				code = res.getString(2);
				desc = res.getString(3);
				state = res.getString(4);
				avg = res.getString(5);
				low = res.getString(6);
				high = res.getString(7);
				costByState.put(code+"~"+state, new CostByState(code, desc, state, avg, low, high));
			}
			
			res = stmt.executeQuery("SELECT * FROM treatmentcostbycodestateandcity");
			while (res.next()) {
				code = res.getString(2);
				desc = res.getString(3);
				city = res.getString(4);
				state = res.getString(5);
				avg = res.getString(6);
				low = res.getString(7);
				high = res.getString(8);
				costByCity.put(code+"~"+city+"~"+state, new CostByCity(code, desc, city, state, avg, low, high));
				
				List<String> cityList = new ArrayList<String>();
				if(stateCityMap.get(state) != null)
					cityList = stateCityMap.get(state);
				cityList.add(city);
				stateCityMap.put(state,cityList);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		destroyHiveConnectionObjs();
		
		System.out.println("###################### "+codesAndDesc.size());
		System.out.println("###################### "+costByCode.size());
		System.out.println("###################### "+costByState.size());
		System.out.println("###################### "+costByCity.size());
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	void destroyHiveConnectionObjs() {
		
		try {
			if (res != null)
				res.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
