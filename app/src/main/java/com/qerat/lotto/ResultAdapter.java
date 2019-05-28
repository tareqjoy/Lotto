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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    private List<ResultClass> itemList;
    private Context context;
    private String gameName;


    public class ResultViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView, dateTextView;
        private TableLayout lfnTable, xtraTable, ffnTable;
        private ResultClass item;
        private LinearLayout parent;

        public ResultViewHolder(View view) {
            super(view);
            codeTextView = view.findViewById(R.id.codeTextView);
            lfnTable = view.findViewById(R.id.lfnTable);
            xtraTable = view.findViewById(R.id.xtraTable);
            ffnTable = view.findViewById(R.id.ffnTable);
            dateTextView = view.findViewById(R.id.dateTextView);
            parent = view.findViewById(R.id.parent);
        }

        public ResultClass getItem() {
            return item;
        }

        public void setItem(ResultClass item) {
            this.item = item;
        }
    }


    public ResultAdapter(List<ResultClass> moviesList, String gameName, Context context) {
        this.itemList = moviesList;
        this.gameName = gameName;
        this.context = context;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_lotto_result_item, parent, false);

        return new ResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        final ResultClass item = itemList.get(position);

        holder.codeTextView.setText(item.getCode());
        holder.dateTextView.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date(-item.getDate())));

        for (int i = 1, c1 = holder.ffnTable.getChildCount(); i < c1; i++) {
            View view = holder.ffnTable.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                for (int j = 0,k=0, c2 = row.getChildCount(); j < c2; j++) {
                    View viewEditText = row.getChildAt(j);
                    if (viewEditText instanceof TextView) {
                        TextView textView = (TextView) viewEditText;
                        textView.setText(item.getFfn().get(k));
                        k++;
                    }
                }

            }
        }

        for (int i = 1, c1 = holder.xtraTable.getChildCount(); i < c1; i++) {
            View view = holder.xtraTable.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                for (int j = 0,k=0, c2 = row.getChildCount(); j < c2; j++) {
                    View viewEditText = row.getChildAt(j);
                    if (viewEditText instanceof TextView) {
                        TextView textView = (TextView) viewEditText;
                        textView.setText(item.getXtra().get(k));
                        k++;
                    }
                }
            }
        }


        for (int i = 1, c1 = holder.lfnTable.getChildCount(); i < c1; i++) {
            View view = holder.lfnTable.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;

                for (int j = 0,k=0, c2 = row.getChildCount(); j < c2; j++) {
                    View viewEditText = row.getChildAt(j);
                    if (viewEditText instanceof TextView) {
                        TextView textView = (TextView) viewEditText;
                        textView.setText(item.getLfn().get(k));
                        k++;
                    }
                }

            }
        }


        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String[] colors = {"Edit"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(item.getCode());
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case 0:
                                Intent myIntent = new Intent(context, ResultAddActivity.class);
                                //Optional parameters
                                myIntent.putExtra("gameName", gameName);
                                myIntent.putExtra("result", item);
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
