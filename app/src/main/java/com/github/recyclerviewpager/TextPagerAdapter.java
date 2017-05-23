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

public class TextPagerAdapter extends RecyclerView.Adapter<TextPagerAdapter.TextPagerVH> {

    private List<String> list;

    public TextPagerAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public TextPagerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_pager, parent, false);
        return new TextPagerVH(view);
    }

    @Override
    public void onBindViewHolder(TextPagerVH holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TextPagerVH extends RecyclerView.ViewHolder{
        private TextView textView;
        public TextPagerVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.page_text);
        }
    }
}
