package com.flawed.meetup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class EventView extends Activity {
	private ScrollView sv;
	private LinearLayout ll;
	private JSONObject self;
	private JSONArray isCloseArray;
	private Event testEvent;
	private ServerConnector conn = new ServerConnector();
	private SharedPreferences cPreferences;
	private SharedPreferences dPreferences;
	private String uuid;
	private Location location;
	private long eId = -1;
	
	CheckBox tempCb;
	TextView tempTv;
	Map<String, CheckBox> participantsCb = new HashMap<String, CheckBox>();
	Map<String, TextView> participantsTv = new HashMap<String, TextView>();


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// We have only one menu option
			case R.id.preferences:
				// Launch Preference activity
				Intent i = new Intent(EventView.this, Preferences.class);
				startActivity(i);
				break;
			case R.id.refresh:
				this.onCreate();
			}
		return true;
	}
        
    /** Called when the activity is first created. */
    @Override
    public synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        cPreferences = getSharedPreferences("MUP", MODE_PRIVATE);
        dPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());        

        Bundle extras = getIntent().getExtras();
        eId = extras.getLong("eId");
        try {
        	isCloseArray = new JSONArray(extras.getString("isCloseArray"));
        }catch(JSONException e) {
        	e.printStackTrace();
        }
//        updateUuid();
//        if(uuid.equals("na")) {
//        	updateUuid();
//        }
       	
        location = getLocation();
        
        if(dPreferences.contains("firstname") && dPreferences.contains("lastname") && dPreferences.contains("uuid")) {
        	
        	self = createSelf();       
	        try {
	    		testEvent = new Event(conn.getEvent(self, eId));
	        }catch(JSONException json1){
	        	json1.printStackTrace();
	        }catch(IOException io1) {
	        	io1.printStackTrace();
	        }
	        createLayout(testEvent);
        }else {
        	Toast.makeText(EventView.this,
    				"Please input your names in the preferences dialog.",
    				Toast.LENGTH_LONG).show();
        }
       

                
    } //End of onCreate(savedInstanceState)
    
    public synchronized void onCreate() {        
        cPreferences = getSharedPreferences("MUP", MODE_PRIVATE);
        dPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        Bundle extras = getIntent().getExtras();
        eId = extras.getLong("eId");
        
//        updateUuid();
//        if(uuid.equals("na")) {
//        	updateUuid();
//        }
       	
        location = getLocation();
        
        if(dPreferences.contains("firstname") && dPreferences.contains("lastname") && dPreferences.contains("uuid")) {
        	
        	self = createSelf();       
	        try {
	    		testEvent = new Event(conn.getEventList(self));
	        }catch(JSONException json1){
	        	json1.printStackTrace();
	        }catch(IOException io1) {
	        	io1.printStackTrace();
	        }
	        createLayout(testEvent);
        }else {
        	Toast.makeText(EventView.this,
    				"Please input your names in the preferences dialog, you can access it from the menu.",
    				Toast.LENGTH_LONG).show();
        }
       

                
    } //End of onCreate
    
    @Override
    public void onStart() {
    	super.onStart();
    	this.onCreate();
    }
    
    public synchronized JSONObject createSelf() {
        try {
        	self = new JSONObject();
        	self.put("first_name", dPreferences.getString("firstname", "na"));
        	self.put("last_name", dPreferences.getString("lastname", "na"));
        	self.put("uuid", dPreferences.getString("uuid", "na"));
        	self.put("loclat", location.getLatitude());
        	self.put("loclong", location.getLongitude());
        	self.put("isClose", isCloseArray.toString());
        	
        }catch(JSONException JSON1) {
        	//@TODO
        }
    	return self;
    }
    
    public synchronized Location getLocation() {
        LocationManager locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation("gps");
           
		if(location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		
		if(location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
    } //End of getLocation
    
//    public synchronized void updateUuid() {
//	    if(!cPreferences.contains("uuid")) {
//	    	uuid = UUID.randomUUID().toString();
//	    	SharedPreferences.Editor editor = cPreferences.edit();
//	    	editor.putString("uuid", uuid);
//	    	editor.commit();
//	    }else {
//	    	uuid = cPreferences.getString("uuid", "na");	    	
//	    }
//    	
//    }//End of updateUuid

    
    public synchronized void createLayout(Event event) {
    	        
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        
        TextView tv = new TextView(this);
        tv.setText(testEvent.getName());
        ll.addView(tv);
    	
        for(int i=0; i < event.getNumParticipants(); i++) {
        	
            LinearLayout llin = new LinearLayout(this);
            llin.setOrientation(LinearLayout.HORIZONTAL);
            llin.setPadding(1, 2, 2, 1);
            
        	participantsCb.put("id"+i, new CheckBox(this));
        	participantsTv.put("id"+i, new TextView(this));
        	
        	tempCb = (CheckBox)participantsCb.get("id"+i);        	
        	tempTv = (TextView)participantsTv.get("id"+i);
        	
        	tempCb.setPadding(0, 0, 5, 0);
        	tempCb.setId(i);
        	tempCb.setClickable(false);
        	tempTv.setId(i);
        	tempTv.setText(event.participantArray[i].getName());
        	tempTv.setPadding(5, 0, 0, 0);
        	tempTv.setGravity(Gravity.RIGHT);
        	
        	if(event.participantArray[i].isClose() == true) {
        		tempCb.setChecked(true);
        	}
        	      	
        	llin.addView((CheckBox) participantsCb.get("id"+i));
        	llin.addView((TextView) participantsTv.get("id"+i));
        	
        	ll.addView(llin);
        }
        setContentView(sv);
    }//End of createLayout 
}
