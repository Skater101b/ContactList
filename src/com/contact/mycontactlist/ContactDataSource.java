package com.contact.mycontactlist;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

public class ContactDataSource {
	
	private SQLiteDatabase database;
	private ContactDBHelper dbHelper;
	public ContactDataSource(Context context) {
		dbHelper = new ContactDBHelper(context);
	}
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	public void close() {
		dbHelper.close();
	}
	
	
	public boolean deleteContact(int contactId) {
		boolean didDelete = false;
		try {
			didDelete = database.delete("contact", "_id=" + contactId, null) > 0;
		}
		catch (Exception e) {
			//do nothing return value already set to false
		}
		return didDelete;
	}

	public boolean insertContact(Contact c) {
		boolean didSucceed = false;
		try {
			ContentValues initialValues = new ContentValues();
			initialValues.put("contactname", c.getContactName());
			initialValues.put("streetaddress", c.getStreetAddress());
			initialValues.put("city", c.getCity());
			initialValues.put("state", c.getState());
			initialValues.put("zipcode", c.getZipCode());
			initialValues.put("phonenumber", c.getPhoneNumber());
			initialValues.put("cellnumber", c.getCellNumber());
			initialValues.put("email", c.getEmail());
			initialValues.put("birthday", String.valueOf(c.getBirthday().toMillis(false)));	
			
			didSucceed = database.insert("contact", null, initialValues) > 0;
		}
		catch (Exception e) {
			//Do nothing will return false if there is an exception
		}
			return didSucceed;
		}
		
		public boolean updateContact(Contact c) {
			boolean didSucceed = false;
			try {
				Long rowId = Long.valueOf(c.getContactID());
				ContentValues updateValues = new ContentValues();
				updateValues.put("contactname", c.getContactName());
				updateValues.put("streetaddress", c.getStreetAddress());
				updateValues.put("city", c.getCity());
				updateValues.put("state", c.getState());
				updateValues.put("zipcode", c.getZipCode());
				updateValues.put("phonenumber", c.getPhoneNumber());
				updateValues.put("cellnumber", c.getCellNumber());
				updateValues.put("email", c.getEmail());
				updateValues.put("birthday", String.valueOf(c.getBirthday().toMillis(false)));
				
				didSucceed = database.update("contact", updateValues, "_id=" + rowId, null) > 0;
			}
			catch  (Exception e) {
					//do nothing 
				}
				return didSucceed;
				
}
		public Contact getSpecificContact(int contactId) {
			Contact contact = new Contact();
			String query = "SELECT * FROM contact WHERE _id =" + contactId;
				Cursor cursor = database.rawQuery(query, null);
			
			if (cursor.moveToFirst()) {
				contact.setContactID(cursor.getInt(0));
				contact.setContactName(cursor.getString(1));
				contact.setStreetAddress(cursor.getString(2));
				contact.setCity(cursor.getString(3));
				contact.setState(cursor.getString(4));
				contact.setZipCode(cursor.getString(5));
				contact.setPhoneNumber(cursor.getString(6));
				contact.setCellNumber(cursor.getString(7));
				contact.setEmail(cursor.getString(8));
				Time t = new Time();
				t.set(Long.valueOf(cursor.getString(9)));
				contact.setBirthday(t);
				
				cursor.close();
				
			}
			
			return contact;
		}
		
		public boolean updateContactAddress(Contact c) {
			boolean didSucceed = false;
			try {
			Long rowId = Long.valueOf(c.getContactID());
			ContentValues updateValues = new ContentValues();
			updateValues.put("streetaddress", c.getStreetAddress());
			updateValues.put("city", c.getCity());
			updateValues.put("state", c.getState());
			updateValues.put("zipcode", c.getZipCode());
			
			didSucceed = database.update("contact", updateValues, "_id=" + rowId, null) > 0;
		}
		catch  (Exception e) {
				//do nothing 
			}
			return didSucceed;
			
		}
		//remove sortField and sortOrder String sortField, String sortOrder
		public ArrayList<Contact> getContacts(String contactName, String ASC) {
			ArrayList<Contact> contacts = new ArrayList<Contact>();
			try {
				//  ORDER BY" + sortField + "" + sortOrder
				String query = "SELECT * FROM contact";
				Cursor cursor = database.rawQuery(query, null);
				
				Contact newContact;
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					newContact = new Contact();
							
					newContact.setContactID(cursor.getInt(0));
					newContact.setContactName(cursor.getString(1));
					newContact.setStreetAddress(cursor.getString(2));
					newContact.setCity(cursor.getString(3));
					newContact.setState(cursor.getString(4));
					newContact.setZipCode(cursor.getString(5));
					newContact.setPhoneNumber(cursor.getString(6));
					newContact.setCellNumber(cursor.getString(7));
					newContact.setEmail(cursor.getString(8));
					Time t = new Time();
					
					t.set(Long.valueOf(cursor.getString(9)));
					newContact.setBirthday(t);
					
					contacts.add(newContact);
					cursor.moveToNext();
					}
				
				cursor.close();
			}
			catch (Exception e) {
				contacts = new ArrayList<Contact>();
			}
			return contacts;						
			
		}
		
		
		
		public int getLastContactId() {
			int lastId = -1;
			try { String query = "Select MAX(_id) from contact";
			Cursor cursor = database.rawQuery(query, null);
			cursor.moveToFirst();
			lastId = cursor.getInt(0);
			cursor.close(); 
			}
			catch (Exception e) {
				lastId = -1; }
			return lastId;
		}
			
}
