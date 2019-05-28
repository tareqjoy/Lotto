package com.qerat.lotto;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResultAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText codeEditText, dateEditText;
    private Button cancelButton, saveButton, deleteButton;
    private TableLayout lfnTable, xtratable, ffnTable;
    private boolean editDlg = false;
    private String gameName;
    private ResultClass oldResult;

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
        setContentView(R.layout.activity_add_result);

        setTitle("Add Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gameName = getIntent().getStringExtra("gameName");
        init();

        if (getIntent().getSerializableExtra("result") != null) {

            setTitle("Edit Result");
            editDlg = true;
            codeEditText.setEnabled(false);
            codeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ResultAddActivity.this, "You can't edit code now. You can delete then add new one.", Toast.LENGTH_SHORT).show();
                }
            });

            deleteButton.setVisibility(View.VISIBLE);
            oldResult = (ResultClass) getIntent().getSerializableExtra("result");

            codeEditText.setText(oldResult.getCode());
            dateEditText.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date(-oldResult.getDate())));

            for (int i = 1, c1 = ffnTable.getChildCount(); i < c1; i++) {
                View view = ffnTable.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow row = (TableRow) view;
                    for (int j = 0, k = 0, c2 = row.getChildCount(); j < c2; j++) {
                        View viewEditText = row.getChildAt(j);
                        if (viewEditText instanceof EditText) {
                            EditText editText = (EditText) viewEditText;
                            if (oldResult.getFfn().get(k).equals("N/A")) {
                                editText.setText("");
                            } else {
                                editText.setText(oldResult.getFfn().get(k));
                            }

                            k++;
                        }
                    }

                }
            }

            for (int i = 1, c1 = xtratable.getChildCount(); i < c1; i++) {
                View view = xtratable.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow row = (TableRow) view;
                    for (int j = 0, k = 0, c2 = row.getChildCount(); j < c2; j++) {
                        View viewEditText = row.getChildAt(j);
                        if (viewEditText instanceof EditText) {
                            EditText editText = (EditText) viewEditText;

                            if (oldResult.getXtra().get(k).equals("N/A")) {
                                editText.setText("");
                            } else {
                                editText.setText(oldResult.getXtra().get(k));
                            }
                            k++;
                        }
                    }
                }
            }


            for (int i = 1, c1 = lfnTable.getChildCount(); i < c1; i++) {
                View view = lfnTable.getChildAt(i);
                if (view instanceof TableRow) {
                    TableRow row = (TableRow) view;

                    for (int j = 0, k = 0, c2 = row.getChildCount(); j < c2; j++) {
                        View viewEditText = row.getChildAt(j);
                        if (viewEditText instanceof EditText) {
                            EditText editText = (EditText) viewEditText;
                            if (oldResult.getLfn().get(k).equals("N/A")) {
                                editText.setText("");
                            } else {
                                editText.setText(oldResult.getLfn().get(k));
                            }
                            k++;
                        }
                    }

                }
            }

        }

    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) ResultAddActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void saving() {
        saveButton.setText("Saving");
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void notSaving() {
        saveButton.setText("Save");
        saveButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    private void init() {
        codeEditText = findViewById(R.id.code);
        dateEditText = findViewById(R.id.date);
        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        lfnTable = findViewById(R.id.lfnTable);
        xtratable = findViewById(R.id.xtraTablle);
        ffnTable = findViewById(R.id.ffnTable);


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editDlg) {


                    int day = Integer.parseInt(DateFormat.format("dd", -oldResult.getDate()).toString()); // 20
                    int monthNumber = Integer.parseInt(DateFormat.format("MM", -oldResult.getDate()).toString()); // 06
                    int year = Integer.parseInt(DateFormat.format("yyyy", -oldResult.getDate()).toString()); // 2013
                    new DatePickerDialog(
                            ResultAddActivity.this, ResultAddActivity.this, year, monthNumber - 1, day).show();
                } else {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(
                            ResultAddActivity.this, ResultAddActivity.this, mYear, mMonth, mDay).show();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(codeEditText.getText().toString().trim())) {
                    codeEditText.setError("This field can't be empty");
                    codeEditText.requestFocus();
                    return;
                }

                hideKeyboard();
                ResultClass result = new ResultClass();

                result.setCode(codeEditText.getText().toString());

                String dateString = dateEditText.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long startDate = date.getTime();

                result.setDate(startDate);

                for (int i = 1, c1 = ffnTable.getChildCount(); i < c1; i++) {
                    View view = ffnTable.getChildAt(i);
                    if (view instanceof TableRow) {
                        TableRow row = (TableRow) view;
                        List<String> ffnArray = new ArrayList<>();
                        for (int j = 0, c2 = row.getChildCount(); j < c2; j++) {
                            View viewEditText = row.getChildAt(j);
                            if (viewEditText instanceof EditText) {
                                EditText editText = (EditText) viewEditText;
                                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                                    ffnArray.add("N/A");
                                } else {
                                    ffnArray.add(editText.getText().toString());
                                }

                            }
                        }
                        result.setFfn(ffnArray);
                    }
                }

                for (int i = 1, c1 = xtratable.getChildCount(); i < c1; i++) {
                    View view = xtratable.getChildAt(i);
                    if (view instanceof TableRow) {
                        TableRow row = (TableRow) view;
                        List<String> xtraArray = new ArrayList<>();
                        for (int j = 0, c2 = row.getChildCount(); j < c2; j++) {
                            View viewEditText = row.getChildAt(j);
                            if (viewEditText instanceof EditText) {
                                EditText editText = (EditText) viewEditText;
                                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                                    xtraArray.add("N/A");
                                } else {
                                    xtraArray.add(editText.getText().toString());
                                }
                            }
                        }
                        result.setXtra(xtraArray);
                    }
                }


                for (int i = 1, c1 = lfnTable.getChildCount(); i < c1; i++) {
                    View view = lfnTable.getChildAt(i);
                    if (view instanceof TableRow) {
                        TableRow row = (TableRow) view;
                        List<String> lfnArray = new ArrayList<>();
                        for (int j = 0, c2 = row.getChildCount(); j < c2; j++) {
                            View viewEditText = row.getChildAt(j);
                            if (viewEditText instanceof EditText) {
                                EditText editText = (EditText) viewEditText;
                                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                                    lfnArray.add("N/A");
                                } else {
                                    lfnArray.add(editText.getText().toString());
                                }
                            }
                        }
                        result.setLfn(lfnArray);
                    }
                }

                writeToFirebase(result);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ResultAddActivity.this)
                        .setTitle("Delete result")
                        .setMessage("Are you sure you want to delete this result?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteData();
                            }
                        })


                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        if (editDlg) {

        } else {
            Date todaysdate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String date = format.format(todaysdate);
            dateEditText.setText(date);
        }


    }

    private void deleteData() {
        saving();
        FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("results").child(oldResult.getCode()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("lastUpdated").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        long oldDate;
                        try {
                            oldDate = dataSnapshot.getValue(long.class);
                            if (oldDate == (oldResult.getDate() * (-1))) {
                                FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("results").orderByChild("date").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                                String gg = childSnapshot.getKey();
                                                Long newDate = Long.parseLong(dataSnapshot.child(gg).child("date").getValue().toString());

                                                FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("lastUpdated").setValue(-1 * newDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(ResultAddActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ResultAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                            }


                                        } else {
                                            FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("lastUpdated").setValue("no update yet").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ResultAddActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        notSaving();
                                        Toast.makeText(ResultAddActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notSaving();
                Toast.makeText(ResultAddActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void writeToFirebase(final ResultClass resultClass) {
        saving();
        FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("results").child(resultClass.getCode()).setValue(resultClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("lastUpdated").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            long oldDate = dataSnapshot.getValue(long.class);
                            if (oldDate < (resultClass.getDate() * (-1))) {
                                FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("lastUpdated").setValue(-resultClass.getDate());

                            }
                        } catch (Exception e) {
                            FirebaseUtilClass.getDatabaseReference().child("Game").child(gameName).child("lastUpdated").setValue(-resultClass.getDate());
                        }
                        finish();
                        Toast.makeText(ResultAddActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        notSaving();
                        Toast.makeText(ResultAddActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notSaving();
                Toast.makeText(ResultAddActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%02d", dayOfMonth));
        stringBuilder.append("-");
        stringBuilder.append(String.format("%02d", month + 1));
        stringBuilder.append("-");
        stringBuilder.append(year);

        dateEditText.setText(stringBuilder.toString());
    }
}
