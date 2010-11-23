package com.flawed.meetup;

import org.json.JSONException;
import org.json.JSONObject;

public class Participant {
	private String name;
	private int locx;
	private int locy;
	private boolean isClose;
	
	Participant(JSONObject participant) throws JSONException{
		name = participant.getString("name");
		locx = participant.getInt("locx");
		locy = participant.getInt("locy");	
	}
	
	public String getName() {
		return name;
	}
	
	public int getx(){
		return locx;
	}
	
	public int gety() {
		return locy;
	}
	
	public boolean isClose() {
		return isClose;
	}
	

}
