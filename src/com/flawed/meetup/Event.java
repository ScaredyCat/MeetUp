package com.flawed.meetup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Event {
	private String name;
	private int	 numParticipants;
	private double loclong;
	private double loclat;
	public Participant[] participantArray;
	private JSONArray participants;
	
	public Event(JSONObject event) throws JSONException{
		name = event.getString("name");
		numParticipants = event.getInt("numParticipants");
		loclong = event.getDouble("loclong");
		loclat = event.getDouble("loclat");
		participants = event.getJSONArray("participants");
		createParticipants();
	}
	
	private void createParticipants() throws JSONException {
		participantArray = new Participant[this.getNumParticipants()];
		for(int i = 0; i < this.getNumParticipants(); i++) {
			participantArray[i] = new Participant(participants.getJSONObject(i));		
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumParticipants() {
		return numParticipants;
	}
	
	public double getx() {
		return loclong;
	}
	
	public double gety() {
		return loclat;
	}
	public Participant[] getParticipants(){	
		return participantArray;
	}
}
