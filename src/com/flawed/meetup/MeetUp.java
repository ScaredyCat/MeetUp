package com.flawed.meetup;

import java.util.HashMap;
import java.util.Map;

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
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MeetUp extends Activity {
    /** Called when the activity is first created. */
    private static final int MENU_ID = Menu.FIRST;
    private static final int CONTEXT_ID = Menu.FIRST + 1;
	CheckBox[] store;
	CheckBox tempCb;
	TextView tempTv;
	Map<String, CheckBox> participantsCb = new HashMap<String, CheckBox>();
	Map<String, TextView> participantsTv = new HashMap<String, TextView>();
	int j = 20;

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

    @Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
    	case CONTEXT_ID:
    		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        //mDbHelper.deleteNote(info.id);
	        //fillData();
	        return true;
		}
		return super.onContextItemSelected(item);
	}
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        
        EditText et = new EditText(this);
        et.setText("Filter:");
        ll.addView(et);
        
        store = new CheckBox[j];
        for(int i=0; i < j; i++) {
        	
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
        	tempTv.setText("Dynamic no "+(i+1));
        	tempTv.setPadding(5, 0, 0, 0);
        	tempTv.setGravity(Gravity.RIGHT);
        	
        	llin.addView((CheckBox) participantsCb.get("id"+i));
        	llin.addView((TextView) participantsTv.get("id"+i));
        	
        	ll.addView(llin);
        }
        
        tempCb = (CheckBox)participantsCb.get("id4");
        tempCb.setChecked(true);
        tempCb = (CheckBox)participantsCb.get("id8");
        tempCb.setChecked(true);
        tempCb = (CheckBox)participantsCb.get("id1");
        tempCb.setChecked(true);
        tempCb = null;
        
        
        setContentView(sv);
        
    }
}