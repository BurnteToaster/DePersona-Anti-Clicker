package com.example.seniorproject2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UpgradesDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "antiClicker.db";
    private static final int DATABASE_VERSION = 1;

    public UpgradesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE upgrades ("
                + "id INTEGER PRIMARY KEY,"
                + "power INTEGER,"
                + "price INTEGER,"
                + "count INTEGER,"
                + "powerFunction TEXT,"
                + "priceFunction TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUpgrade(Upgrade upgrade) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", upgrade.getID());
        values.put("power", upgrade.getPower());
        values.put("price", upgrade.getPrice());
        values.put("count", upgrade.getCount());
        values.put("powerFunction", upgrade.getPowerFunction());
        values.put("priceFunction", upgrade.getPriceFunction());

        db.insert("upgrades", null, values);
        db.close();
    }

    public List<Upgrade> getAllUpgrades() {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {"id", "power", "price", "count", "powerFunction", "priceFunction"};

        Cursor cursor = db.query("upgrades", columns, null, null, null, null, null);

        List<Upgrade> upgrades = new ArrayList<Upgrade>();
        while (cursor.moveToNext()) {
            Upgrade upgrade = new Upgrade();
            upgrade.setID(cursor.getInt(0));
            upgrade.setPower(cursor.getInt(2));
            upgrade.setPrice(cursor.getInt(3));
            upgrade.setCount(cursor.getInt(4));
            upgrade.setPowerFunction(cursor.getString(5));
            upgrade.setPriceFunction(cursor.getString(6));
            upgrades.add(upgrade);
        }
        cursor.close();
        db.close();
        return upgrades;
    }

    public void updateUpgrade(Upgrade upgrade) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ID", upgrade.getID());
        values.put("power", upgrade.getPower());
        values.put("price", upgrade.getPrice());
        values.put("count", upgrade.getCount());
        values.put("powerFunction", upgrade.getPowerFunction());
        values.put("priceFunction", upgrade.getPriceFunction());

        db.update("upgrades", values, "id = ?", new String[] { String.valueOf(upgrade.getID()) });
        db.close();
    }

    public void deleteUpgrade(Upgrade upgrade) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("upgrades", "id = ?", new String[] { String.valueOf(upgrade.getID()) });
        db.close();
    }
}
