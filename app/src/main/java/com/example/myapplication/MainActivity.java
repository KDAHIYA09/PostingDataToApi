package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // creating variables for our edittext,
    // button, textview and progressbar.
    private EditText nameEdt, jobEdt;
    private Button postDataBtn;
    private TextView responseTV;
    private ProgressBar loadingPB;
    private String p1 = "14";
    private String p2 = "1690603292851";
    private String p4 = "23A Shivaji Marg, Karampura Industrial Area, Karam Pura, Delhi, 110015";
    private String p3 = "28.661130,77.148360";
    private String p5 = "15-07-2023 12:12:54";
    private String p6 = "attendance test";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        postDataBtn = findViewById(R.id.idBtnPost);
        responseTV = findViewById(R.id.idTVResponse);
        loadingPB = findViewById(R.id.idLoadingPB);

        getSupportActionBar().hide();

        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postData(p1, p2, p3, p4, p5, p6);
            }
        });
    }

    private void postData(String id, String created_at, String latlng, String location, String actDate, String remark) {

        // below line is for displaying our progress bar.
        loadingPB.setVisibility(View.VISIBLE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://x8ki-letl-twmt.n7.xano.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);


        DataModal modal = new DataModal(id, created_at, latlng, location, actDate, remark);

        Call<DataModal> call = retrofitAPI.createPost(modal);


        call.enqueue(new Callback<DataModal>() {
            @Override
            public void onResponse(Call<DataModal> call, Response<DataModal> response) {

                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();


                loadingPB.setVisibility(View.GONE);

                DataModal responseFromAPI = response.body();

                String responseString = "Response Code : " + response.code() + "\nid : " + responseFromAPI.getId() + "\n" + "created_at : " + responseFromAPI.getCreated_at()
                                               + "latitude & longitude : " + responseFromAPI.getLatlng() + "location : " + responseFromAPI.getLocation()
                                                + "Date & Time : " + responseFromAPI.getActDate() + "remarks : " + responseFromAPI.getRemark();

                responseTV.setText(responseString);
            }

            @Override
            public void onFailure(Call<DataModal> call, Throwable t) {
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }
}
