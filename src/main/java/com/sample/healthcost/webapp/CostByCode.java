package com.sample.healthcost.webapp;

public class CostByCode {

	private String code;
	private String desc;
	private String avg;
	private String low;
	private String high;
	
	public CostByCode(String code, String desc, String avg, String low,
			String high) {
		super();
		this.code = code;
		this.desc = desc;
		this.avg = avg;
		this.low = low;
		this.high = high;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public String getAvg() {
		return avg;
	}

	public String getLow() {
		return low;
	}

	public String getHigh() {
		return high;
	}
	
	
}
