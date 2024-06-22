package eu.mister3551.smokingtracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import eu.mister3551.smokingtracker.R;
import eu.mister3551.smokingtracker.record.History;
import eu.mister3551.smokingtracker.ui.dashboard.HistoryInterface;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<History> historyList;
    private final LayoutInflater layoutInflater;
    private final HistoryInterface listener;

    public HistoryAdapter(Context context, List<History> data, HistoryInterface listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.historyList = data;
        this.listener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_dashboard_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.text_view_full_date_text.setText(String.valueOf(history.date()));

        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(holder.itemView.getContext(), position);
            }
        });

        holder.imageButtonEdit.setOnClickListener(view -> {
            if (listener != null) {
                listener.onEditClick(holder.imageButtonEdit.getContext(), history.id());
            }
        });

        holder.imageButtonDelete.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDeleteClick(holder.imageButtonDelete.getContext(), history.id());
            }
        });

        holder.imageButtonIsLent.setEnabled(!history.isLent());
        holder.imageButtonIsLent.setVisibility(history.isLent() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_view_full_date_text;
        ImageButton imageButtonIsLent;
        ImageButton imageButtonEdit;
        ImageButton imageButtonDelete;

        ViewHolder(View itemView) {
            super(itemView);
            text_view_full_date_text = itemView.findViewById(R.id.text_view_full_date_text);
            imageButtonIsLent = itemView.findViewById(R.id.image_button_is_lent);
            imageButtonEdit = itemView.findViewById(R.id.image_button_edit);
            imageButtonDelete = itemView.findViewById(R.id.image_button_delete);
        }
    }
}