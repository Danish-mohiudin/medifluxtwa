package com.mediflux.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class OnboardingActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "mediflux_prefs";
    public static final String KEY_ONBOARDING_DONE = "onboarding_done";

    private ViewPager2 viewPager;
    private Button btnNext;
    private TextView btnSkip;
    private ImageView[] dots;
    private static final int TOTAL_PAGES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
        dots = new ImageView[]{
            findViewById(R.id.dot0),
            findViewById(R.id.dot1),
            findViewById(R.id.dot2)
        };

        viewPager.setAdapter(new OnboardingPagerAdapter(this));
        viewPager.setOffscreenPageLimit(3);

        updateDots(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDots(position);
                if (position == TOTAL_PAGES - 1) {
                    btnNext.setText("Get Started");
                    btnSkip.setVisibility(View.INVISIBLE);
                } else {
                    btnNext.setText("Next");
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < TOTAL_PAGES - 1) {
                viewPager.setCurrentItem(current + 1, true);
            } else {
                finishOnboarding();
            }
        });

        btnSkip.setOnClickListener(v -> {
            viewPager.setCurrentItem(TOTAL_PAGES - 1, true);
        });
    }

    private void updateDots(int activeIndex) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setImageResource(
                i == activeIndex ? R.drawable.dot_active : R.drawable.dot_inactive
            );
        }
    }

    private void finishOnboarding() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, true).apply();
        startActivity(new Intent(this, LauncherActivity.class));
        finish();
    }
}
