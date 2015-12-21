package com.contact.mycontactlist;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.*;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;

public class ContactListActivity extends ListActivity {
	
	boolean isDeleting = false;
	ContactAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		initSettingsButton();
		initMapButton();
//		initListButton();
	//	setColor();
		initDeleteButton();
		initAddContactButton();
	//	String sortBy = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortField", "contactname");
	//	String sortOrder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
		
		
		ContactDataSource ds = new ContactDataSource(this);
		ds.open();
		// changed removed sortOrder and SortBy
		final ArrayList<Contact> contacts = ds.getContacts("contactname", "ACS");
		ds.close();
		
		if (contacts.size() > 0) {
			
		
		adapter = new ContactAdapter(ContactListActivity.this, contacts);
		setListAdapter(adapter);
		setListAdapter(new ContactAdapter(ContactListActivity.this, contacts));	
		
		ListView listView = getListView();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
				Contact selectedContact = contacts.get(position);
				if(isDeleting) {
					adapter.showDelete(position, itemClicked, ContactListActivity.this, selectedContact);
				}
				else {
				Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
				intent.putExtra("contactid", selectedContact.getContactID());
				startActivity(intent);
			}
		}
	
	});
		}
		else {
			Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_list, menu);
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

//	private void initListButton() {
//		ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
//		list.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent(ContactListActivity.this, ContactListActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//			}
//		});
//	}

	private void initMapButton() {
		ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ContactListActivity.this, ContactMapActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	private void initSettingsButton() {
		ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ContactListActivity.this, ContactSettingsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	private void initDeleteButton() {
		final Button deleteButton = (Button) findViewById(R.id.buttonDelete);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if (isDeleting) {
					deleteButton.setText("Delete");
					isDeleting = false;
					adapter.notifyDataSetChanged();
				}
				else {
					deleteButton.setText("Done Deleting");
					isDeleting = true;
				}
				}
			
		});
		
		
	}
	
	public void setColor() {
		String BKGColor3 = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("bkgcolor", "Orange");
		ScrollView SVL = (ScrollView) findViewById(R.id.scrollView1);
		if (BKGColor3.equalsIgnoreCase("Orange")) {
			SVL.setBackgroundResource(R.color.Orange);
		}
		else {
			SVL.setBackgroundResource(R.color.White);
		}
			
		}
	
	private void initAddContactButton() {
		Button newContact = (Button) findViewById(R.id.buttonAddContact);
		newContact.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(ContactListActivity.this, ContactActivity.class);
				startActivity(intent);
			}
		});
	}
}
