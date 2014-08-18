package com.sample.healthcost.webapp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCostController {

    @RequestMapping("/getallcodes")
    public Map<String, String> getAllCodes() {
    	return Application.codesAndDesc;
    }
    
    @RequestMapping("/getcostbycode")
    public CostByCode getCostByCode(@RequestParam(value="code", required=false, defaultValue="") String code) {
    	return Application.costByCode.get(code);
    }
    
    @RequestMapping("/getcostbystate")
    public CostByState getCostByState(@RequestParam(value="code", required=false, defaultValue="") String code,@RequestParam(value="state", required=false, defaultValue="") String state) {
    	return Application.costByState.get(code+"~"+state);
    }
    
    @RequestMapping("/getcostbycity")
    public CostByCity getCostByCity(@RequestParam(value="code", required=false, defaultValue="") String code,@RequestParam(value="state", required=false, defaultValue="") String state,@RequestParam(value="city", required=false, defaultValue="") String city) {
    	return Application.costByCity.get(code+"~"+city+"~"+state);
    }
    
    @RequestMapping("/getstateswithcode")
    public Map<String, String> getStatesWithCode(@RequestParam(value="code", required=false, defaultValue="") String code) {
    	Map<String, String> result = new HashMap<String, String>();
    	for(String symbol : Application.stateSymbols) {
    		if(Application.costByState.get(code+"~"+symbol) != null) {
    			result.put(symbol, Application.stateCodeAndAbbrev.get(symbol));
    		}
    	}
    	return result;
    }    
    
    @RequestMapping("/getcitywithcode")
    public Map<String, String> getCityWithCode(@RequestParam(value="code", required=false, defaultValue="") String code,@RequestParam(value="state", required=false, defaultValue="") String state) {
    	Map<String, String> result = new HashMap<String, String>();
    	for(String city : Application.stateCityMap.get(state)) {
    		if(Application.costByCity.get(code+"~"+city+"~"+state) != null) {
    			result.put(city,city);
    		}
    	}
    	return result;
    }
    
}
