package com.mediflux.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Transparent activity that immediately fires the system share sheet
 * for the MediFlux Play Store link, then finishes itself.
 * Triggered via the "Share App" launcher shortcut.
 */
public class ShareActivity extends Activity {

    private static final String PLAY_STORE_URL =
            "https://play.google.com/store/apps/details?id=com.mediflux.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out MediFlux");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Manage your pharmacy smarter with MediFlux!\n" + PLAY_STORE_URL);

        startActivity(Intent.createChooser(shareIntent, "Share MediFlux via"));
        finish(); // close immediately after share sheet appears
    }
}
