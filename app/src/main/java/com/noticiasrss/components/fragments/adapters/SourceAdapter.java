package com.noticiasrss.components.fragments.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.noticiasrss.R;
import com.noticiasrss.data.parsers.HttpsXmlParser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {

    private Listener listener;
    private List<HttpsXmlParser.Channel> channels;

    public SourceAdapter(List<HttpsXmlParser.Channel> channels) {
        this.channels = channels;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.source_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(SourceAdapter.ViewHolder holder, int position) {
        HttpsXmlParser.Channel channel = channels.get(position);
        CardView cardView = holder.cardView;

        TextView titleView = cardView.findViewById(R.id.source_name);
        TextView descriptionView = cardView.findViewById(R.id.source_description);
        ImageView imageView = cardView.findViewById(R.id.source_image);

        titleView.setText(channel.getTitle());
        descriptionView.setText(channel.getDescription());
        Picasso.get().load(channel.getImageLink()).into(imageView);

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(channel.getDbSourceId(), channel.getLink());
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public interface Listener {
        void onClick(long sourceId, String link);
    }
}
