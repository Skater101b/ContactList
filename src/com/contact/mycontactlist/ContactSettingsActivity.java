package com.contact.mycontactlist;

import android.support.v7.app.ActionBarActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

public class ContactSettingsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_settings);
//		initSettingsButton();
		initMapButton();
		initListButton();
		initSortByClick();
		initSettings();
		initSortOrderClick();
		initChangeBGKColorClick();
	//	setColor();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void initListButton() {
		ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ContactSettingsActivity.this, ContactListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	private void initMapButton() {
		ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ContactSettingsActivity.this, ContactMapActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
//	private void initSettingsButton() {
//		ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
//		list.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent(ContactSettingsActivity.this, ContactSettingsActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//			}
//		});
//	}
	
	private void initSettings() {
		String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield","contactname");
		String sortOrder = getSharedPreferences("MyContactListPreferences" , Context.MODE_PRIVATE).getString("sortorder","ASC");
		String BKGColor = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("bkgcolor", "Orange");
		
		RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
		RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
		RadioButton rbBirthday = (RadioButton) findViewById(R.id.radioBirthday);
			if (sortBy.equalsIgnoreCase("contactname")) {
				rbName.setChecked(true);
			}
			else if (sortBy.equalsIgnoreCase("city")) {
				rbCity.setChecked(true);
			}
			else { 
				rbBirthday.setChecked(true);
			}
			
			RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
			RadioButton rbDescending = (RadioButton) findViewById(R.id.radioDescending);
				if (sortOrder.equalsIgnoreCase("ASC")) {
					rbAscending.setChecked(true);
				}
				else { 
					rbDescending.setChecked(true);
				}
				
			RadioButton rgWhite = (RadioButton) findViewById(R.id.radioColorWhite);
			RadioButton rgOrange = (RadioButton) findViewById(R.id.radioColorOrange);
				if (BKGColor.equalsIgnoreCase("Orange")) {
					rgOrange.setChecked(true);
				}
				else {
					rgWhite.setChecked(true);
				}
	}
	
	private void initSortByClick() {
		RadioGroup rgSortBy = (RadioGroup) findViewById(R.id.radioGroup1);
		rgSortBy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				RadioButton rbName = (RadioButton) findViewById(R.id.radioName);
				RadioButton rbCity = (RadioButton) findViewById(R.id.radioCity);
				if (rbName.isChecked()) {
					getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortfield", "contactname").commit();
				}
				else if (rbCity.isChecked()) {
					getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortfield", "city").commit();
				}
				else {
					getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortfield", "birthday").commit();
				}
			}
		});
			
		}
	private void initSortOrderClick() {
		RadioGroup rgSortOrder = (RadioGroup) findViewById(R.id.radioGroup2);
		rgSortOrder.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				RadioButton rbAscending = (RadioButton) findViewById(R.id.radioAscending);
				if (rbAscending.isChecked()) {
					getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortOrder", "ASC").commit();
				}
				else { 
					getSharedPreferences("MyContactListPreferences", MODE_PRIVATE).edit().putString("sortOrder", "DESC").commit();
				
				}
			}
		});
	}
	
	private void initChangeBGKColorClick() {
		RadioGroup rgColor = (RadioGroup) findViewById(R.id.radioGroup3);
		rgColor.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				RadioButton rbOrange = (RadioButton) findViewById(R.id.radioColorOrange);
				
				if (rbOrange.isChecked()) {
					getSharedPreferences("MyContactListPreferences" , MODE_PRIVATE).edit().putString("bkgcolor","Orange").commit(); 
				}
				else { 
					getSharedPreferences("MyContactListPreferences" , MODE_PRIVATE).edit().putString("bkgcolor","White").commit();
				}
			}
		});
		
	}
	
	public void setColor() {
		String BKGColor1 = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("bkgcolor", "Orange");
		ScrollView SV = (ScrollView) findViewById(R.id.scrollView2);
		if (BKGColor1.equalsIgnoreCase("Orange")) {
			SV.setBackgroundResource(R.color.Orange);
		}
		else {
			SV.setBackgroundResource(R.color.White);
		}
			
		}
			
		
	}
			
	
		
	
	

