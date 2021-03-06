package com.qerat.lotto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Pair<String, String>> itemList = new ArrayList<>();
    private GameItemAdapter mAdapter;
    private TextView noGame;
    private LinearLayout forecastLinearLayout;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.gameContainer);
        noGame = findViewById(R.id.noGame);
        forecastLinearLayout = findViewById(R.id.forecastLinearLayout);
        refreshLayout = findViewById(R.id.refreshLayout);


        mAdapter = new GameItemAdapter(itemList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        forecastLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameActivity.this, ForecastActivity.class);
                startActivity(myIntent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GameAddDialog(GameActivity.this).show();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readDataFromFirebase();
            }
        });

        readDataFromFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void readDataFromFirebase() {
        refreshLayout.setRefreshing(true);

        FirebaseUtilClass.getDatabaseReference().child("Game").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    final String name = dsp.getKey();
                    String date;

                    try {
                        Long oldDate = Long.parseLong(dsp.child("lastUpdated").getValue().toString());
                        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date(oldDate));
                    } catch (Exception e) {
                        date = dsp.child("lastUpdated").getValue().toString();
                    }


                    itemList.add(new Pair<String, String>(name, date));


                    //add result into array list
                }
                if (itemList.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noGame.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noGame.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);


                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                refreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
