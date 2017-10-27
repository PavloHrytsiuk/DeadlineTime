package com.example.pasha.deadlinecount.main;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pasha.deadlinecount.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DeadlinesAdapter extends RecyclerView.Adapter<DeadlinesAdapter.PersonViewHolder> {

    private List<String> deadlineNames;
    private DeadlineCallbacks callbacks;

    public DeadlinesAdapter(List<String> deadlineNames, DeadlineCallbacks callbacks) {
        this.deadlineNames = deadlineNames;
        this.callbacks = callbacks;
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deadlines_item,
                parent, false);
        return new PersonViewHolder(view, callbacks);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.textView.setText(deadlineNames.get(position));
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

        PersonViewHolder(View itemView, final DeadlineCallbacks callbacks) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.onClick(getAdapterPosition());
                }
            });
        }
    }
}
