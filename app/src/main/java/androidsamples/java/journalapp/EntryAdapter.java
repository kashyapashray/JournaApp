package androidsamples.java.journalapp;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.MyViewHolder> {
    private ArrayList<Journal> entries;
    private RecyclerUpdate listener;

    public EntryAdapter(ArrayList<Journal> entries) {
        this.entries = entries;
    }
    public void setListener(RecyclerUpdate listener){
        this.listener = listener;
    }

    // This method creates a new ViewHolder object for each item in the RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item and return a new ViewHolder object
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_entry, parent, false);
        return new MyViewHolder(itemView);
    }

    // This method returns the total
    // number of items in the data set
    @Override
    public int getItemCount() {
        return entries.size();
    }

    // This method binds the data to the ViewHolder object
    // for each item in the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Journal entry = entries.get(position);
        holder.title.setText(entry.title);
        holder.date.setText(entry.date);
        holder.start.setText(entry.start);
        holder.end.setText(entry.end);
        holder.setJournal(entry);
        holder.bind(position, listener);
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    void insertData(List<Journal> entries){
        this.entries = (ArrayList<Journal>) entries;
        notifyDataSetChanged();
    }

    void deleteData(JournalWIndex jwi){
        this.entries.remove(jwi.index);
        notifyItemRemoved(jwi.index);
    }

    void updateData(JournalWIndex jwi){
        if(jwi.index == 0){
            this.entries.add(jwi.index,Journal.fromJournalData(jwi.j));
        }else{
            this.entries.set(jwi.index, Journal.fromJournalData(jwi.j));
            notifyItemChanged(jwi.index);
        }
    }

    // This class defines the ViewHolder object for each item in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView date;
        private final TextView start;
        private final TextView end;
        private Journal journal;

        void setJournal(Journal j){
            this.journal = j;
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_item_title);
            date = itemView.findViewById(R.id.txt_item_date);
            start = itemView.findViewById(R.id.txt_item_start_time);
            end = itemView.findViewById(R.id.txt_item_end_time);
        }
        void bind(int position,  RecyclerUpdate listener){
            itemView.setOnClickListener(v -> listener.onItemClick(position, journal));
        }
    }
}
