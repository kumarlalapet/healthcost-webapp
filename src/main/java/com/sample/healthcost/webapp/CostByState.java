package com.sample.healthcost.webapp;

public class CostByState {

	private String code;
	private String desc;
	private String state;
	private String avg;
	private String low;
	private String high;
	
	public CostByState(String code, String desc, String state, String avg,
			String low, String high) {
		super();
		this.code = code;
		this.desc = desc;
		this.state = state;
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

	public String getState() {
		return state;
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
