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

public class ForecastActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ForecastClass> itemList = new ArrayList<>();
    private ForecastAdapter mAdapter;
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
        setContentView(R.layout.activity_forecast);


        setTitle("Forecast");

        FloatingActionButton fab = findViewById(R.id.fab);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.forecastContainerRecyclerView);
        noResult = findViewById(R.id.noResultsTextView);


        mAdapter = new ForecastAdapter(itemList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ForecastActivity.this, ForecastAddActivity.class);
                //Optional parameters

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
        FirebaseUtilClass.getDatabaseReference().child("Forecast").orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    final ForecastClass item = dsp.getValue(ForecastClass.class);
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
