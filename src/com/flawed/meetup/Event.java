package com.flawed.meetup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Event {
	private String name;
	private int	 numParticipants;
	private int locx;
	private int locy;
	public Participant[] participantArray;
	private JSONArray participants;
	
	public Event(JSONObject event) throws JSONException{
		name = event.getString("event");
		numParticipants = event.getInt("numParticipants");
		locx = event.getInt("locx");
		locy = event.getInt("locy");
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
	
	public int getx() {
		return locx;
	}
	
	public int gety() {
		return locy;
	}
	public Participant[] getParticipants(){	
		return participantArray;
	}
}
