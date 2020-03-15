package com.lyloou.flow.ui.list;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyloou.flow.R;
import com.lyloou.flow.util.Uscreen;
import com.lyloou.flow.widget.ItemOffsetDecoration;

public class Part {

    private ListAdapter mAdapter;
    private View mView;
    private String mTitle;

    @SuppressLint("InflateParams")
    Part(Context context, String title) {
        mTitle = title;
        mView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
        RecyclerView recyclerView = mView.findViewById(R.id.rv_list);
        mAdapter = new ListAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(context, 16f)));
    }

    public String getTitle() {
        return mTitle;
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    public View getView() {
        return mView;
    }
}
