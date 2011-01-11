package com.flawed.meetup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MeetUp extends Activity {
    /** Called when the activity is first created. */
	
	private static final String PROXIMITY_ACTION = "com.flawed.meetup.action.PROXIMITY_ALERT";
	
	private ScrollView sv;
	private IntentFilter intentFilter;
	private LinearLayout ll;
	private JSONObject self;
	private JSONObject eventList;
	private JSONArray isCloseArray;
	private ServerConnector conn = new ServerConnector();
	private SharedPreferences cPreferences;
	private SharedPreferences dPreferences;
	private String uuid;
	private Location location;
		
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
				Intent i = new Intent(MeetUp.this, Preferences.class);
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
        
        intentFilter = new IntentFilter(PROXIMITY_ACTION);
        
        Bundle extras = getIntent().getExtras();
        
//        updateUuid();
//        if(uuid.equals("na")) {
//        	updateUuid();
//        }
       	
        location = getLocation();
        if(extras != null) {
	        try {
	        	isCloseArray = new JSONArray();
	        	isCloseArray.put(new JSONObject().put("eId", extras.getLong("eId")));
	        	isCloseArray.put(new JSONObject().put("isClose", extras.getBoolean("isClose")));
	        }catch(JSONException json2) {
	        	json2.printStackTrace();
	        }
        }
        if(dPreferences.contains("firstname") && dPreferences.contains("lastname") && dPreferences.contains("uuid")) {
        	
        	self = createSelf();       
	        try {
	    		eventList = conn.getEventList(self);
	        }catch(JSONException json1){
	        	json1.printStackTrace();
	        }catch(IOException io1) {
	        	io1.printStackTrace();
	        }
	        
	        createLayout(eventList);
        }else {
        	Toast.makeText(MeetUp.this,
    				"Please input your name in the preferences dialog.",
    				Toast.LENGTH_LONG).show();
        }
       

                
    } //End of onCreate(savedInstanceState)
    
    public synchronized void onCreate() {        
        cPreferences = getSharedPreferences("MUP", MODE_PRIVATE);
        dPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
//        updateUuid();
//        if(uuid.equals("na")) {
//        	updateUuid();
//        }
       	
        location = getLocation();
        
        if(dPreferences.contains("firstname") && dPreferences.contains("lastname") && dPreferences.contains("uuid")) {      	    
	        try {
	        	if(isCloseArray == null)
	        		isCloseArray = new JSONArray();
	        	
	        	self = createSelf();   
	    		eventList = conn.getEventList(self);
	    		isCloseArray = new JSONArray();
	    		for(int i=0; i < eventList.getInt("numEvents"); i++) {
	    			
	    			JSONArray eventArray = eventList.getJSONArray("eventArray");
	    			long loclat = eventArray.getJSONObject(i).getLong("loclat");
	    			long loclong = eventArray.getJSONObject(i).getLong("loclong");
	    			long eid = eventArray.getJSONObject(i).getLong("eId");
	    			if(location.getLongitude() == loclong && location.getLatitude() == loclat) {
	    				JSONObject temp = new JSONObject();
	    				temp.put("eId", eid);
	    				temp.put("isClose", true);
	    				isCloseArray.put(temp);
	    			}else {
	    				JSONObject temp = new JSONObject();
	    				temp.put("eId", eid);
	    				temp.put("isClose", false);
	    				isCloseArray.put(temp);
	    			}
	    		}
//	    		for(int i=0; i < eventList.getInt("numEvents"); i++) {
//	    			JSONArray eventArray = eventList.getJSONArray("eventArray");
//	    			long loclat = eventArray.getJSONObject(i).getLong("loclat");
//	    			long loclong = eventArray.getJSONObject(i).getLong("loclong");
//	    			long eid = eventArray.getJSONObject(i).getLong("eId");
//	    			setProximityAlert(loclat, loclong, eid);
//	    		}
	        }catch(JSONException json1){
	        	json1.printStackTrace();
	        }catch(IOException io1) {
	        	io1.printStackTrace();
	        }
	        createLayout(eventList);
        }else {
        	Toast.makeText(MeetUp.this,
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
        	//self.put("isClose", isCloseArray.toString());
        	
        }catch(JSONException JSON1) {
        	JSON1.printStackTrace();
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
    
    private void setProximityAlert(double lat, double lon, final long eventID){
     // 100 meter radius
     float radius = 100f;
     
     int requestCode = 0;
    
     long expiration = 0;
    
     LocationManager locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
    
     Intent intent = new Intent(PROXIMITY_ACTION);
     intent.putExtra("eId", eventID);
     intent.putExtra("isClose", true);
     PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    
     locationManager.addProximityAlert(lat, lon, radius, expiration, pendingIntent);
    }
    
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

    
    public synchronized void createLayout(JSONObject eventList) {
    	try {        
	        sv = new ScrollView(this);
	        ll = new LinearLayout(this);
	        ll.setOrientation(LinearLayout.VERTICAL);
	        sv.addView(ll);
	        
	        TextView tv = new TextView(this);
	        tv.setText("List of your Events");
	        ll.addView(tv);
	    	
	        for(int i=0; i < eventList.getInt("numEvents"); i++) {
	        	
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
	        	tempTv.setId(eventList.getJSONArray("eventArray").getJSONObject(i).getInt("eId"));
	        	tempTv.setText(eventList.getJSONArray("eventArray").getJSONObject(i).getString("name"));
	        	tempTv.setPadding(5, 0, 0, 0);
	        	tempTv.setGravity(Gravity.RIGHT);
	        	tempTv.setOnClickListener(new View.OnClickListener() {	        		
	        		long eId = tempTv.getId();
	        		JSONArray iisCloseArray = isCloseArray;
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MeetUp.this, EventView.class);
						intent.putExtra("eId", eId);
						intent.putExtra("isCloseArray", iisCloseArray.toString());
						startActivity(intent);						
					}
				});
	        	
	        	if(eventList.getJSONArray("eventArray").getJSONObject(i)
	        			.getLong("loclat") == location.getLatitude() 
	        	&& eventList.getJSONArray("eventArray").getJSONObject(i)
	        			.getLong("loclong") == location.getLongitude()) {
	        		tempCb.setChecked(true);
	        	}
    	      	
	        	llin.addView((CheckBox) participantsCb.get("id"+i));
	        	llin.addView((TextView) participantsTv.get("id"+i));
	        	
	        	ll.addView(llin);
	        }
    	}catch(JSONException e) {
    		e.printStackTrace();
    	}
        setContentView(sv);
    }//End of createLayout 
}//End of Class