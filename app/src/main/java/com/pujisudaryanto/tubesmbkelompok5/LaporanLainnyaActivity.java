package com.pujisudaryanto.tubesmbkelompok5;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LaporanLainnyaActivity extends AppCompatActivity {

    DatePickerDialog picker;
    EditText eText,eText2;
    Button btnLihat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_lainnya);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.blueColor));

        eText = findViewById(R.id.tanggalDari);
        eText2 = findViewById(R.id.tanggalSampai);

        //tanggal dari
        eText=(EditText) findViewById(R.id.tanggalDari);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(LaporanLainnyaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;
                                if(month < 10){
                                    formattedMonth = "0" + month;
                                }
                                if(dayOfMonth < 10){

                                    formattedDayOfMonth  = "0" + dayOfMonth ;
                                }
                                eText.setText(  year + "-" + formattedMonth + "-" + formattedDayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        eText2=(EditText) findViewById(R.id.tanggalSampai);
        eText2.setInputType(InputType.TYPE_NULL);
        eText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(LaporanLainnyaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;
                                if(month < 10){
                                    formattedMonth = "0" + month;
                                }
                                if(dayOfMonth < 10){
                                    formattedDayOfMonth  = "0" + dayOfMonth ;
                                }
                                eText2.setText(  year + "-" + formattedMonth + "-" + formattedDayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

            btnLihat = findViewById(R.id.btnLihatLaporanLainnya);
            btnLihat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (eText.getText().toString().equals("") || eText2.getText().toString().equals("")){
                        Toast.makeText(LaporanLainnyaActivity.this,"Data tidak boleh kosong!!", Toast.LENGTH_SHORT ).show();
                    }else {
                        if(!isValidDate(eText.getText().toString())){
                            Toast.makeText(LaporanLainnyaActivity.this,"Format tanggal salah!!", Toast.LENGTH_SHORT ).show();
                        }else if(!isValidDate(eText2.getText().toString())) {
                            Toast.makeText(LaporanLainnyaActivity.this,"Format tanggal salah!!", Toast.LENGTH_SHORT ).show();
                        } else{
                            String tanggalAwal, tanggalAkhir;
                            tanggalAwal = eText.getText().toString();
                            tanggalAkhir = eText2.getText().toString();
                            Intent i = new Intent(getApplicationContext(),DetailLaporanLainnya.class);
                            i.putExtra("tanggalAwal", tanggalAwal);
                            i.putExtra("tanggalAkhir", tanggalAkhir);
                            startActivity(i);
                        }

                    }
                }
            });
    }

    public boolean isValidDate(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        String errorMessage;
        try {
            testDate = sdf.parse(date);
        }
        catch (ParseException e) {
            return false;
        }
        if (!sdf.format(testDate).equals(date))
        {
            return false;
        }
        return true;
    }
}
