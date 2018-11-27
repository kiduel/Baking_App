package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity {
    private int stepNumber;
    private ArrayList<Step> steps;
    private Step step;

    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);



        if(savedInstanceState == null) {

            Intent intent = getIntent();
            if (intent != null) {
                stepNumber = intent.getIntExtra("stepNumber", 0);
                steps = intent.getParcelableArrayListExtra("stepsArrayList");
                step = intent.getParcelableExtra("singleStep");

                title.setText(step.getShortDescription());


                VideoFragment videoFragment = new VideoFragment();
                Bundle bundle = new Bundle();
                //bundle.putString("video", getIntent().getStringExtra("video") );
                //bundle.putString("description", getIntent().getStringExtra("video") );
                bundle.putInt("stepNumber", stepNumber);
                bundle.putParcelableArrayList("stepsArrayList", steps);
                //bundle.putParcelable("singleStep", getIntent().getParcelableExtra("singleStep") );
                videoFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction().replace(R.id.video_fragment_container, videoFragment).commit();
            }


        }else {
            stepNumber = savedInstanceState.getInt("stepNumber");
            steps = savedInstanceState.getParcelableArrayList("stepsArrayList");
        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("stepNumber", stepNumber);
        outState.putParcelableArrayList("stepsArrayList", steps);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}