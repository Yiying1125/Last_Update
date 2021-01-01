package mmu.edu.my.last_update;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tv_totalnumber1, tv_activenumber1, tv_deathnumber1, tv_recoverednumber1, tv_todaynumber1;
    private String str_total, str_active, str_death, str_recovered, str_today, str_tadaycal;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int int_today;
    private ProgressDialog progressDialog;
    private String appUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("TraCo-19 (Latest Update)");
        //initialise
        Init();
        //Fetch data from API
        FetchData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void FetchData() {
        //show progress dialog
        ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.apify.com/v2/key-value-stores/6t65lJVfs3d8s6aKc/records/LATEST?disableRedirect=true";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            str_total = response.getString("testedPositive");
                            str_active = response.getString("activeCases");
                            str_death = response.getString("deceased");
                            str_recovered = response.getString("recovered");

                            Handler delayToShowProgress = new Handler();
                            delayToShowProgress.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Setting text in the textview
                                    tv_totalnumber1.setText(NumberFormat.getInstance().format(Integer.parseInt(str_total)));
                                    tv_activenumber1.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));
                                    tv_recoverednumber1.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                                    tv_deathnumber1.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
                                    /*int_today = Integer.parseInt(str_total)
                                            - (Integer.parseInt(str_total) + Integer.parseInt(str_death));*/
                                    tv_todaynumber1.setText(NumberFormat.getInstance().format(int_today));
                                    dismissDialog();
                                }}, 1000);
                        } catch (JSONException e) { e.printStackTrace(); }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    } }
                        );
        requestQueue.add(jsonObjectRequest);
    }
    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void dismissDialog(){progressDialog.dismiss();}
    private void Init() {
        tv_totalnumber1 = findViewById(R.id.totalnumber2);
        tv_activenumber1 = findViewById(R.id.activenumber2);
        tv_deathnumber1= findViewById(R.id.deathnumber2);
        tv_recoverednumber1= findViewById(R.id.recoverednumber2);
        tv_todaynumber1 = findViewById(R.id.todaynumber2);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

