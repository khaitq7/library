package com.nabugs.smscatcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nabugs.smscatcher.R;
import com.nabugs.smscatcher.model.SmsMessage;

import java.util.List;

/**
 * Created by namanh on 6/4/2018.
 */

public class SmsMessageAdapter extends RecyclerView.Adapter<SmsMessageAdapter.WordViewHolder> {
    private final LayoutInflater mInflater;
    private List<SmsMessage> listMes;

    public SmsMessageAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (listMes != null) {
            SmsMessage current = listMes.get(position);
            holder.tvMessage.setText(current.getMessage());
            holder.tvSender.setText(current.getSender());
        } else {
            // Covers the case of data not being ready yet.
            holder.tvMessage.setText("No Message");
            holder.tvSender.setText("No Sender");
        }
    }

    public void setMess(List<SmsMessage> listMess){
        listMes = listMess;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listMes != null)
            return listMes.size();
        else return 0;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;
        private final TextView tvSender;

        private WordViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.textView);
            tvSender = itemView.findViewById(R.id.tvSender);
        }
    }
}
