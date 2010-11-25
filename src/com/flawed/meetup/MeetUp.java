package com.flawed.meetup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MeetUp extends Activity {
    /** Called when the activity is first created. */
    private static final int MENU_ID = Menu.FIRST;
    private static final int CONTEXT_ID = Menu.FIRST + 1;
//	private int numParticipants = 3;
	private ScrollView sv;
	private LinearLayout ll;
	private String JSONString = "{\"event\":\"TestEvent\",\"numParticipants\":3,\"locx\":15,\"locy\":25,\"participants\":[{\"name\":\"Jill\",\"locx\":15,\"locy\":25,\"isClose\":true},{\"name\":\"Claire\",\"locx\":20,\"locy\":20,\"isClose\":true},{\"name\":\"Leon\",\"locx\":25,\"locy\":15,\"isClose\":false}]}";
	private JSONObject JSONEvent;
	private Event testEvent;
	private ServerConnector conn = new ServerConnector();
	
	CheckBox tempCb;
	TextView tempTv;
	Map<String, CheckBox> participantsCb = new HashMap<String, CheckBox>();
	Map<String, TextView> participantsTv = new HashMap<String, TextView>();


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_ID, 0, R.string.menu_pref);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case MENU_ID:
            //createNote();
            return true;
        }  
        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CONTEXT_ID, 0, R.string.context_show);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
        	JSONEvent = new JSONObject(JSONString);
        }catch(JSONException JSON2) {
        	//@TODO
        }
        
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        

        
//        EditText et = new EditText(this);
//        et.setText("Filter:");
//        ll.addView(et);
        
        try {
        	testEvent = new Event(conn.connect());
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