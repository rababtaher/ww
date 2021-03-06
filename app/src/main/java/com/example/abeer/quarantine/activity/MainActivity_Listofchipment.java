package com.example.abeer.quarantine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.abeer.quarantine.R;
import com.example.abeer.quarantine.adapter.AdapterCheckRequest;
import com.example.abeer.quarantine.databinding.ActivityMainListofchipmentBinding;
import com.example.abeer.quarantine.functions.Public_function;
import com.example.abeer.quarantine.model.RequestTreatmentData;
import com.example.abeer.quarantine.presenter.ClickCustomCheckRequest;
import com.example.abeer.quarantine.remote.ApiCall;
import com.example.abeer.quarantine.remote.PlantQurDBHelper;
import com.example.abeer.quarantine.remote.data.DataManger;
import com.example.abeer.quarantine.remote.data.IDataValue;
import com.example.abeer.quarantine.viewmodel.Emp_Committe;
import com.example.abeer.quarantine.viewmodel.ExportCheckRequest;
import com.example.abeer.quarantine.viewmodel.ListItemDataDetail;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity_Listofchipment extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataManger dataManger;
    final List<ExportCheckRequest>[] exportCheckRequestList = new List[1];
    List<ExportCheckRequest>[] totalexportCheckRequestList = new List[1];
    List<ExportCheckRequest> EX_exportCheckRequestList = new ArrayList<>();
    List<ExportCheckRequest> IM_exportCheckRequestList = new ArrayList<>();
    List<ExportCheckRequest> EXTRA_exportCheckRequestList = new ArrayList<>();
    RequestTreatmentData requestTreatmentData = new RequestTreatmentData();
    ActivityMainListofchipmentBinding activityMainListofchipmentBinding;
    String ipadrass;
    Gson gson;
    Context context = this;
    Button shownavbar;
    DrawerLayout drawer;
    LocationManager manager;
    Location location;
    boolean checked;
    AdapterCheckRequest adapterCheckRequest;
    //  String Token;
    Long EmpId;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;
    Public_function public_function = new Public_function();
    PlantQurDBHelper plantQurDBHelper;
    String checkdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            activityMainListofchipmentBinding = DataBindingUtil.setContentView((Activity) context, R.layout.activity_main__listofchipment);
        } catch (Exception ex) {
            int s = 10;
        }
        shownavbar = findViewById(R.id.shownavbar);
        dataManger = new DataManger(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPreferences = getApplicationContext().getSharedPreferences("SharedPreference", 0);
        ipadrass = sharedPreferences.getString("ipadrass", "");
        //    Token=sharedPreferences.getString("Token","");
        EmpId = sharedPreferences.getLong("EmpId", 0);
        // Toast.makeText(context, Token, Toast.LENGTH_SHORT).show();
    }

//   shownavbar.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            drawer.openDrawer(GravityCompat.START);
//        }
//    });

    @Override
    protected void onStart() {
        super.onStart();
        get_list_chipment();
        // filladapter(EX_exportCheckRequestList);
        // fill_list_chipment();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = public_function.getlocation(context, manager);

//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
//        }
//        location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if(location==null){
//            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        }
    }

    public void get_list_chipment() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date(System.currentTimeMillis());
        checkdate = String.valueOf(formatter.format(date));
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            String url = ipadrass + ApiCall.UrlListOfChipment + "User_Id=" + EmpId + "&Check_Date=" + checkdate;
            dataManger.SendVollyRequestJsonArrayGet(this, Request.Method.GET, url, new IDataValue() {
                @Override
                public void Success(Object response) throws JSONException {
                    String Result = response.toString();
                    gson = new Gson();
                    totalexportCheckRequestList[0] = Arrays.asList(gson.fromJson(Result, ExportCheckRequest[].class));
                    int size = totalexportCheckRequestList[0].size();
                    int isExport;
                    for (int x = 0; x < size; x++) {
                        isExport = totalexportCheckRequestList[0].get(x).getIsExport();
                        if (isExport == 1) {
                            EX_exportCheckRequestList.add(totalexportCheckRequestList[0].get(x));
                        } else if (isExport == 2) {
                            IM_exportCheckRequestList.add(totalexportCheckRequestList[0].get(x));
                        } else {
                            EXTRA_exportCheckRequestList.add(totalexportCheckRequestList[0].get(x));
                        }
                    }
                    filladapter(EX_exportCheckRequestList);
                }

                @Override
                public void Error(VolleyError error) {

                }
            });
        }else {

        }
    }

    public void filladapter(final List<ExportCheckRequest> CheckRequestList) {

        adapterCheckRequest = new AdapterCheckRequest(CheckRequestList, context, new ClickCustomCheckRequest() {
            @Override
            public void CheckRequest_click(View view, ExportCheckRequest exportCheckRequest) {
                if (exportCheckRequest.getIsExport() == 2 || exportCheckRequest.getIsExport() == 3) {
                    public_function.AlertDialog("الصفحه جاري العمل عليها ", context, false);
                } else {
                    prefsEditor = sharedPreferences.edit();
                    prefsEditor.putString("num_Request", exportCheckRequest.getCheckRequest_Number());        // Saving integer
                    prefsEditor.putString("checkRequest_Id", String.valueOf(exportCheckRequest.getCheckRequest_Id()));
                    prefsEditor.putLong("Committee_ID", Long.valueOf(exportCheckRequest.getCommittee_ID()));
                    prefsEditor.putInt("RequestCommittee_Status_Id", exportCheckRequest.getRequestCommittee_Status_Id());
                    prefsEditor.putString("BarCode", exportCheckRequest.getBarCode());
                    prefsEditor.putInt("Committee_Type_Id", exportCheckRequest.getCommittee_Type_Id());

                    prefsEditor.putInt("request_data", 1);

                    if (location.getLongitude() != 0 && location.getLatitude() != 0) {
                        prefsEditor.putLong("Latitude", (long) location.getLatitude());
                        prefsEditor.putLong("Longitude", (long) location.getLongitude());
                        Toast.makeText(MainActivity_Listofchipment.this, "" + location.getLatitude() + location.getLongitude(), Toast.LENGTH_LONG).show();
                    }
                    ArrayList<Emp_Committe> emp_committeArrayList = new ArrayList<>();
                    HashMap<String, Integer> hashMap = new HashMap<>();
                    try {
                        emp_committeArrayList.addAll(exportCheckRequest.Handle_Emp_Committe());
                        for (int j = 0; j < emp_committeArrayList.size(); j++) {
                            if (emp_committeArrayList.get(j).getISAdmin().equals((short) 1) && emp_committeArrayList.get(j).getEmployee_Id().equals(EmpId)) {
                                prefsEditor.putBoolean("ISAdmin", true);
                            } else if (emp_committeArrayList.get(j).getISAdmin().equals((short) 0) && emp_committeArrayList.get(j).getEmployee_Id().equals(EmpId)) {
                                prefsEditor.putBoolean("ISAdmin", false);
                            }
                        }
                        //last version
                        //     hashMap.putAll(exportCheckRequest.Handel_Request_Treatment());
                        // int treatment_data=hashMap.get("treatment_data").intValue();
                        // int sample_data=hashMap.get("sample_data").intValue();
                        // int request_data=hashMap.get("request_data").intValue();
                        //////////
                        //   hashMap.putAll(exportCheckRequest.getRequest_Treatment_Data().);
                        requestTreatmentData = exportCheckRequest.getRequest_Treatment_Data();
                        int treatment_data = requestTreatmentData.getTreatment_Total();
                        int sample_data = requestTreatmentData.getAnalysis_Total();
                        //  int request_data = requestTreatmentData.getCheck_Total();
                        int request_data = 1;
                        if (treatment_data == -1) {
                            treatment_data = 0;
                        }
                        if (sample_data == -1) {
                            sample_data = 0;
                        }
                        if (request_data == -1) {
                            request_data = 0;
                        }
                        //last version
//                    prefsEditor.putInt("treatment_data",hashMap.get("treatment_data").intValue());
//                    prefsEditor.putInt("sample_data",hashMap.get("sample_data").intValue());
//                    prefsEditor.putInt("request_data",hashMap.get("request_data").intValue());
                        ////////

                        prefsEditor.putInt("treatment_data", treatment_data);
                        prefsEditor.putInt("sample_data", sample_data);
                        prefsEditor.putInt("request_data", request_data);
                        prefsEditor.putInt("totalprocess", treatment_data + sample_data + request_data);

                        ///////////////////////////sql////////////
//                        String CheckRequest_Number,String BarCode,int Committee_Type_Id ,int Total_process,
//                        short IsExport,long Committee_ID,boolean RequestCommittee_Status_Id,int checkRequest_Id
                        plantQurDBHelper = new PlantQurDBHelper(context);
                        exportCheckRequest.setCheck_Date(checkdate);
                        if (plantQurDBHelper.Insert_CommitteeRequestEmployee(context, exportCheckRequest, emp_committeArrayList)) {
                            Toast.makeText(context, "good", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "حدث خطا ما ", Toast.LENGTH_SHORT).show();
                        }
                        ///////////////////////////////////////////////////////////
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    prefsEditor.apply();
                    Intent i = new Intent(context, MainActivity_DetailsListOfChimpments.class);
                    startActivity(i);
                }
            }
        });
        activityMainListofchipmentBinding.contentListofchipment.setAdapter(adapterCheckRequest);
        activityMainListofchipmentBinding.contentListofchipment.recycler.setLayoutManager(new LinearLayoutManager(context));

    }

//public void fill_list_chipment() {
//
//        //   dataManger.SendVollyRequestJsonArrayGet(this, Request.Method.GET, ApiCall.UrlListOfChipment, new IDataValue() {
//
//     //   ipadrass = getIntent().getStringExtra("ipadrass");
//
//////running but not data added//////////
//        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
//        Date date = new Date(System.currentTimeMillis());
//     //   String sssss = "http://"+ipadrass+ApiCall.UrlListOfChipment+String.valueOf(formatter.format(date));
//       // String ddd="http://10.5.1.6:9090/api/Export_CheckRequest?User_Id="+
//    ///////////////////////testing ////////////////////////////
//           String url=ipadrass+ApiCall.UrlListOfChipment+"User_Id="+EmpId+"&Check_Date="+String.valueOf(formatter.format(date));
//           dataManger.SendVollyRequestJsonArrayGet(this, Request.Method.GET, url, new IDataValue() {
//           ////////////////////////////////////////////////
//    //  dataManger.SendVollyRequestJsonArrayGet(this, Request.Method.GET, sssss, new IDataValue() {
//        @Override
//            public void Success(Object response) throws JSONException {
//                String Result = response.toString();
//                gson = new Gson();
//                exportCheckRequestList[0] = Arrays.asList(gson.fromJson(Result, ExportCheckRequest[].class));
//
//            AdapterCheckRequest adapterCheckRequest = new AdapterCheckRequest(exportCheckRequestList[0], context, new ClickCustomCheckRequest() {
//                @Override
//                public void CheckRequest_click(View view, ExportCheckRequest exportCheckRequest) {
//                    Toast.makeText(context, "" + exportCheckRequest.getCommittee_ID(), Toast.LENGTH_SHORT).show();
//                   // Intent i = new Intent(context, MainActivity_DetailsListOfChimpments.class);
////                            i.putExtra("ipadrass", ipadrass);
////                            i.putExtra("num_Request", String.valueOf(exportCheckRequest.getCheckRequest_Number()));
////                            i.putExtra("Committee_ID",String.valueOf(exportCheckRequest.getCommittee_ID()));
//                    prefsEditor = sharedPreferences.edit();
//                    prefsEditor.putString("num_Request", exportCheckRequest.getCheckRequest_Number());        // Saving integer
//                    prefsEditor.putString("checkRequest_Id", exportCheckRequest.getCheckRequest_Id());
//                    prefsEditor.putLong("Committee_ID", Long.valueOf(exportCheckRequest.getCommittee_ID()));
//                    prefsEditor.putInt("RequestCommittee_Status_Id", exportCheckRequest.getRequestCommittee_Status_Id());
//                    prefsEditor.putString("BarCode", exportCheckRequest.getBarCode());
//                    prefsEditor.putInt("Committee_Type_Id",exportCheckRequest.getCommittee_Type_Id());
//                    prefsEditor.putInt("request_data",1);
//                    if (location.getLongitude()!=0 &&location.getLatitude()!=0 )
//                    {
//                        prefsEditor.putLong("Latitude", (long) location.getLatitude());
//                        prefsEditor.putLong("Longitude", (long) location.getLongitude());
//                        Toast.makeText(MainActivity_Listofchipment.this,""+location.getLatitude()+location.getLongitude(), Toast.LENGTH_LONG).show();
//                    }
//                    ArrayList<Emp_Committe> emp_committeArrayList = new ArrayList<>();
//                    HashMap<String,Integer>hashMap=new HashMap<>();
//                    try {
//                        emp_committeArrayList.addAll(exportCheckRequest.Handle_Emp_Committe());
//                        for (int j = 0; j < emp_committeArrayList.size(); j++) {
//                            if (emp_committeArrayList.get(j).getISAdmin().equals((short)1) && emp_committeArrayList.get(j).getEmployee_Id().equals( EmpId)) {
//                                prefsEditor.putBoolean("ISAdmin", true);
//                            } else if(emp_committeArrayList.get(j).getISAdmin().equals((short)0) && emp_committeArrayList.get(j).getEmployee_Id().equals( EmpId)){
//                                prefsEditor.putBoolean("ISAdmin",false );
//                            }
//                        }
//                        hashMap.putAll(exportCheckRequest.Handel_Request_Treatment());
//                        int treatment_data=hashMap.get("treatment_data").intValue();
//                        int sample_data=hashMap.get("sample_data").intValue();
//                        int request_data=hashMap.get("request_data").intValue();
//                        if(treatment_data==-1){
//                            treatment_data=0;
//                        }
//                        if(sample_data==-1){
//                            sample_data=0;
//                        }
//                        if(request_data==-1) {
//                            request_data=0;
//                        }
////                                ////////////////////
////                        prefsEditor.putInt("treatment_data",hashMap.get("treatment_data").intValue());
////                        prefsEditor.putInt("sample_data",hashMap.get("sample_data").intValue());
////                        prefsEditor.putInt("request_data",hashMap.get("request_data").intValue());
////                        prefsEditor.putInt("totalprocess",hashMap.get("request_data").intValue()+hashMap.get("treatment_data").intValue()+hashMap.get("sample_data").intValue());
////                        ////////////////
//                        prefsEditor.putInt("treatment_data",hashMap.get("treatment_data").intValue());
//                        prefsEditor.putInt("sample_data",hashMap.get("sample_data").intValue());
//                        prefsEditor.putInt("request_data",hashMap.get("request_data").intValue());
//                        prefsEditor.putInt("totalprocess",treatment_data+sample_data+request_data);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    prefsEditor.apply();
//                  //  prefsEditor.commit();
//                    Intent i = new Intent(context, MainActivity_DetailsListOfChimpments.class);
//                    startActivity(i);
//                }
//            });
//                    activityMainListofchipmentBinding.contentListofchipment.setAdapter(adapterCheckRequest);
//                    activityMainListofchipmentBinding.contentListofchipment.recycler.setLayoutManager(new LinearLayoutManager(context));
//            }
//
//            @Override
//            public void Error(VolleyError error) {
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            try {
                public_function.logout(context, ipadrass, sharedPreferences.getString("Token", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    public void AlertDialog() {
//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.alertdialog);
//        Button dialogButton = dialog.findViewById(R.id.btnalert);
//      //  TextView dialogtext=dialog.findViewById(R.id.detailspopup);
//        // if button is clicked, close the custom dialog
//        dialogButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.getWindow().setLayout(1200 , 635);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//    }


//    public void alert(View view) {
//
//    }
//});


//        AlertDialog.Builder alert_dialog =new AlertDialog.Builder(this,R.style.MyAlertDialogStyles);
//        alert_dialog.setMessage("برجاء اختيار الشحنة اولا")
//                .setIcon(image)
//                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setTitle("ddd")
//                .create();
//        alert_dialog.show();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main_activity__listofchipment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        try {
            public_function.NavMenuClick(id, context, sharedPreferences.getString("Token", "")
                    , sharedPreferences.getBoolean("ISAdmin", false)
                    , sharedPreferences.getInt("RequestCommittee_Status_Id", 0),
                    sharedPreferences.getInt("treatment_data", -1),
                    sharedPreferences.getInt("sample_data", -1),
                    sharedPreferences.getInt("request_data", -1),
                    sharedPreferences.getInt("Committee_Type_Id", 0), ipadrass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (id == R.id.language) {
//            // Handle the camera action
//        } else if (id == R.id.sample_title) {
////            Intent i=new Intent(context,MainActivity_SampleWithDraw.class);
////            startActivity(i);
//            public_function.AlertDialog("رجاء اختيار الشحنة",context);
//        } else if (id == R.id.treatment_title) {
////            Intent i=new Intent(context,MainActivity_TreatmentStatement.class);
////            startActivity(i);
//
//            public_function.AlertDialog("رجاء اختيار الشحنة",context);
//        } else if (id == R.id.Committee_title) {
////            Intent i=new Intent(context,MainActivity_Ex_RequestCommitteeResult.class);
////            startActivity(i);
//
//            public_function.AlertDialog("رجاء اختيار الشحنة",context);
//        }else if (id == R.id.todolist) {
//
//
//            fill_list_chipment();
//
//        }
//        else if (id == R.id.logout) {
//
//            Intent i=new Intent(context,LogIn.class);
//            startActivity(i);
//
//
//        } else if (id == R.id.nav_send) {
//
//        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shownav(View view) {
        drawer.openDrawer(GravityCompat.START);
    }

    public void onRadioclicks(View view) {
        checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {

            case R.id.ExportCheck:
                if (checked)
                    filladapter(EX_exportCheckRequestList);
                break;
            case R.id.ImportCheck:
                if (checked)
                    filladapter(IM_exportCheckRequestList);
                break;
            case R.id.ExtraCheck:
                if (checked)
                    filladapter(EXTRA_exportCheckRequestList);
                break;
            default:
                get_list_chipment();
                break;

        }
    }
//    public void onRadioclicks(View view) {
//        checked = ((RadioButton) view).isChecked();
//
//        switch (view.getId()) {
//
//            case R.id.ExportCheck:
//
//                    if (checked)
//
//                        adapterCheckRequest = new AdapterCheckRequest(totalexportCheckRequestList[0], context, new ClickCustomCheckRequest() {
//                            @Override
//                            public void CheckRequest_click(View view, ExportCheckRequest exportCheckRequest) {
//                                Toast.makeText(context, "" + exportCheckRequest.getCommittee_ID(), Toast.LENGTH_SHORT).show();
//                                prefsEditor = sharedPreferences.edit();
//                                prefsEditor.putString("num_Request", exportCheckRequest.getCheckRequest_Number());        // Saving integer
//                                prefsEditor.putString("checkRequest_Id", exportCheckRequest.getCheckRequest_Id());
//                                prefsEditor.putLong("Committee_ID", Long.valueOf(exportCheckRequest.getCommittee_ID()));
//                                prefsEditor.putInt("RequestCommittee_Status_Id", exportCheckRequest.getRequestCommittee_Status_Id());
//                                prefsEditor.putString("BarCode", exportCheckRequest.getBarCode());
//                                prefsEditor.putInt("Committee_Type_Id", exportCheckRequest.getCommittee_Type_Id());
//                                prefsEditor.putInt("request_data", 1);
//                                if (location != null) {
//                                    prefsEditor.putLong("Latitude", (long) location.getLatitude());
//                                    prefsEditor.putLong("Longitude", (long) location.getLongitude());
//                                }
//                                ArrayList<Emp_Committe> emp_committeArrayList = new ArrayList<>();
//                                HashMap<String, Integer> hashMap = new HashMap<>();
//                                try {
//                                    emp_committeArrayList.addAll(exportCheckRequest.Handle_Emp_Committe());
//                                    for (int j = 0; j < emp_committeArrayList.size(); j++) {
//                                        if (emp_committeArrayList.get(j).getISAdmin().equals((short) 1) && emp_committeArrayList.get(j).getEmployee_Id().equals(EmpId)) {
//                                            prefsEditor.putBoolean("ISAdmin", true);
//                                        } else if (emp_committeArrayList.get(j).getISAdmin().equals((short) 0) && emp_committeArrayList.get(j).getEmployee_Id().equals(EmpId)) {
//                                            prefsEditor.putBoolean("ISAdmin", false);
//                                        }
//                                    }
//                                    hashMap.putAll(exportCheckRequest.Handel_Request_Treatment());
//                                    int treatment_data = hashMap.get("treatment_data").intValue();
//                                    int sample_data = hashMap.get("sample_data").intValue();
//                                    int request_data = hashMap.get("request_data").intValue();
//                                    if (treatment_data == -1) {
//                                        treatment_data = 0;
//                                    }
//                                    if (sample_data == -1) {
//                                        sample_data = 0;
//                                    }
//                                    if (request_data == -1) {
//                                        request_data = 0;
//                                    }
////                                ////////////////////
////                        prefsEditor.putInt("treatment_data",hashMap.get("treatment_data").intValue());
////                        prefsEditor.putInt("sample_data",hashMap.get("sample_data").intValue());
////                        prefsEditor.putInt("request_data",hashMap.get("request_data").intValue());
////                        prefsEditor.putInt("totalprocess",hashMap.get("request_data").intValue()+hashMap.get("treatment_data").intValue()+hashMap.get("sample_data").intValue());
////                        ////////////////
//                                    prefsEditor.putInt("treatment_data", hashMap.get("treatment_data").intValue());
//                                    prefsEditor.putInt("sample_data", hashMap.get("sample_data").intValue());
//                                    prefsEditor.putInt("request_data", hashMap.get("request_data").intValue());
//                                    prefsEditor.putInt("totalprocess", treatment_data + sample_data + request_data);
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                prefsEditor.apply();
//                                //  prefsEditor.commit();
//                                Intent i = new Intent(context, MainActivity_DetailsListOfChimpments.class);
//                                startActivity(i);
//
//                            }
//                        });
//                                  activityMainListofchipmentBinding.contentListofchipment.setAdapter(adapterCheckRequest);
//                                activityMainListofchipmentBinding.contentListofchipment.recycler.setLayoutManager(new LinearLayoutManager(context));
//
//
//                break;
//            case R.id.ImportCheck:
//                if (checked)
//
//                break;
//
//            default:
//
//                break;
//
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}
