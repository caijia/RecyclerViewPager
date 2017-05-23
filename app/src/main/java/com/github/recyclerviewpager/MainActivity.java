package com.github.recyclerviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.library.LooperPageRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private LooperPageRecyclerView looperPageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TextAdapter(getData()));

        looperPageRecyclerView = (LooperPageRecyclerView) findViewById(R.id.pager_recycler_view);
        looperPageRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        looperPageRecyclerView.setAdapter(new TextPagerAdapter(getData()));
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("Item position = " + (i + 1));
        }
        return list;
    }

    public void scrollNext(View view) {
        int position = layoutManager.findLastVisibleItemPosition();
        recyclerView.smoothScrollToPosition(++position);
    }
}
