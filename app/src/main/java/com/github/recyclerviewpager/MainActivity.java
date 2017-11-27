package com.github.recyclerviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.caijia.widget.looperrecyclerview.LooperPageRecyclerView;
import com.caijia.widget.looperrecyclerview.RecyclerViewCircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LooperPageRecyclerView looperPageRecyclerView;
    private RecyclerViewCircleIndicator indicator;
    private TextPagerAdapter adapter;
    private List<String> colorStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicator = findViewById(R.id.indicator);
        looperPageRecyclerView = findViewById(R.id.pager_recycler_view);
        looperPageRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        colorStrings = getData();
        adapter = new TextPagerAdapter(colorStrings);
        looperPageRecyclerView.setAdapter(adapter);
        indicator.setupWithRecyclerView(looperPageRecyclerView);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("#339933");
        list.add("#ff00ff");
        list.add("#ff5566");
        list.add("#ff0000");
        return list;
    }

    public void changeAdapter(View view) {
        if (colorStrings.size() < 5) {
            colorStrings.add("#333333");
            adapter.notifyDataSetChanged();
        }
    }
}
