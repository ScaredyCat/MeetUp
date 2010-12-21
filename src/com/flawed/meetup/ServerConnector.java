package com.flawed.meetup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerConnector {
	private static final String serverHost = "10.0.2.2";
	private static final int serverPort = 53424;
	private Socket MUSock;
	private PrintWriter out;
	private BufferedReader in;
	private String fromServer;
	private String toServer;
	private JSONObject JSONEvent;
	private JSONObject self;
	
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
	public JSONObject connect(JSONObject self) throws IOException {
		this.self = self;
		toServer = self.toString();
        try {
            MUSock = new Socket(serverHost, serverPort);
            out = new PrintWriter(MUSock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(MUSock.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " +serverHost);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " +serverHost);
            System.exit(1);
        }          
        out.println(self);
        if((fromServer = in.readLine()) != null) {
        	try {
        		JSONEvent = new JSONObject(fromServer);
        	}catch(JSONException JSON1) {
        		JSON1.printStackTrace();
        		System.out.println("ERROR OLOL!");
        	}
        }

		out.close();
		in.close();
		MUSock.close();
        
		return JSONEvent;
	}

}
