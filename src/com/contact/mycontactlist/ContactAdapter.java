package com.contact.mycontactlist;

import java.util.ArrayList;


import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {
	
	private ArrayList<Contact> items;
	private Context adapterContext;
	public boolean ColorChange = false;
	
	public ContactAdapter(Context context, ArrayList<Contact> items) {
		super(context, R.layout.list_item, items);
		adapterContext = context;
		this.items = items;
		 
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	View v = convertView;
	try {
		Contact contact = items.get(position);
		
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item, null);

		}
		
		
		TextView contactNumber = (TextView) v.findViewById(R.id.textPhoneNumber);
		TextView contactCell = (TextView) v.findViewById(R.id.textCellNumber);
		TextView contactAddressInfo = (TextView) v.findViewById(R.id.textAddressInfo);
		TextView contactCityInfo = (TextView) v.findViewById(R.id.textCityInfo);
		TextView contactStateInfo = (TextView) v.findViewById(R.id.textStateInfo);
		TextView contactZipCodeInfo = (TextView) v.findViewById(R.id.textZipCodeInfo);

		Button b = (Button) v.findViewById(R.id.buttonDeleteContact);


		contactNumber.setText("Home: " + contact.getPhoneNumber());
		contactCell.setText("Cell " + contact.getCellNumber());
		contactAddressInfo.setText("Address: " + contact.getStreetAddress());
		contactCityInfo.setText("City: " + contact.getCity());
		contactStateInfo.setText("State: " + contact.getState());
		contactZipCodeInfo.setText("Zip: " + contact.getZipCode());
		b.setVisibility(View.INVISIBLE);
		
			if (ColorChange == false) {
			
				ColorChange = true;
			
				TextView contactName = (TextView) v.findViewById(R.id.textContactName);
				int red = contactName.getResources().getColor(R.color.system_red);	
				contactName.setTextColor(red);
				contactName.setText(contact.getContactName());
				
			}
			else {
				ColorChange = false;
				TextView contactName = (TextView) v.findViewById(R.id.textContactName);
				int white = contactName.getResources().getColor(R.color.system_white);
				contactName.setTextColor(white);
				contactName.setText(contact.getContactName());

			}
			}
	
	catch (Exception e) {
		e.printStackTrace(); 
		e.getCause();
	}
	return v;
		
		}
	
	public void showDelete(final int position, final View convertView, final Context context, final Contact contact) {
		View v = convertView;
		final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);
		
		if (b.getVisibility()==View.INVISIBLE) {
			b.setVisibility(View.VISIBLE);
			b.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					hideDelete(position, convertView, context);
					items.remove(contact);
					deleteOption(contact.getContactID(), context);
				}
			});
		}
		else {
			hideDelete(position, convertView, context);
		}
	}
	
	
	private void deleteOption(int contactToDelete, Context context) {
		ContactDataSource db = new ContactDataSource(context);
		db.open();
		db.deleteContact(contactToDelete);
		db.close();
		this.notifyDataSetChanged();
	}
	
	public void hideDelete(int position, View convertView, Context context) {
		View v = convertView;
		final Button b = (Button) v.findViewById(R.id.buttonDeleteContact);
		b.setVisibility(View.INVISIBLE);
		b.setOnClickListener(null);
	}
	}



