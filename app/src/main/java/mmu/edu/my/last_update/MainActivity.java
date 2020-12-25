package mmu.edu.my.last_update;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tv_totalnumber1, tv_activenumber1, tv_deathnumber1, tv_recoverednumber1, tv_todaynumber1;
    private String str_total, str_active, str_death, str_recovered, str_today;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int int_today;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise
        Init();
        //Fetch data from API
        FetchData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchData();
                swipeRefreshLayout.setRefreshing(false);
                //Toast.makeText(MainActivity.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
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
                            JSONObject result = new JSONObject(apiUrl);
                            str_total = result.getString("testedPositive");
                            str_active = result.getString("activeCases");
                            str_death = result.getString("deceased");
                            str_recovered = result.getString("recovered");

                            Handler delayToShowProgess = new Handler();
                            delayToShowProgess.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Setting text in the textview
                                    tv_totalnumber1.setText(NumberFormat.getInstance().format(Integer.parseInt(str_total)));
                                    tv_activenumber1.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));
                                    tv_recoverednumber1.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
                                    tv_deathnumber1.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
                                    int_today = Integer.parseInt(str_total)
                                            - (Integer.parseInt(str_total) + Integer.parseInt(str_death));
                                    tv_todaynumber1.setText(NumberFormat.getInstance().format(int_today));
                                DismissDialog();
                                }
                            }, 1000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void DismissDialog() {
        progressDialog.dismiss();
    }
    private void Init() {
        tv_totalnumber1 = findViewById(R.id.totalnaumber1);
        tv_activenumber1 = findViewById(R.id.activenumber1);
        tv_deathnumber1= findViewById(R.id.deathnumber1);
        tv_recoverednumber1= findViewById(R.id.recovernumber1);
        tv_todaynumber1 = findViewById(R.id.todaynumber1);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }
}

