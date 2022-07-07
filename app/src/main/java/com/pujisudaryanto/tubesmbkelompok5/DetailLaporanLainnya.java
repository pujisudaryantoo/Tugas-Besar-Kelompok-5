package com.pujisudaryanto.tubesmbkelompok5;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DetailLaporanLainnya extends AppCompatActivity implements com.pujisudaryanto.tubesmbkelompok5.GroceryRecyclerViewAdapter.RecyclerImageAdapter{
    DatabaseHelper dbcenter;
    public static DetailLaporanLainnya ma;
    protected Cursor cursor;

    private RecyclerView recyclerView;
    private com.pujisudaryanto.tubesmbkelompok5.GroceryRecyclerViewAdapter adapter;
    private ArrayList<Grocery> groceryArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    private String t1,t2;

    DatePickerDialog picker;

    Locale localeID = new Locale("in", "ID");
    private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_lainnya);

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

        AdRequest adRequest = new AdRequest.Builder().addTestDevice("323C4BAC43608B9E353C1E674F09F67C").build();
//        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(DetailLaporanLainnya.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));



        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
        // Call displayInterstitial() function
                displayInterstitial();
            }
        });

        ma = this;
        dbcenter = new DatabaseHelper(this);

        Intent intent = getIntent();


        final String tanggalAwal = getIntent().getStringExtra("tanggalAwal");
        final String tanggalAkhir = getIntent().getStringExtra("tanggalAkhir");

        this.t1 = tanggalAwal;
        this.t2 = tanggalAkhir;

//        Toast.makeText(DetailLaporanLainnya.this, tanggalAkhir, Toast.LENGTH_LONG).show();
        GetPengeluaranBulanIni(tanggalAwal, tanggalAkhir);
        RefreshList(tanggalAwal, tanggalAkhir);

        ImageView imgHalamanUtama = findViewById(R.id.btnHalamanUtama);
        imgHalamanUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailLaporanLainnya.this,MainActivity.class);
                startActivity(i);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshList(tanggalAwal, tanggalAkhir);
                GetPengeluaranBulanIni(tanggalAwal, tanggalAkhir);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void displayInterstitial() {
    // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    public void GetPengeluaranBulanIni(String firstDate, String secondDate){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int pengeluaranBulanIni;
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        String q = "SELECT SUM(fee) as total FROM tb_pengeluaran WHERE category = 'pengeluaran' and date BETWEEN '"+firstDate+"' AND '"+secondDate+"';";
        Log.d("keluarin",q);
        cursor = db.rawQuery(q, null);
        if(cursor.moveToFirst())
            pengeluaranBulanIni = cursor.getInt(0);
        else
            pengeluaranBulanIni = 0;
        cursor.close();
        TextView tTotalPengeluaran = findViewById(R.id.totalPengeluaranLihat);
        TextView tPeriodeLaporan = findViewById(R.id.periode);
//        String period = formateDateFromstring("YYYY-mm-dd","dd/mm/YYYY",firstDate).toString() +" s/d "+formateDateFromstring("YYYY-mm-dd","dd/mm/YYYY",secondDate).toString();
        String period = firstDate+" s/d "+secondDate;
        tPeriodeLaporan.setText(String.valueOf(period));
        tTotalPengeluaran.setText(String.valueOf(formatRupiah.format(pengeluaranBulanIni)));
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
            Log.d("Error : ", "ParseException - dateFormat");
        }

        return outputDate;

    }

    public void RefreshList(String firstDate, String secondDate) {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM tb_pengeluaran where category = 'pengeluaran'  and date BETWEEN '"+firstDate+"' AND '"+secondDate+"';", null);
        cursor.moveToFirst();

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            try {
                HashMap<String, String> showData = new HashMap<String, String>();
                showData.put("id_pengeluaran", cursor.getString(0).toString());
                showData.put("title", cursor.getString(1).toString());
                showData.put("date", cursor.getString(2).toString());
                showData.put("fee", "Rp. "+cursor.getString(3).toString());
                list.add(showData);
            } catch (Exception e) {
                Log.d("Exception : ",e.getMessage());
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.rvPengeluaranDetail);
        adapter = new com.pujisudaryanto.tubesmbkelompok5.GroceryRecyclerViewAdapter(new ArrayList<HashMap>(list), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onRecyclerImageSelected(HashMap groceryList) {
        String xImg = groceryList.get("fee").toString();
        final String idPengeluaran = groceryList.get("id_pengeluaran").toString();
        AlertDialog.Builder alert = new AlertDialog.Builder(DetailLaporanLainnya.this);
        alert.setTitle("Hapus Data");
        alert.setMessage("Apakah anda yakin akan menghapus data ini?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db = dbcenter.getReadableDatabase();
                db.delete("tb_pengeluaran","id_pengeluaran=? ",new String[]{idPengeluaran});
                cursor.moveToFirst();
                Toast.makeText(DetailLaporanLainnya.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                RefreshList(t1, t2);
                GetPengeluaranBulanIni(t1, t2);
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void onRecyclerLongClick(HashMap groceryList){

//        dialog = new AlertDialog.Builder(MainActivity.this);
//        inflater = getLayoutInflater();
//        dialogView = inflater.inflate(R.layout.activity_edit_data, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_edit_data,null);

        builder.setView(dialogView);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();

        final String id_pengeluaran = groceryList.get("id_pengeluaran").toString();
        final String title = groceryList.get("title").toString();
        final String date = groceryList.get("date").toString();
        final String fee = groceryList.get("fee").toString();

        String currentString = fee;
        String[] separated = currentString.split(". ");
        final String feeBaru = separated[1].trim();

        //        EditText id_pengeluaran_edit = dialogView.findViewById(R.id.tPengeluaranEdit);
        final EditText titleEdit = dialogView.findViewById(R.id.tPengeluaranEdit);
        final EditText feeEdit = dialogView.findViewById(R.id.tHargaEdit);
        final EditText tanggalEdit = dialogView.findViewById(R.id.tTanggalEdit);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);


        //google calendar
        tanggalEdit.setInputType(InputType.TYPE_NULL);
        tanggalEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(DetailLaporanLainnya.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                tanggalEdit.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        titleEdit.setText(title);
        feeEdit.setText(feeBaru);
        tanggalEdit.setText(date);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbcenter.getReadableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("title", titleEdit.getText().toString());
                cv.put("date", tanggalEdit.getText().toString());
                cv.put("fee", feeEdit.getText().toString());
                db.update("tb_pengeluaran", cv, "id_pengeluaran="+id_pengeluaran, null);
                cursor.moveToFirst();
                Toast.makeText(DetailLaporanLainnya.this, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show();
                RefreshList(t1, t2);
                GetPengeluaranBulanIni(t1, t2);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
