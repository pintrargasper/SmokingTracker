package eu.mister3551.smokingtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smoking_tracker_1";
    private static final int DATABASE_VERSION = 1;
    public static final String MAIN_TABLE = "main";
    public static final String HISTORY_TABLE = "history";
    public static final String SETTINGS_TABLE = "settings";
    public static final String GRAPH_TABLE = "graph";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Queries.createMain);
        sqLiteDatabase.execSQL(Queries.createHistory);
        sqLiteDatabase.execSQL(Queries.createSettings);
        sqLiteDatabase.execSQL(Queries.createGraph);
        sqLiteDatabase.execSQL(Queries.updateSettings("EN"));
        sqLiteDatabase.execSQL(Queries.insertGraph());
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(Queries.dropMain);
        database.execSQL(Queries.dropHistory);
        database.execSQL(Queries.dropSettings);
        database.execSQL(Queries.dropGraph);
    }
}