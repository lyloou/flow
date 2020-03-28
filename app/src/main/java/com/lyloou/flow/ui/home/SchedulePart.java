package com.lyloou.flow.ui.home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lyloou.flow.R;
import com.lyloou.flow.util.Uscreen;
import com.lyloou.flow.widget.ItemOffsetDecoration;

public class SchedulePart {

    private ScheduleSyncAdapter mAdapter;
    private View mView;
    private String mTitle;

    @SuppressLint("InflateParams")
    SchedulePart(Context context, String title) {
        mTitle = title;
        mView = LayoutInflater.from(context).inflate(R.layout.item_schedule_list, null);
        RecyclerView recyclerView = mView.findViewById(R.id.rv_list);
        mAdapter = new ScheduleSyncAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new ItemOffsetDecoration(Uscreen.dp2Px(context, 16f)));
    }

    public String getTitle() {
        return mTitle;
    }

    public ScheduleSyncAdapter getAdapter() {
        return mAdapter;
    }

    public View getView() {
        return mView;
    }
}
