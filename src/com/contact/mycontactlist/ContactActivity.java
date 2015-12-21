package com.contact.mycontactlist;

import com.contact.mycontactlist.DatePickerDialog.SaveDateListener;



import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ContactActivity extends FragmentActivity implements SaveDateListener {
	
	private Contact currentContact;
	//private Contact Address;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		initListButton();
		initTextChangedEvents();
		initMapButton();
		initTextChangedEvents();
		initSettingsButton ();
		initTextChangedEvents();
		initToggleButton ();
		initTextChangedEvents();
		initTextChangedEvents();
		initChangeDateBtton();
		initTextChangedEvents();
		initSaveButton();
	//	setColor();
	//	currentContact = new Contact();
		//Address = new Contact();
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			initContact(extras.getInt("contactid"));
		}
		else {
			currentContact = new Contact();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
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
				Intent intent = new Intent(ContactActivity.this, ContactListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	private void initMapButton() {
		ImageButton list = (ImageButton) findViewById(R.id.imageButtonMap);
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ContactActivity.this, ContactMapActivity.class);
				if (currentContact.getContactID()== -1) {
					Toast.makeText(getBaseContext(), "Contact must be saved before it can be mapped", Toast.LENGTH_LONG).show();
				}
				else {
					intent.putExtra("contactid", currentContact.getContactID());
				}
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	private void initSettingsButton() {
		ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ContactActivity.this, ContactSettingsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	private void initToggleButton() {
		final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
		editToggle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setForEditing(editToggle.isChecked());
			}
		});
	}
	
	private void initSaveButton() {
		Button saveButton = (Button) findViewById(R.id.buttonSave);
		saveButton.setOnClickListener(new View.OnClickListener() { 
			
			@Override
			public void onClick(View v) {
				ContactDataSource ds = new ContactDataSource(ContactActivity.this);
				ds.open();
				boolean wasSuccessful = false;
				if (currentContact.getContactID()==-1) {
					wasSuccessful = ds.insertContact(currentContact);
					int newId = ds.getLastContactId();
					currentContact.setContactID(newId);
				}
				else { wasSuccessful = ds.updateContact(currentContact);
				}
				ds.close();
				if (wasSuccessful) {
					ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
					editToggle.toggle();
					//setForViewing();
					setForEditing(false);
				}
			}
		});
	}
	
	
	private void initTextChangedEvents() {
		final EditText contactName = (EditText) findViewById(R.id.editName);
		contactName.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				currentContact.setContactName(contactName.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// auto generated method stub
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//auto generated method
			}
		});
		final EditText streetAddress = (EditText) findViewById(R.id.editAddress);
		streetAddress.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { currentContact.setStreetAddress(streetAddress.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int agr2, int arg3) {
				//auto gen method stub
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//auto stub method gen
		}
	});
		final EditText city = (EditText) findViewById(R.id.editCity);
		city.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { currentContact.setCity(city.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int agr2, int arg3) {
				//auto gen method stub
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//auto stub method gen
		}
	});
		final EditText state = (EditText) findViewById(R.id.editState);
		state.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { currentContact.setState(state.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int agr2, int arg3) {
				//auto gen method stub
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//auto stub method gen
		}
	});
		final EditText zipCode = (EditText) findViewById(R.id.editZipCode);
		zipCode.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { currentContact.setZipCode(zipCode.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int agr2, int arg3) {
				//auto gen method stub
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//auto stub method gen
		}
	});
		final EditText phoneNumber = (EditText) findViewById(R.id.editHome);
		phoneNumber.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { currentContact.setPhoneNumber(phoneNumber.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int agr2, int arg3) {
				//auto gen method stub
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//auto stub method gen
		}
	});
		final EditText cellNumber = (EditText) findViewById(R.id.editCell);
		cellNumber.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { currentContact.setCellNumber(cellNumber.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int agr2, int arg3) {
				//auto gen method stub
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//auto stub method gen
		}
	});
		final EditText eMail = (EditText) findViewById(R.id.editEmail);
		eMail.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { currentContact.setEmail(eMail.getText().toString());
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int agr2, int arg3) {
				//auto gen method stub
			}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//auto stub method gen
				
		}
	});
		 //phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
		//cellNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
}
		
	private void setForEditing(boolean enabled) {
		EditText editName = (EditText) findViewById(R.id.editName);
		EditText editAddress = (EditText) findViewById(R.id.editAddress);
		EditText editCity = (EditText) findViewById(R.id.editCity);
		EditText editState = (EditText) findViewById(R.id.editState);
		EditText editZipCode = (EditText) findViewById(R.id.editZipCode);
		EditText editPhone = (EditText) findViewById(R.id.editHome);
		EditText editCell = (EditText) findViewById(R.id.editCell);
		EditText editEmail = (EditText) findViewById(R.id.editEmail);
		Button buttonChange = (Button) findViewById(R.id.btnBirthday);
		Button buttonSave = (Button) findViewById(R.id.buttonSave);
		
		editName.setEnabled(enabled);
		editAddress.setEnabled(enabled);
		editCity.setEnabled(enabled);
		editState.setEnabled(enabled);
		editZipCode.setEnabled(enabled);
		editPhone.setEnabled(enabled);
		editCell.setEnabled(enabled);
		editEmail.setEnabled(enabled);
		buttonChange.setEnabled(enabled);
		buttonSave.setEnabled(enabled);
		
	if (enabled) { 
		editName.requestFocus();
		
	}
		
		
	}

	@Override
	public void didFinisheDatePickerDialog(Time selectedTime) {
		TextView birthDay = (TextView) findViewById(R.id.textBirthday);
		birthDay.setText(DateFormat.format("MM/dd/yyyy",  selectedTime.toMillis(false)).toString());
		currentContact.setBirthday(selectedTime);
	}
		private void initChangeDateBtton() {
		Button changeDate = (Button) findViewById(R.id.btnBirthday);
		changeDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick (View v) {
				FragmentManager fm = getSupportFragmentManager();
						
				DatePickerDialog datePickerDialog = new DatePickerDialog();
				datePickerDialog.show(fm, "DatePick");
				
			}
		});
		
	}
		public void setColor() {
			String BKGColor2 = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("bkgcolor", "Orange");
			ScrollView SVM = (ScrollView) findViewById(R.id.scrollView1);
			if (BKGColor2.equalsIgnoreCase("Orange")) {
				SVM.setBackgroundResource(R.color.Orange);
			}
			else {
				SVM.setBackgroundResource(R.color.White);
			}
				
			}
		
		private void initContact(int contactid) {
			ContactDataSource ds = new ContactDataSource(ContactActivity.this);
			ds.open();
			currentContact = ds.getSpecificContact(contactid);
			ds.close();
			
			EditText editName = (EditText) findViewById(R.id.editName);
			EditText editAddress = (EditText) findViewById(R.id.editAddress);
			EditText editCity = (EditText) findViewById(R.id.editCity);
			EditText editState = (EditText) findViewById(R.id.editState);
			EditText editZipCode = (EditText) findViewById(R.id.editZipCode);
			EditText editPhone = (EditText) findViewById(R.id.editHome);
			EditText editCell = (EditText) findViewById(R.id.editCell);
			EditText editEmail = (EditText) findViewById(R.id.editEmail);
			TextView birthDay = (TextView) findViewById(R.id.textBirthday);
			
			editName.setText(currentContact.getContactName());
			
			editAddress.setText(currentContact.getStreetAddress());
			editCity.setText(currentContact.getCity());
			editState.setText(currentContact.getState());
			editZipCode.setText(currentContact.getZipCode());
			editPhone.setText(currentContact.getPhoneNumber());
			editCell.setText(currentContact.getCellNumber());
			editEmail.setText(currentContact.getEmail());
			
			birthDay.setText(DateFormat.format("MM/dd/yyy", currentContact.getBirthday().toMillis(false)).toString());

		}
	
}
			
		
