package com.swapnilborkar.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {


    private static String sortMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);


            MainFragment fragment = new MainFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame_main, fragment);
            transaction.commit();

        }
    }

    public void setSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movie_sort, R.layout.toolbar_spinner_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assert spinner != null;
        spinner.setAdapter(adapter);

        //Spinner on Start
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                SharedPreferences.Editor editor = getPreferences(0).edit();
                int selectedPosition;

                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_main);

                switch (position) {

                    case 0:

                        assert spinner != null;
                        selectedPosition = spinner.getSelectedItemPosition();
                        editor.putInt("spinnerSelection", selectedPosition);
                        editor.apply();
                        sortMode = "popular";
                        fragment.getSortMode(sortMode);
                        fragment.updateMovies();

                        break;

                    case 1:

                        assert spinner != null;
                        selectedPosition = spinner.getSelectedItemPosition();
                        editor.putInt("spinnerSelection", selectedPosition);
                        editor.apply();
                        sortMode = "top_rated";
                        fragment.getSortMode(sortMode);
                        fragment.updateMovies();
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        }


    //
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


}







