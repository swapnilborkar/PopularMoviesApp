package com.swapnilborkar.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_favorite_movies);
//

        final Intent intent = getIntent();


//        if (intent!= null) {
//            //Get EXTRA from intent and attach to Fragment as Argument
//            String text = getIntent().getStringExtra("url");
//            Bundle args = new Bundle();
//            args.putString("ARGUMENTS", text);
//            MovieActivityFragment detailFragment = new MovieActivityFragment();
//            detailFragment.setArguments(args);
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, detailFragment).commit();
//        }

        if (intent != null) {
            if (getSupportActionBar() != null) {

                getSupportActionBar().setDisplayShowTitleEnabled(true);
                String title = intent.getStringExtra("title");
                setTitle(title);
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Loading Image and Applying Pal
        String baseUrl = "http://image.tmdb.org/t/p/w500/";

        int w;
        int h;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            w = getResources().getDisplayMetrics().widthPixels / 4; //dividing by numColumns
            h = (int) (w * 1.5); //adjusting height to 1.5x times the width
        } else {
            w = getResources().getDisplayMetrics().widthPixels / 2; //dividing by numColumns
            h = (int) (w * 1.5); //adjusting height to 1.5x times the width
        }


        final ImageView moviePoster = (ImageView) findViewById(R.id.img_poster2);

        assert intent != null;
        String posterURL = intent.getStringExtra("url");


        assert moviePoster != null;
        Picasso.with(this)
                .load(baseUrl + posterURL)
                .resize(w, h)
                .centerCrop()
                .error(R.drawable.icon_cry)
                .placeholder(R.drawable.placeholder)
                .transform(PaletteTransformation.instance())
                .into(moviePoster, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) moviePoster.getDrawable()).getBitmap();
                        Palette palette = PaletteTransformation.getPalette(bitmap);


                        //Apply palette to views here:
                        int defaultColor = getColor(MovieActivity.this, R.color.colorPrimary);
                        int colorAccent = getColor(MovieActivity.this, R.color.colorAccent);
                        int lightVibrantColor = palette.getLightVibrantColor(defaultColor);
                        //int vibrantColor = palette.getVibrantColor(colorAccent);


                        if (getSupportActionBar() != null) {
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

                        //fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
                        int white = getColor(MovieActivity.this, R.color.white);
                        assert fab != null;
                        fab.setBackgroundTintList(ColorStateList.valueOf(white));
                        fab.show();

                    }
                });

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = intent.getStringExtra("title");
                String addFavorite = (String) getResources().getText(R.string.added_to_favorites);
                Snackbar snack = Snackbar.make(v, title + " " + addFavorite, Snackbar.LENGTH_LONG);
                View snackView = snack.getView();
                TextView textView = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(getColor(MovieActivity.this, R.color.black));
                ViewGroup group = (ViewGroup) snack.getView();
                group.setBackgroundColor(ContextCompat.getColor(MovieActivity.this, R.color.white));
                snack.show();
            }
        });

    }



}
