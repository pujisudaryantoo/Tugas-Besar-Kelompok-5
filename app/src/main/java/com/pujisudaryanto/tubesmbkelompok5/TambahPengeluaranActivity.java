package com.pujisudaryanto.tubesmbkelompok5;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TambahPengeluaranActivity extends AppCompatActivity {

    DatePickerDialog picker;
    EditText eText, ePengeluaran, eHarga;

    protected Cursor cursor;
    com.pujisudaryanto.tubesmbkelompok5.DatabaseHelper dbHelper;
    Button btnSimpan;

    private AdView mAdView;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pengeluaran);

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

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.btnTanggalTambah, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);

        //admob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        /*for test*/
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("323C4BAC43608B9E353C1E674F09F67C").build();
//        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        dbHelper = new com.pujisudaryanto.tubesmbkelompok5.DatabaseHelper(this);

        //edit text
        eText=(EditText) findViewById(R.id.btnTanggalTambah);
        ePengeluaran =(EditText) findViewById(R.id.btnPengeluaranTambah);
        eHarga = (EditText)findViewById(R.id.btnHargaTambah);

        //button
        btnSimpan = (Button)findViewById(R.id.btnSimpan);

        //google calendar
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(TambahPengeluaranActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                int month = monthOfYear + 1;
                                String formattedMonthOfYear = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;
                                if(month < 10){
                                    formattedMonthOfYear = "0" + month;
                                }
                                if(dayOfMonth < 10){

                                    formattedDayOfMonth  = "0" + dayOfMonth ;
                                }
                                eText.setText(year + "-" + formattedMonthOfYear + "-" + formattedDayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (ePengeluaran.getText().toString().equals("") || eText.getText().toString().equals("") || eHarga.getText().toString().equals("")){
                    Toast.makeText(TambahPengeluaranActivity.this,"Semua Data Harus Diisi!", Toast.LENGTH_SHORT ).show();
                }else {
                    if(isValidDate(eText.getText().toString())){
//                        Toast.makeText(TambahPengeluaranActivity.this,"cocok!", Toast.LENGTH_SHORT ).show();
                        // TODO Auto-generated method stub
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("insert into tb_pengeluaran(title, date, fee, category) values('" +
                                ePengeluaran.getText().toString() + "','" +
                                eText.getText().toString() + "','" +
                                eHarga.getText().toString() + "','pengeluaran')");
                        Toast.makeText(getApplicationContext(), "Berhasil Disimpan", Toast.LENGTH_LONG).show();
                        com.pujisudaryanto.tubesmbkelompok5.MainActivity.ma.RefreshList();
                        com.pujisudaryanto.tubesmbkelompok5.MainActivity.ma.GetPengeluaranBulanIni();
                        com.pujisudaryanto.tubesmbkelompok5.MainActivity.ma.GetPengeluaranHariIni();
                        com.pujisudaryanto.tubesmbkelompok5.MainActivity.ma.GetPengeluaranKemarin();
                        finish();
                    }else{
                        Toast.makeText(TambahPengeluaranActivity.this,"Format tanggal salah!", Toast.LENGTH_SHORT ).show();
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
