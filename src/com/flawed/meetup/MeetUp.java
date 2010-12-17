package com.flawed.meetup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MeetUp extends Activity {
    /** Called when the activity is first created. */
    private static final int CONTEXT_ID = Menu.FIRST + 1;
	private ScrollView sv;
	private LinearLayout ll;
	private JSONObject self;
	private Event testEvent;
	private ServerConnector conn = new ServerConnector();
	private SharedPreferences preferences;
	private String uuid;
	private LocationManager locationManager;
	
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
			}
		return true;
	}
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CONTEXT_ID, 0, R.string.context_show);
        preferences = getSharedPreferences("MUP", MODE_PRIVATE);
	}

/*    @Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
    	case CONTEXT_ID:
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        //mDbHelper.deleteNote(info.id);
	        //fillData();
	        return true;
		}
		return super.onContextItemSelected(item);
	}*/
    
    /** Called when the activity is first created. */
    @Override
    public synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        preferences = getSharedPreferences("MUP", MODE_PRIVATE);
        
        if(!preferences.contains("uuid")) {
        	uuid = UUID.randomUUID().toString();
        	SharedPreferences.Editor editor = preferences.edit();
        	editor.putString("uuid", uuid);
        	editor.putString("firstname", "Sami");
        	editor.putString("lastname", "Test");
        	editor.commit();
        }else {
        	uuid = preferences.getString("uuid", "na");
        }
                
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        
        locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation("gps");
           
		if(location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		
		if(location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}		
  
        try {
        	self = new JSONObject();
        	self.put("first_name", preferences.getString("firstname", "na"));
        	self.put("last_name", preferences.getString("lastname", "na"));
        	self.put("uuid", preferences.getString("uuid", "na"));
        	self.put("loclat", location.getLatitude());
        	self.put("loclong", location.getLongitude());
        	self.put("isClose", "[{\"eId\":0, \"isClose\":TRUE},{\"eId\":1, \"isClose\":TRUE},{\"eId\":2, \"isClose\":TRUE}]");
        	
        	testEvent = new Event(conn.connect(self));
        }catch(JSONException JSON1) {
        	//@TODO
        }catch(IOException IO1) {
        	//@TODO
        }
        
        TextView tv = new TextView(this);
        tv.setText(testEvent.getName());
        ll.addView(tv);
        
        createLayout(testEvent);
        
        setContentView(sv);
        
    } //End of onCreate
    
    public synchronized void createLayout(Event event) {
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
    }//End of createLayout 
}//End of Class