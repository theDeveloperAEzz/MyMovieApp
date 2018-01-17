package com.example.allahoakbar1.mymovie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allahoakbar1.mymovie.DB.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<MovieClass> movieClasses;
    String path;
    ProgressDialog pDialog;
    GridView gridView;
    String url;
    DBHelper db;
    CustomImageAdaptur customImageAdaptur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieClasses = new ArrayList<MovieClass>();
//path whose sub with poster_path to get the image
        path = "http://image.tmdb.org/t/p/w185/";
        pDialog = new ProgressDialog(MainActivity.this);
        gridView = (GridView) findViewById(R.id.moviegrid);
//crete db ....
        db = new DBHelper(getApplicationContext());
        customImageAdaptur = new CustomImageAdaptur(getApplicationContext(), R.layout.layout1, movieClasses);
        gridView.setAdapter(customImageAdaptur);
        // gridView.setPadding(2, 2, 2, 2);
        pDialog.setMessage("Loading...");
//the json url
        url = "https://api.themoviedb.org/3/movie/popular?api_key=8d2ebaf0d42cc0e7ab1592dc777a3a62";
//if online goto json request , clear database , download data from json and inserting it in database
        if (isOnline()) {
            Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_LONG).show();
            //don't duplicate when not connected
            db.clearTable();
            pDialog.show();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int x = 0; x < results.length(); x++) {
                            MovieClass movieClass = new MovieClass();
                            movieClass.setPoster_path(path + results.getJSONObject(x).getString("poster_path"));
                            movieClass.setTitle(results.getJSONObject(x).getString("title"));
                            movieClass.setRelease_date(results.getJSONObject(x).getString("release_date"));
                            movieClass.setOverview(results.getJSONObject(x).getString("overview"));
                            movieClass.setVote_average(results.getJSONObject(x).getString("vote_average"));
                            movieClass.setOriginal_language(results.getJSONObject(x).getString("original_language"));
                            movieClass.setId(results.getJSONObject(x).getString("id"));
                            movieClasses.add(movieClass);
                            //insert data in data base (go to getimageData methoud)
                            // download and convert poster_Path's url to bitmap made by eng : :D
                            getimageData(movieClass);
                            //tell the adapter that movieClasses arraylist' data  is changed
                            //and give adapter the new data
                            customImageAdaptur.notifyDataSetChanged();


                        }
                        pDialog.hide();
                    } catch (Exception e) {
                    }

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


            final String urltrailer1 = "http://api.themoviedb.org/3/movie/";
            final String urltrailer2 = "/videos?api_key=8d2ebaf0d42cc0e7ab1592dc777a3a62";
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()

            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "i'm :" + position + "", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra("posterimage", movieClasses.get(position).getPoster_path());
                    intent.putExtra("name", movieClasses.get(position).getTitle());
                    intent.putExtra("relasedate", movieClasses.get(position).getRelease_date());
                    intent.putExtra("overview", movieClasses.get(position).getOverview());
                    intent.putExtra("voteaverage", movieClasses.get(position).getVote_average());
                    intent.putExtra("original_language", movieClasses.get(position).getOriginal_language());
                    String trailer = urltrailer1 + movieClasses.get(position).getId() + urltrailer2;
                    intent.putExtra("trailer", (trailer));
                    startActivity(intent);

                }
            });

        } else {//if not connected to internet get saved data from DB
            final String urltrailer1 = "http://api.themoviedb.org/3/movie/";
            final String urltrailer2 = "/videos?api_key=8d2ebaf0d42cc0e7ab1592dc777a3a62";
            Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
            movieClasses = db.Select();
            customImageAdaptur = new CustomImageAdaptur(getApplicationContext(), R.layout.layout1, movieClasses);
            gridView.setAdapter(customImageAdaptur);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "you aren't connect to the internet ", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                    intent.putExtra("posterimage", movieClasses.get(position).getPoster_path());
//                    intent.putExtra("name", movieClasses.get(position).getTitle());
//                    intent.putExtra("relasedate", movieClasses.get(position).getRelease_date());
//                    intent.putExtra("overview", movieClasses.get(position).getOverview());
//                    intent.putExtra("voteaverage", movieClasses.get(position).getVote_average());
//                    intent.putExtra("original_language", movieClasses.get(position).getOriginal_language());
//                    String trailer = urltrailer1 + movieClasses.get(position).getId() + urltrailer2;
//                    intent.putExtra("trailer", (trailer));
//                    startActivity(intent);
                }
            });

        }


    }

    //chick online methoud
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    // public void checkConnection() {
    //   if (isOnline()) {
    //     Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_LONG).show();
    // } else {
    //     Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
    // }
//    }

    // download and convert poster_Path's url to bitmap made by eng :  :D
    public void getimageData(final MovieClass movieClass) {
        ImageRequest imageRequest = new ImageRequest(
                movieClass.getPoster_path(), // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // convert from url's poster_path to base64 for save it in SQLite
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        response.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        movieClass.setIMAGE_DATA(encoded);
                        //the solution is delete all contacts if connected
                        db.Insert(movieClass);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        error.printStackTrace();

                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        Volley.newRequestQueue(getApplicationContext()).add(imageRequest);
    }

}