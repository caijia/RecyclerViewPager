package com.github.recyclerviewpager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cai.jia on 2017/5/23 0023
 */

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextVH> {

    private List<String> list;

    public TextAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public TextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text, parent, false);
        return new TextVH(view);
    }

    @Override
    public void onBindViewHolder(TextVH holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TextVH extends RecyclerView.ViewHolder{

        private TextView textView;

        public TextVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
