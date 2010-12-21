package com.flawed.meetup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Preferences extends PreferenceActivity{
	private SharedPreferences preferences;
	private static final int UUID_TOAST_ID = Menu.FIRST;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences("MUP", MODE_PRIVATE);
//		SharedPreferences.Editor editor = preferences.edit();
		
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, UUID_TOAST_ID, 0, R.string.menu_uuid);
        return true;
    }
	
//	@Override
//	public void onStop() {
//		preferences = getSharedPreferences("MUP", MODE_PRIVATE);
//		SharedPreferences.Editor editor = preferences.edit();
//
//	}
	   
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case UUID_TOAST_ID:
            showUUID();
            return true;
        }
       
        return true;
	}
    
    public void showUUID() {
    	String uuid = preferences.getString("uuid", "n/a");
    	Toast.makeText(Preferences.this,
				"Your saved uuid: " + uuid,
				Toast.LENGTH_LONG).show();
    }
}
