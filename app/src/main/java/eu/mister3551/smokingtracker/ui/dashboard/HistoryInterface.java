package eu.mister3551.smokingtracker.ui.dashboard;

import android.content.Context;

public interface HistoryInterface {
    void onItemClick(Context context, int position);
    void onEditClick(Context context, Long id);
    void onDeleteClick(Context context, Long id);
}