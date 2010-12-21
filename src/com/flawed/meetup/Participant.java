package com.flawed.meetup;

import org.json.JSONException;
import org.json.JSONObject;

public class Participant {
	private String first_name;
	private String last_name;
	private String uuid;
	private int loclat;
	private int loclong;
	private boolean isClose;
	
	Participant(JSONObject participant) throws JSONException{
		first_name = participant.getString("first_name");
		last_name = participant.getString("last_name");
		loclat = participant.getInt("loclat");
		loclong = participant.getInt("loclong");	
		isClose = participant.getBoolean("isClose");
		uuid = participant.getString("uuid");
	}
	
	public String getName() {
		String name = first_name + " " + last_name;
		return name;
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public String getLastName() {
		return last_name;
	}
	
	public int getlat(){
		return loclat;
	}
	
	public int getlong() {
		return loclong;
	}
	
	public boolean isClose() {
		return isClose;
	}
	

}
