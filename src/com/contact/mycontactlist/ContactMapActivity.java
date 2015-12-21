package com.contact.mycontactlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;


public class ContactMapActivity extends FragmentActivity {
	
	GoogleMap googleMap;

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_map);
		initSettingsButton();
		initListButton();
		initMapTypeButton();
		initLocationButton();

		googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		//for contacts location
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		Contact currentContact = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
			ds.open();
			currentContact = ds.getSpecificContact(extras.getInt("contactid"));
			ds.close();
		}
		else {
			ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
			ds.open();
			contacts = ds.getContacts("contactname", "ASC");
			ds.close();
		}
		
		int measuredWidth = 0;
		int measuredHeight = 0;
		Point size = new Point();
		WindowManager w = getWindowManager();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			w.getDefaultDisplay().getSize(size);
			measuredWidth = size.x;
			measuredHeight = size.y;
		}
		else {
			Display d = w.getDefaultDisplay();
			measuredWidth = d.getWidth();
			measuredHeight = d.getHeight()-180;
		}
		if (contacts.size()>0) {
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			for (int i=0; i<contacts.size(); i++) {
				currentContact = contacts.get(i);
				
				Geocoder geo = new Geocoder(this);
				List<Address> addresses = null;
				
				String address = currentContact.getStreetAddress() + ", " + currentContact.getCity() + ", " + currentContact.getState() + ", " + currentContact.getZipCode();
				
				try {
					addresses = geo.getFromLocationName(address,  1);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
				builder.include(point);
				
				googleMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address).icon(BitmapDescriptorFactory.fromResource(R.drawable.aaaaaaa)));
			}
			googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), measuredWidth, measuredHeight, 100));
		}
		else {
			if (currentContact != null) {
				Geocoder geo = new Geocoder(this);
				List<Address> addresses = null;
				
				String address = currentContact.getStreetAddress() + ", " + currentContact.getCity() + ", " + currentContact.getState() + ", " + currentContact.getZipCode();
				
				try { 
					addresses = geo.getFromLocationName(address, 1);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0) .getLongitude());
				
				googleMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
			}
			else {
				AlertDialog alertDialog = new AlertDialog.Builder(ContactMapActivity.this).create();
				alertDialog.setTitle("No Data");
				alertDialog.setMessage("No data avaialble for the map function.");
				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					} });
				alertDialog.show();
				
		
				
				}
			
		}
		
		}
		
		// for my location
	//	googleMap.setMyLocationEnabled(true);
	//	googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
	//		//@Override
	//		public void onMyLocationChange(Location location) {
	//			LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
	//			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,11));
	//			Toast.makeText(getBaseContext(), "Lat: "+ location.getLatitude() + "long: " + location.getLongitude()+" Accuracy: "+ location.getAccuracy(), Toast.LENGTH_LONG).show();
	//		}
	//	});
	//}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_map, menu);
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
	
	private void initLocationButton() {
		final Button locationbtn = (Button) findViewById(R.id.buttonShowMe);
		locationbtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String currentSetting = locationbtn.getText().toString();
				if (currentSetting.equalsIgnoreCase("Location On")) {
					locationbtn.setText("Location Off");
					googleMap.setMyLocationEnabled(true);
				}
				else {
					locationbtn.setText("Location On");
					googleMap.setMyLocationEnabled(false);
					
				}
			}
		});
	}
	
	private void initMapTypeButton() {
		final Button satelitebtn = (Button) findViewById(R.id.buttonMapType);
		satelitebtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String currentSetting = satelitebtn.getText().toString();
				if (currentSetting.equalsIgnoreCase("Satellite View")) {
					googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					satelitebtn.setText("normal View");
				}
				else {
					googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					satelitebtn.setText("Satellite View");
				}
			}
		});

	}
	
	
	private void initListButton() {
		ImageButton list = (ImageButton) findViewById(R.id.imageButtonList);
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ContactMapActivity.this, ContactListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	private void initSettingsButton() {
		ImageButton list = (ImageButton) findViewById(R.id.imageButtonSettings);
		list.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ContactMapActivity.this, ContactSettingsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
	
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		final String TAG_ERROR_DIALOG_FRAGMENT="errorDialog";
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if (status == ConnectionResult.SUCCESS) {
			//sweet
		}
		else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
			ErrorDialogFragment.newInstance(status).show(getFragmentManager(), TAG_ERROR_DIALOG_FRAGMENT);	
		}
		else {
			Toast.makeText(this, "Google Maps v2 is not avaiolable!", Toast.LENGTH_LONG).show();
			finish();
		}
	
	}
	
	public static class ErrorDialogFragment extends DialogFragment {
	static final String ARG_STATUS="status";
		static ErrorDialogFragment newInstance(int status) {
			Bundle args=new Bundle();
			args.putInt(ARG_STATUS, status);
			ErrorDialogFragment result = new ErrorDialogFragment();
			result.setArguments(args);
			return(result);
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Bundle args = getArguments();
			return GooglePlayServicesUtil.getErrorDialog(args.getInt(ARG_STATUS), getActivity(), 0);
		}
		
		@Override
		public void onDismiss(DialogInterface dlg) {
			if (getActivity() != null) {
				getActivity().finish();
			}
		}
	}
}

