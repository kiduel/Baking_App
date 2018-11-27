package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.android.bakingapp.adapters.FragPageAdapter;
import com.example.android.bakingapp.model.Step;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity implements StepsFragment.OnClickStepListener {
    FragPageAdapter fragPageAdapter;
    @BindView(R.id.vpPager)
    @Nullable ViewPager viewPager;
    boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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

        if ( findViewById(R.id.all_item_holder) != null ) {
            viewPager = null;
        } else {
            twoPane = false;

            fragPageAdapter = new FragPageAdapter(getSupportFragmentManager());
            viewPager.setAdapter(fragPageAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void onStepSelected(ArrayList<Step> steps, Step single_step, String videoURL, String description) {

        int stepNumber = steps.indexOf(single_step);
        if ( findViewById(R.id.all_item_holder) != null ) {
            viewPager = null;
            twoPane = true;
            VideoFragment videoFragment = new VideoFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("stepsArrayList", steps);
            bundle.putParcelable("singleStep", single_step);
            bundle.putInt("stepNumber", stepNumber);
            //videoFragment.setVideoURL(videoURL);
            //videoFragment.setDescription(description);
            videoFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.two_pane_video_fragment_container, videoFragment).commit();

        } else {
            twoPane = false;
            Intent intent = new Intent(this, VideoActivity.class);
            //intent.putExtra("stepsArrayList", steps);
            intent.putExtra("singleStep", single_step); //trying to ADD the individual steps as well as teh whole list
            intent.putExtra("stepNumber", stepNumber);
            intent.putParcelableArrayListExtra("stepsArrayList", steps);
            intent.putExtra("video", "goooogle.com");
            intent.putExtra("description", "descccrrrppp");

            startActivity(intent);
        }
    }
}
