package com.dd.adapterhelper;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.library.adapter.base.BaseQuickAdapter;
import com.dd.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AdapterActivity extends AppCompatActivity implements BaseQuickAdapter.RequestRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private int pageSize = 5;
    private int pageNow = 1;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView head = new TextView(this);
        head.setText("我是头部");
        head.setGravity(Gravity.CENTER);
        head.setPadding(0, 100, 0, 100);

        TextView foot = new TextView(this);
        foot.setText("我是底部");
        foot.setGravity(Gravity.CENTER);
        foot.setPadding(0, 100, 0, 100);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerAdapter(new ArrayList<String>());
        adapter.addHeaderView(head);
        adapter.addFooterView(foot);
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setOnRefreshListener(this, recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getContext(), "点击了第 " + position + " 个 item", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getContext(), "点击了第 " + position + " 个 item 的 child", Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getContext(), "长按了第 " + position + " 个 item", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        adapter.setEmptyView(R.layout.recycler_empty);
        recyclerView.setAdapter(adapter);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.refresh();
            }
        }, 1000);
    }

    @Override
    public void onRefreshRequested() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNow = 1;
                List<String> list = new ArrayList<>();
                list.add("1");
                list.add("2");
                list.add("3");
                list.add("4");
                list.add("5");
                list.add("6");
                list.add("7");
                list.add("8");
                list.add("9");
                list.add("10");
                list.add("11");
                list.add("12");
                list.add("13");
                adapter.setNewData(list);
                adapter.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMoreRequested() {
        if (pageNow >= pageSize) {
            adapter.loadMoreEnd();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pageNow == 2) {
                        adapter.loadMoreFail();
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add("1");
                        list.add("2");
                        list.add("3");
                        list.add("4");
                        list.add("5");
                        adapter.addData(list);
                        adapter.loadMoreComplete();
                    }
                    pageNow++;
                }
            }, 1500);
        }
    }

    private class RecyclerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public RecyclerAdapter(@Nullable List<String> data) {
            super(R.layout.recycler_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.txt, item);
            helper.addOnClickListener(R.id.txt);
        }
    }

    private Context getContext() {
        return this;
    }
}
