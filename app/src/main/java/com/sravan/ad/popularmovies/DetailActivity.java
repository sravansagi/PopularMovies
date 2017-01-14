package com.sravan.ad.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.activity_detail,new DetailFragment()).
                commit();

    }
}
