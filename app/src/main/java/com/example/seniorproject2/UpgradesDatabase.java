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
    private static final String TABLE_NAME = "upgrades";
    private static final String COL_ID = "id";
    private static final String COL_POWER = "power";
    private static final String COL_PRICE = "price";
    private static final String COL_COUNT = "count";
    private static final String COL_POWER_COEFFICIENT = "powerCoefficient";
    private static final String COL_PRICE_COEFFICIENT = "priceCoefficient";

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
                + "powerCoefficient FLOAT,"
                + "priceCoefficient FLOAT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUpgrade(Upgrade upgrade) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ID, upgrade.getID());
        values.put(COL_POWER, upgrade.getPower());
        values.put(COL_PRICE, upgrade.getPrice());
        values.put(COL_COUNT, upgrade.getCount());
        values.put(COL_POWER_COEFFICIENT, upgrade.getPowerCoefficient());
        values.put(COL_PRICE_COEFFICIENT, upgrade.getPriceCoefficient());

        db.insert("upgrades", null, values);
        db.close();
    }

    public List<Upgrade> getAllUpgrades() {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {"id", "power", "price", "count", "powerCoefficient", "priceCoefficient"};

        Cursor cursor = db.query("upgrades", columns, null, null, null, null, null);

        List<Upgrade> upgrades = new ArrayList<>();
        while (cursor.moveToNext()) {
            Upgrade upgrade = new Upgrade();
            upgrade.setID(cursor.getInt(0));
            upgrade.setPower(cursor.getInt(1));
            upgrade.setPrice(cursor.getInt(2));
            upgrade.setCount(cursor.getInt(3));
            upgrade.setPowerCoefficient(cursor.getFloat(4));
            upgrade.setPriceCoefficient(cursor.getFloat(5));
            upgrades.add(upgrade);
        }
        cursor.close();
        db.close();
        return upgrades;
    }

    public List<Upgrade> getVisualUpgrades() {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {"id", "power", "price", "count"};

        Cursor cursor = db.query("upgrades", columns, null, null, null, null, null);

        List<Upgrade> upgrades = new ArrayList<>();
        while (cursor.moveToNext()) {
            Upgrade upgrade = new Upgrade();
            upgrade.setID(cursor.getInt(0));
            upgrade.setPower(cursor.getInt(1));
            upgrade.setPrice(cursor.getInt(2));
            upgrade.setCount(cursor.getInt(3));
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
        values.put("powerCoefficient", upgrade.getPowerCoefficient());
        values.put("priceCoefficient", upgrade.getPriceCoefficient());

        db.update("upgrades", values, "id = ?", new String[] { String.valueOf(upgrade.getID()) });
        db.close();
    }

    public void deleteUpgrade(Upgrade upgrade) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("upgrades", "id = ?", new String[] { String.valueOf(upgrade.getID()) });
        db.close();
    }
}
