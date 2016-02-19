package com.swapnilborkar.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        if (intent != null) {
            if (getSupportActionBar() != null) {

                getSupportActionBar().setDisplayShowTitleEnabled(true);
                setTitle(intent.getStringExtra("title"));
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        FloatingActionButton fab = (FloatingActionBu
// tton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Loading Image and Applying Pal
        String baseUrl = "http://image.tmdb.org/t/p/w780/";
        int w = getResources().getDisplayMetrics().widthPixels / 2; //dividing by numColumns
        int h = (int) (w * 1.5); //adjusting height to 1.5x times the width

        final ImageView moviePoster = (ImageView) findViewById(R.id.img_poster);
        Picasso.with(this)
                .load(baseUrl + intent.getStringExtra("url"))
                .resize(w, h)
                .centerCrop()
                .transform(PaletteTransformation.instance())
                .into(moviePoster, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) moviePoster.getDrawable()).getBitmap();
                        Palette palette = PaletteTransformation.getPalette(bitmap);

                        //Apply palette to views here:
                        int defaultColor = getResources().getColor(R.color.colorPrimary);
                        int lightVibrantColor = palette.getLightVibrantColor(defaultColor);
                        if (((getSupportActionBar() != null))) {
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(lightVibrantColor));
                        }

                        if (lightVibrantColor != defaultColor) {
                            float[] hsv = new float[3];
                            Color.colorToHSV(lightVibrantColor, hsv);
                            hsv[2] *= 0.8f;
                            int colorPrimaryDark = Color.HSVToColor(hsv);

                            if (Build.VERSION.SDK_INT >= 21)
                                getWindow().setStatusBarColor(colorPrimaryDark);
                        }

                        if (lightVibrantColor == defaultColor) {
                            int alternateColor = palette.getLightMutedColor(defaultColor);
                            if (getSupportActionBar() != null) {
                                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(alternateColor));
                                float[] hsv = new float[3];
                                Color.colorToHSV(alternateColor, hsv);
                                hsv[2] *= 0.8f;
                                int colorPrimaryDark = Color.HSVToColor(hsv);

                                if (Build.VERSION.SDK_INT >= 21)
                                    getWindow().setStatusBarColor(colorPrimaryDark);
                            }

                        }

                    }
                });


    }

}
