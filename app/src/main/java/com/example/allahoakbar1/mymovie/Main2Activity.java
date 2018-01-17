package com.example.allahoakbar1.mymovie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //image
        final Intent intent = getIntent();
        String message = intent.getStringExtra("posterimage");
        final String message2 = intent.getStringExtra("posterimage");
        final ImageView imageView = (ImageView) findViewById(R.id.poster_image);
        Picasso.with(getApplicationContext()).load(message).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //name
        TextView textView = (TextView) findViewById(R.id.name);
        message = intent.getStringExtra("name");
        textView.setText(message);
        //original_language
        TextView textView3 = (TextView) findViewById(R.id.language);
        message = intent.getStringExtra("original_language");
        textView3.setText(message);
        //relasedate
        TextView textView1 = (TextView) findViewById(R.id.relase_date);
        message = intent.getStringExtra("relasedate");
        textView1.setText(message);
        //rating bar >>voteaverage
        message = intent.getStringExtra("voteaverage");
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        ratingBar.setRating(Float.parseFloat(message) / 2);
        //overview
        TextView textView2 = (TextView) findViewById(R.id.over_view);
        message = intent.getStringExtra("overview");
        textView2.setText(message);
        //--------------------
        final ProgressDialog pDialog = new ProgressDialog(Main2Activity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        message = intent.getStringExtra("trailer");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, message, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    final String[] links = new String[results.length()];
                    String[] lable = new String[results.length()];
                    String youtubelink = "http://www.youtube.com/watch?v=";
                    int i = 0;
                    for (int x = 0; x < results.length(); x++) {
                        links[x] = youtubelink + results.getJSONObject(x).getString("key");
                        i++;
                        lable[x] = "Trailer " + i;
                    }
                    ListView listView = (ListView) findViewById(R.id.list_trailers);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.layout_test_trailers, lable);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(links[position])));
                        }
                    });
                } catch (Exception e) {
                }
                pDialog.hide();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        //singleton
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjReq);


    }
}
