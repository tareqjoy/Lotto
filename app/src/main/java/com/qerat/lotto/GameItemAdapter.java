package com.qerat.lotto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class GameItemAdapter extends RecyclerView.Adapter<GameItemAdapter.GameViewHolder> {
    private List<Pair<String, String>> itemList;
    private Context context;


    public class GameViewHolder extends RecyclerView.ViewHolder {
        private TextView gameNameTextView, lastUpdatedTextView;
        private String item;
        private LinearLayout parent;

        public GameViewHolder(View view) {
            super(view);
            gameNameTextView = view.findViewById(R.id.gameNameTextView);
            lastUpdatedTextView = view.findViewById(R.id.lastUpdatedTextView);
            parent = view.findViewById(R.id.parent);
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }


    public GameItemAdapter(List<Pair<String, String>> moviesList, Context context) {
        this.itemList = moviesList;

        this.context = context;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_lotto_item, parent, false);

        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        final String itemStr = itemList.get(position).first;
        final String date = itemList.get(position).second;
        holder.gameNameTextView.setText(itemStr);
        holder.lastUpdatedTextView.setText(date);
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = {"Edit"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(itemStr);
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case 0:
                                new GameAddDialog(context, itemStr).show();

                                break;
                        }
                    }
                });
                builder.show();

                return false;
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, ResultActivity.class);
                myIntent.putExtra("gameName", itemStr); //Optional parameters
                context.startActivity(myIntent);
            }
        });

        holder.setItem(itemStr);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
