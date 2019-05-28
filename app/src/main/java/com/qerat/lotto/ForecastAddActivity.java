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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class ForecastAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText dateEditText, bankerEditText, sure21EditText, sure22EditText, best51EditText, best52EditText, best53EditText, best54EditText, best55EditText;
    private Spinner gameNameSpinner;
    private ProgressBar loadingProgressbar;
    private Button cancelButton, saveButton, deleteButton;
    private boolean editDlg = false;
    private ForecastClass oldForecast;

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
        setContentView(R.layout.activity_add_forecast);

        setTitle("Add Forecast");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        if (getIntent().getSerializableExtra("item") != null) {

            setTitle("Edit Forecast");
            editDlg = true;

            deleteButton.setVisibility(View.VISIBLE);
            oldForecast = (ForecastClass) getIntent().getSerializableExtra("item");
            dateEditText.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date(-oldForecast.getDate())));
            bankerEditText.setText(oldForecast.getBanker());
            sure21EditText.setText(oldForecast.getSure2_1());
            sure22EditText.setText(oldForecast.getSure2_2());
            best51EditText.setText(oldForecast.getBest5_1());
            best52EditText.setText(oldForecast.getBest5_2());
            best53EditText.setText(oldForecast.getBest5_3());
            best54EditText.setText(oldForecast.getBest5_4());
            best55EditText.setText(oldForecast.getBest5_5());
        }
        loadProductsToSpinner();
    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) ForecastAddActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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

    private void setLoadingProgressbar() {
        loadingProgressbar.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);
        gameNameSpinner.setEnabled(false);

    }

    private void unsetLoadingProgressbar() {
        loadingProgressbar.setVisibility(View.GONE);
        saveButton.setEnabled(true);
        gameNameSpinner.setEnabled(true);
    }


    private void loadProductsToSpinner() {
        setLoadingProgressbar();

        FirebaseUtilClass.getDatabaseReference().child("Game").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> loc = new ArrayList<>();


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    String l = dsp.getKey(); //add result into array list
                    loc.add(l);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ForecastAddActivity.this, android.R.layout.simple_spinner_item, loc);
                ;
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gameNameSpinner.setAdapter(adapter);
                if (editDlg) {
                    gameNameSpinner.setSelection(getIndexFromSpinner(gameNameSpinner, oldForecast.getGameName()));
                }
                unsetLoadingProgressbar();
                // mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForecastAddActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                unsetLoadingProgressbar();
                finish();
            }
        });
    }

    private void init() {
        gameNameSpinner = findViewById(R.id.gameNameSpinner);
        dateEditText = findViewById(R.id.dateEditText);
        cancelButton = findViewById(R.id.cancelButton);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        bankerEditText = findViewById(R.id.bankerEditText);
        sure21EditText = findViewById(R.id.sure2EditText1);
        sure22EditText = findViewById(R.id.sure2EditText2);
        best51EditText = findViewById(R.id.best5EditText1);
        best52EditText = findViewById(R.id.best5EditText2);
        best53EditText = findViewById(R.id.best5EditText3);
        best54EditText = findViewById(R.id.best5EditText4);
        best55EditText = findViewById(R.id.best5EditText5);
        loadingProgressbar = findViewById(R.id.loadingProgressbar);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editDlg) {


                    int day = Integer.parseInt(DateFormat.format("dd", -oldForecast.getDate()).toString()); // 20
                    int monthNumber = Integer.parseInt(DateFormat.format("MM", -oldForecast.getDate()).toString()); // 06
                    int year = Integer.parseInt(DateFormat.format("yyyy", -oldForecast.getDate()).toString()); // 2013
                    new DatePickerDialog(
                            ForecastAddActivity.this, ForecastAddActivity.this, year, monthNumber - 1, day).show();
                } else {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(
                            ForecastAddActivity.this, ForecastAddActivity.this, mYear, mMonth, mDay).show();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameNameSpinner.getSelectedItem() == null) {
                    Toast.makeText(ForecastAddActivity.this, "Please add a game first", Toast.LENGTH_SHORT).show();
                    return;
                }

                hideKeyboard();

                if (TextUtils.isEmpty(bankerEditText.getText().toString().trim())) {
                    bankerEditText.setError("Can't be empty!");
                    bankerEditText.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(sure21EditText.getText().toString().trim())) {
                    sure21EditText.setError("Can't be empty!");
                    sure21EditText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(sure22EditText.getText().toString().trim())) {
                    sure22EditText.setError("Can't be empty!");
                    sure22EditText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(best51EditText.getText().toString().trim())) {
                    best51EditText.setError("Can't be empty!");
                    best51EditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(best52EditText.getText().toString().trim())) {
                    best52EditText.setError("Can't be empty!");
                    best52EditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(best53EditText.getText().toString().trim())) {
                    best53EditText.setError("Can't be empty!");
                    best53EditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(best54EditText.getText().toString().trim())) {
                    best54EditText.setError("Can't be empty!");
                    best54EditText.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(best55EditText.getText().toString().trim())) {
                    best55EditText.setError("Can't be empty!");
                    best55EditText.requestFocus();
                    return;
                }


                String dateString = dateEditText.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                try {
                    date = sdf.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long startDate = date.getTime();


                writeToFirebase(new ForecastClass(gameNameSpinner.getSelectedItem().toString(), startDate, bankerEditText.getText().toString()
                        , sure21EditText.getText().toString(), sure22EditText.getText().toString(), best51EditText.getText().toString()
                        , best52EditText.getText().toString(), best53EditText.getText().toString(), best54EditText.getText().toString()
                        , best55EditText.getText().toString()));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });

        if (editDlg) {

        } else {
            Date todaysdate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String date = format.format(todaysdate);
            dateEditText.setText(date);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ForecastAddActivity.this)
                        .setTitle("Delete forecast")
                        .setMessage("Are you sure you want to delete this forecast?")

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


    }

    private void deleteData() {
        saving();
        FirebaseUtilClass.getDatabaseReference().child("Forecast").child(oldForecast.getGameName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notSaving();
                finish();
                Toast.makeText(ForecastAddActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notSaving();
                Toast.makeText(ForecastAddActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getIndexFromSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }


    private void writeToFirebase(final ForecastClass resultClass) {
        saving();
        FirebaseUtilClass.getDatabaseReference().child("Forecast").child(resultClass.getGameName()).setValue(resultClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notSaving();
                finish();
                Toast.makeText(ForecastAddActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                notSaving();
                Toast.makeText(ForecastAddActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
