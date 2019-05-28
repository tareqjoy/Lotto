package com.qerat.lotto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private List<ForecastClass> itemList;
    private Context context;


    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView gameNameTextView, dateTextView, bankerTextView, sure21TextView, sure22TextView, best51TextView, best52TextView, best53TextView, best54TextView, best55TextView;
        private ForecastClass item;
        private LinearLayout parent;

        public ForecastViewHolder(View view) {
            super(view);
            gameNameTextView = view.findViewById(R.id.gameNameTextView);
            bankerTextView = view.findViewById(R.id.bankerTextView);
            sure21TextView = view.findViewById(R.id.sure2TextView1);
            sure22TextView = view.findViewById(R.id.sure2TextView2);
            best51TextView = view.findViewById(R.id.best5TextView1);
            best52TextView = view.findViewById(R.id.best5TextView2);
            best53TextView = view.findViewById(R.id.best5TextView3);
            best54TextView = view.findViewById(R.id.best5TextView4);
            best55TextView = view.findViewById(R.id.best5TextView5);
            dateTextView = view.findViewById(R.id.dateTextView);
            parent = view.findViewById(R.id.parent);
        }

        public ForecastClass getItem() {
            return item;
        }

        public void setItem(ForecastClass item) {
            this.item = item;
        }
    }


    public ForecastAdapter(List<ForecastClass> moviesList, Context context) {
        this.itemList = moviesList;
        this.context = context;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_forecast, parent, false);

        return new ForecastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        final ForecastClass item = itemList.get(position);

        holder.gameNameTextView.setText(item.getGameName());
        holder.dateTextView.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date(-item.getDate())));

        holder.bankerTextView.setText(item.getBanker());
        holder.sure21TextView.setText(item.getSure2_1());
        holder.sure22TextView.setText(item.getSure2_2());
        holder.best51TextView.setText(item.getBest5_1());
        holder.best52TextView.setText(item.getBest5_2());
        holder.best53TextView.setText(item.getBest5_3());
        holder.best54TextView.setText(item.getBest5_4());
        holder.best55TextView.setText(item.getBest5_5());

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = {"Edit"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getGameName());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case 0:
                                Intent myIntent = new Intent(context, ForecastAddActivity.class);
                                //Optional parameters
                                myIntent.putExtra("item", item);
                                context.startActivity(myIntent);

                                break;
                        }
                    }
                });
                builder.show();

                return false;
            }
        });


        holder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
