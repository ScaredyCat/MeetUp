package com.flawed.meetup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerConnector {
	private static final String serverHost = "10.0.2.2";
	private static final int serverPort = 53424;
	private Socket MUSock;
	private PrintWriter out;
	private BufferedReader in;
	private String fromServer;
	private JSONObject JSONEvent;
	
	public ServerConnector(){
		MUSock = null;
		out = null;
		in = null;
	}
	/**
	 * Opens the client/server connections, gets the JSON string, tokenizes it and returns that JSONObject.	
	 * @return Returns the tokenized JSONObject of the event. 
	 *  Usually this is passed straight to the constructor of the event class for easier access.
	 * @throws IOException
	 * @TODO Add the client output part.
	 */
	public JSONObject getEvent(JSONObject self, long eId) throws IOException, JSONException {		
        try {
    		self.put("eId", eId);
            MUSock = new Socket(serverHost, serverPort);
            out = new PrintWriter(MUSock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(MUSock.getInputStream()));
           
            out.println(self);
            if((fromServer = in.readLine()) != null) {
            	JSONEvent = new JSONObject(fromServer);
            }
        } finally {		
    		out.close();
    		in.close();
    		MUSock.close();  	
    	}    
		return JSONEvent;
	}//End of getEvent

	public JSONObject getEventList(JSONObject self) throws IOException, JSONException {
		JSONObject eventList = null;
        try {
            MUSock = new Socket(serverHost, serverPort);
            out = new PrintWriter(MUSock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(MUSock.getInputStream()));
            
            out.println(self);
            if((fromServer = in.readLine()) != null) {
            		eventList = new JSONObject(fromServer);
            }          
        }  finally {      
			out.close();
			in.close();
			MUSock.close();
    	}
    	
		return eventList;
	}
	
}
