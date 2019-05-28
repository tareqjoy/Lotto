package com.qerat.lotto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ResultClass> itemList = new ArrayList<>();
    private ResultAdapter mAdapter;
    private String gameName;
    private TextView noResult;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        gameName = intent.getStringExtra("gameName");

        setTitle(gameName);

        FloatingActionButton fab = findViewById(R.id.fab);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.resultContainerRecyclerView);
        noResult = findViewById(R.id.noResultsTextView);


        mAdapter = new ResultAdapter(itemList, gameName, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ResultActivity.this, ResultAddActivity.class);
                //Optional parameters
                myIntent.putExtra("gameName", gameName);
                startActivity(myIntent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDataFromFirebase();
            }
        });

        readDataFromFirebase();
    }

    private void readDataFromFirebase() {
        refreshLayout.setRefreshing(true);
        FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("results").orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    final ResultClass item = dsp.getValue(ResultClass.class);
                    itemList.add(item);


                    //add result into array list
                }
                mAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                if (itemList.size() == 0) {
                    noResult.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noResult.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }


                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
