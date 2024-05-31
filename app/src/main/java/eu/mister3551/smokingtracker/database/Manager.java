package eu.mister3551.smokingtracker.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;

import java.sql.SQLDataException;

public class Manager {

    private Database database;
    private final Context context;
    private SQLiteDatabase sqLiteDatabase;

    public Manager(Context context) {
        this.context = context;
    }

    public void open() throws SQLDataException {
        database = new Database(context);
        sqLiteDatabase = database.getReadableDatabase();
    }

    public void close() {
        database.close();
    }

    public void insertRecord(String currentDate) {
        sqLiteDatabase.execSQL(Queries.updateTimer);
        sqLiteDatabase.execSQL(Queries.insertRecord(currentDate));
    }
    
    public void delete(Long id) {
        sqLiteDatabase.execSQL(Queries.deleteRecord(id));
    }

    public void update(Long id, String customDate) {
        sqLiteDatabase.execSQL(Queries.updateRecord(id, customDate));
    }

    public Cursor fetchCurrentDate(String currentDate) {
        return sqLiteDatabase.rawQuery(Queries.fetchCurrentDate(currentDate), null);
    }

    public Cursor fetchAll(String currentDate) {
        return sqLiteDatabase.rawQuery(Queries.fetchAll(currentDate), null);
    }

    public Cursor fetchHistory(String currentDate) {
        return sqLiteDatabase.rawQuery(Queries.fetchHistory(currentDate), null);
    }

    public Cursor fetchById(Long id) {
        return sqLiteDatabase.rawQuery(Queries.fetchById(id), null);
    }

    public Cursor fetchByWeek(String minDate, String maxDate) {
        return sqLiteDatabase.rawQuery(Queries.fetchByWeekAndMonth(minDate, maxDate), null);
    }

    public Cursor fetchByMonth(String minDate, String maxDate) {
        return sqLiteDatabase.rawQuery(Queries.fetchByWeekAndMonth(minDate, maxDate), null);
    }

    public Cursor fetchByYear(String minDate, String maxDate) {
        return sqLiteDatabase.rawQuery(Queries.fetchByYear(minDate, maxDate), null);
    }

    public Cursor fetchSettings() {
        return sqLiteDatabase.rawQuery(Queries.fetchSettings, null);
    }

    public void update(String language) {
        sqLiteDatabase.execSQL(Queries.updateSettings(language));
    }

    public void update(int color, String pointColors, String lineColors) {
        sqLiteDatabase.execSQL(Queries.updateGraph(color, pointColors, lineColors));
    }

    public void update(Paint.Style paintStyle) {
        sqLiteDatabase.execSQL(Queries.updatePaintStyle(paintStyle));
    }
}