package edu.temple.chatapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<String> partnerNames;
    private Context context;
    private static RecyclerViewItemClicked itemClickedListener;

    public ListAdapter(ArrayList<String> partnerNames, RecyclerViewItemClicked itemClickedListener, Context context) {
        this.partnerNames = partnerNames;
        this.itemClickedListener = itemClickedListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.layout_recycleritem, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.partnerTextView.setText(partnerNames.get(i));
    }

    @Override
    public int getItemCount() {
        return partnerNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView partnerTextView;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            partnerTextView = itemView.findViewById(R.id.item_textview);
            parentLayout = itemView.findViewById(R.id.item_layout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickedListener.userItemClick(v, this.getAdapterPosition());
        }
    }
}
