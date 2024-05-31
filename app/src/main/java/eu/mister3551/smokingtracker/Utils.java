package eu.mister3551.smokingtracker;

import java.lang.ref.WeakReference;

import eu.mister3551.smokingtracker.database.Manager;
import eu.mister3551.smokingtracker.record.Settings;

public class Utils {

    public static WeakReference<Manager> manager;
    public static Settings settings;

    public static WeakReference<Manager> getManager() {
        return manager;
    }

    public static void setManager(WeakReference<Manager> manager) {
        Utils.manager = manager;
    }

    public static Settings getSettings() {
        return settings;
    }

    public static void setSettings(Settings settings) {
        Utils.settings = settings;
    }
}