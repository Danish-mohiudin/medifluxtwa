/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mediflux.app;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.gms.tasks.Task;

public class LauncherActivity
        extends com.google.androidbrowserhelper.trusted.LauncherActivity {

    private static final String TAG = "MediFlux";
    // Delay before showing the review prompt (5 seconds after launch)
    private static final long REVIEW_DELAY_MS = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lock portrait on Android O (API 26) and above.
        // On older versions, transparent background + fixed orientation causes a crash,
        // so we fall back to sensor-based portrait which avoids the issue.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        Log.d(TAG, "LauncherActivity started, SDK=" + Build.VERSION.SDK_INT);

        // Trigger in-app review after a short delay so the app has time to load
        new Handler(Looper.getMainLooper()).postDelayed(this::requestInAppReview, REVIEW_DELAY_MS);
    }

    /**
     * Requests the Play Store in-app review dialog.
     * Google controls whether the dialog actually shows based on review quotas,
     * so it won't appear on every launch.
     */
    private void requestInAppReview() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(flowTask -> {
                    // Flow finished — whether the user rated or dismissed, continue normally
                    Log.d(TAG, "In-app review flow completed");
                });
            } else {
                // Not a critical failure — review just won't show
                Log.w(TAG, "In-app review request failed: " + task.getException());
            }
        });
    }

    @Override
    protected Uri getLaunchingUrl() {
        Uri uri = super.getLaunchingUrl();
        return uri;
    }
}
