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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Listener listener;
    private final List<HttpsXmlParser.Item> items;

    public NewsAdapter(List<HttpsXmlParser.Item> items) {
        this.items = items;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_card, parent, false);
        return new NewsAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        HttpsXmlParser.Item item = items.get(position);
        CardView cardView = holder.cardView;

        TextView titleView = cardView.findViewById(R.id.new_name);
        TextView descriptionView = cardView.findViewById(R.id.new_description);
        TextView pubDateView = cardView.findViewById(R.id.new_date);
        ImageView imageView = cardView.findViewById(R.id.new_image);

        titleView.setText(item.getTitle());
        descriptionView.setText(item.getDescription());
        pubDateView.setText(item.getPubDate());
        Picasso.get().load(item.getImageLink()).into(imageView);

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(item.getLink());
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
        void onClick(String link);
    }
}
