package com.shashank.managemembers.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.shashank.managemembers.models.history;
import com.shashank.managemembers.models.members;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Members.DB", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE MEMBERS( ID INTEGER PRIMARY KEY AUTOINCREMENT, MEMBERCODE TEXT UNIQUE,FIRSTNAME TEXT, LASTNAME TEXT, MOBILE INTEGER, " +
                "JOINDATE TEXT, BIRTHDATE TEXT, DRINK1 TEXT, DRINK2 TEXT, DRINK3 TEXT, DRINK4 TEXT, DRINK5 TEXT, DRINK6 TEXT, DRINK7 TEXT, DRINK8 TEXT,DRINK9 TEXT, DRINK10 TEXT);");
        db.execSQL("CREATE TABLE MENU(TYPE TEXT, ITEM TEXT UNIQUE)");
        db.execSQL("CREATE TABLE HISTORY(MEMBERCODE TEXT, NAME TEXT, MOBILE TEXT, BOUGHT TEXT,TIME TEXT,DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MEMBERS;");
        onCreate(db);
    }

    public void insertMenu(String type, String item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("TYPE", type);
        contentValues.put("ITEM", item);
        this.getWritableDatabase().insertOrThrow("MENU", "", contentValues);
    }

    public void listMenu(ArrayList<String> arrayList) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM MENU", null);

        while (cursor.moveToNext()) {
            arrayList.add("type: " + cursor.getString(0) + "    drink: " + cursor.getString(1));
        }
        cursor.close();
    }

    public void fetchMenu(String type, ArrayList<String> menu) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT ITEM FROM MENU WHERE TYPE = '" + type + "'", null);
//        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            menu.add(cursor.getString(0));
        }
        cursor.close();
    }

    public void removeItem(String itemName) {
        this.getWritableDatabase().execSQL("DELETE FROM MENU WHERE ITEM ='" + itemName + "'");
    }

    public void insertMember(String memberCode, String firstName, String lastName, Long mobile, String joinDate, String birthdate, ArrayList<String> drinks) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("MEMBERCODE", memberCode);
        contentValues.put("FIRSTNAME", firstName);
        contentValues.put("LASTNAME", lastName);
        contentValues.put("MOBILE", mobile);
        contentValues.put("JOINDATE", joinDate);
        contentValues.put("BIRTHDATE", birthdate);
        try {
            contentValues.put("DRINK1", drinks.get(0));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK1", "");
        }
        try {
            contentValues.put("DRINK2", drinks.get(1));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK2", "");
        }
        try {
            contentValues.put("DRINK3", drinks.get(2));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK3", "");
        }
        try {
            contentValues.put("DRINK4", drinks.get(3));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK4", "");
        }
        try {
            contentValues.put("DRINK5", drinks.get(4));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK5", "");
        }
        try {
            contentValues.put("DRINK6", drinks.get(5));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK6", "");
        }
        try {
            contentValues.put("DRINK7", drinks.get(6));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK7", "");
        }
        try {
            contentValues.put("DRINK8", drinks.get(7));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK8", "");
        }
        try {
            contentValues.put("DRINK9", drinks.get(8));
        } catch (IndexOutOfBoundsException e) {
            contentValues.put("DRINK9", "");
        }
        try {
            contentValues.put("DRINK10", drinks.get(9));
        } catch (IndexOutOfBoundsException e){
            contentValues.put("DRINK10", "");
        }

        this.getWritableDatabase().insertOrThrow("MEMBERS", "", contentValues);
    }

    public void updateAvailable(String memberCode, ArrayList<String> boughtDrinks) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM MEMBERS WHERE MEMBERCODE = '" + memberCode + "'", null);
        int count = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            for (int j = 0; j < boughtDrinks.size(); j++) {
                for (int i = cursor.getColumnIndex("DRINK1"); i < count; i++) {
                    if (boughtDrinks.contains(cursor.getString(i))) {
                        this.getReadableDatabase().execSQL("UPDATE MEMBERS SET " + cursor.getColumnName(i) + "= '" + boughtDrinks.get(j) + " Bought' WHERE MEMBERCODE = '" + memberCode + "'");
                    }
                }
            }
        }
        cursor.close();
    }

    public void updateMember(String memberCode, String name, String lastName, String mobile, String dob) {
        this.getReadableDatabase().execSQL("UPDATE MEMBERS SET FIRSTNAME ='" + name + "' , LASTNAME ='" + lastName + "' , MOBILE ='" + mobile + "' , BIRTHDATE ='" + dob + "' WHERE MEMBERCODE ='" + memberCode + "'");
    }

    @SuppressLint("SetTextI18n")
    public void availableDrinks(String memberCode, ArrayList<String> drinks, TextView name, TextView mobile, TextView dob) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM MEMBERS WHERE MEMBERCODE = '" + memberCode + "'", null);
        int count = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            for (int i = cursor.getColumnIndex("DRINK1"); i < count; i++) {
                if (!cursor.getString(i).contains("Bought") && !cursor.getString(i).equals("")) {
                    drinks.add(cursor.getString(i));
                }
            }
            name.append(cursor.getString(2) + " " + cursor.getString(3));
            mobile.append(cursor.getString(4));
            dob.append(cursor.getString(6));
        }
        cursor.close();
    }

    public int drinkNo(String memberCode) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM MEMBERS WHERE MEMBERCODE ='" + memberCode + "'", null);
        int drinkNo = 0;
        int count = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            for (int i = cursor.getColumnIndex("DRINK1"); i < count; i++) {
                if (!cursor.getString(i).contains("Bought") && !cursor.getString(i).equals("")) {
                    drinkNo++;
                }
            }
        }
        cursor.close();
        return drinkNo;
    }

    public boolean fetchMember(String memberCode) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT MEMBERCODE FROM MEMBERS WHERE MEMBERCODE = '" + memberCode + "'", null);
        if (cursor.moveToFirst()) {
            boolean cursorBoolean = cursor.getString(0).equals(memberCode);
            cursor.close();
            return cursorBoolean;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean fetchMember(String memberCode, EditText name, EditText lastName, EditText mobile, EditText dob) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM MEMBERS WHERE MEMBERCODE = '" + memberCode + "'", null);
        if (cursor.moveToFirst()) {
            boolean cursorBoolean = cursor.getString(1).equals(memberCode);
            if (cursor.getString(1).equals(memberCode)) {
                name.setText(cursor.getString(2));
                lastName.setText(cursor.getString(3));
                mobile.setText(cursor.getString(4));
                dob.setText(cursor.getString(6));
                cursor.close();
            }
            return cursorBoolean;
        } else {
            cursor.close();
            return false;
        }
    }

    public void renewMembership(String memberCode, ArrayList<String> drinks) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM MEMBERS WHERE MEMBERCODE = '" + memberCode + "'", null);
        int count = cursor.getColumnCount();
        while (cursor.moveToNext()) {
            int j = 0;
            for (int i = cursor.getColumnIndex("DRINK1"); i < count; i++) {
                try {
                    this.getReadableDatabase().execSQL("UPDATE MEMBERS SET " + cursor.getColumnName(i) + "= '" + drinks.get(j) + "' WHERE MEMBERCODE = '" + memberCode + "'");
                    j++;
                } catch (IndexOutOfBoundsException e){
                    this.getReadableDatabase().execSQL("UPDATE MEMBERS SET " + cursor.getColumnName(i) + "= '' WHERE MEMBERCODE = '" + memberCode + "'");
                }

            }
        }
        cursor.close();
    }


    public ArrayList<members> listAll() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM MEMBERS", null);
        ArrayList<members> mMembers = new ArrayList<>();
        int count = cursor.getColumnCount();
        StringBuilder Customer = new StringBuilder();
        StringBuilder Drinks = new StringBuilder();
        String memberCode = "";
        String Mobile = "";
        String joinDate = "";
        String dob = "";
        while (cursor.moveToNext()) {
            memberCode = cursor.getString(1);
            Customer.append(" ").append(cursor.getString(2)).append(" ").append(cursor.getString(3));
            Mobile = cursor.getString(4);
            joinDate = cursor.getString(5);
            dob = cursor.getString(6);
            for (int i = cursor.getColumnIndex("DRINK1"); i < count; i++) {
                    if (!cursor.getString(i).equals("")) {
                        Drinks.append(cursor.getString(i)).append("\n");
                    }
            }
            members members = new members(memberCode, Customer.toString(), Mobile, joinDate, dob, Drinks.toString());
            mMembers.add(0, members);
            Customer.setLength(0);
            Drinks.setLength(0);
        }
        cursor.close();
        return mMembers;
    }

    public ArrayList<String> getPhoneNumbers() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT MOBILE FROM MEMBERS", null);
        ArrayList<String> sms = new ArrayList<>();
        while (cursor.moveToNext()){
            if (!sms.contains(cursor.getString(0))) {
                sms.add(cursor.getString(0));
            }
        }
        cursor.close();
        return sms;
    }

    public void insertHistory(String memberCode, String name, String mobile, String order, String time, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MEMBERCODE", memberCode);
        contentValues.put("NAME",name);
        contentValues.put("MOBILE", mobile);
        contentValues.put("BOUGHT", order);
        contentValues.put("TIME", time);
        contentValues.put("DATE", date);
        this.getWritableDatabase().insertOrThrow("HISTORY", "", contentValues);
    }

    public ArrayList<history> getHistory(String memberCode){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM HISTORY WHERE MEMBERCODE ='"+memberCode+"'", null);
        ArrayList<history> historyArrayList = new ArrayList<>();
        while (cursor.moveToNext()){
            history historyObj = new history(cursor.getString(1),cursor.getString(2),cursor.getString(3).replace(",", "\n"),
                    cursor.getString(4),cursor.getString(5));
            historyArrayList.add(0,historyObj);
        }
        cursor.close();
        return historyArrayList;
    }
}
