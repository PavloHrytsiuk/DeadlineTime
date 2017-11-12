package com.example.pasha.deadlinecount.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pasha.deadlinecount.R;
import com.example.pasha.deadlinecount.date.DataPref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public final class DeadlinesAdapter extends RecyclerView.Adapter<DeadlinesAdapter.PersonViewHolder> {

    private static final String START_PREF = "Start data";
    private static final String DEADLINE_PREF = "Deadline data";

    private final List<String> deadlineNames;
    private final DeadlineCallbacks callbacks;
    private final DataPref dataPref;

    public DeadlinesAdapter(@NonNull final List<String> deadlineNames,
                            @NonNull final DeadlineCallbacks callbacks,
                            @NonNull final DataPref dataPref) {
        this.deadlineNames = deadlineNames;
        this.callbacks = callbacks;
        this.dataPref = dataPref;
    }


    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deadlines_item,
                parent, false);
        return new PersonViewHolder(view, callbacks);
    }

    @Override
    public void onBindViewHolder(@NonNull final PersonViewHolder holder, final int position) {
        holder.textView.setText(deadlineNames.get(position));
        try {
            Long bufStart = dataPref.loadLongData(START_PREF + deadlineNames.get(position));
            Long bufEnd = dataPref.loadLongData(DEADLINE_PREF + deadlineNames.get(position));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM", Locale.ROOT);
            holder.dateView.setText(formatter.format(new Date(bufStart)) + " - "
                    + formatter.format(new Date(bufEnd)));
        } catch (Exception e) {
            Log.d("TAG", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return deadlineNames.size();
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv)
        CardView cardView;
        @BindView(R.id.item_Text)
        TextView textView;
        @BindView(R.id.item_date)
        TextView dateView;

        PersonViewHolder(@NonNull final View itemView, @NonNull final DeadlineCallbacks callbacks) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull final View view) {
                    callbacks.onClick(getAdapterPosition());
                }
            });
        }
    }
}
