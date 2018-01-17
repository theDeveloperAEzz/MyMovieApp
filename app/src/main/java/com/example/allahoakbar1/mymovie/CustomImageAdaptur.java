package com.example.allahoakbar1.mymovie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomImageAdaptur extends ArrayAdapter<MovieClass> {
    Context context;
    int resource;
    ArrayList<MovieClass> gridobjects;



    public CustomImageAdaptur(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MovieClass> gridobjects) {
        super(context, R.layout.layout1, gridobjects);
        this.context = context;
        this.resource = resource;
        this.gridobjects = gridobjects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout1, null);
            imageView = (ImageView) convertView.findViewById(R.id.imageitemgrid);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }
//chick online and if connected download the image with picasso
        if (isOnline()){
            Picasso.with(getContext()).load(gridobjects.get(position).getPoster_path()).into(imageView);
//else get it from image_data variable whose Store it in database as base64
        }else {

            // convert from base64 to bitmap made by eng  :D :D
            Bitmap myBitmapAgain = decodeBase64(gridobjects.get(position).getIMAGE_DATA());
            imageView.setImageBitmap(myBitmapAgain);
        }

        return convertView;
    }
    //convert from String to base64
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    //chick internet methoud
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}


