package com.mediflux.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnboardingPagerAdapter extends RecyclerView.Adapter<OnboardingPagerAdapter.PageViewHolder> {

    private static final int[] LAYOUTS = {
        R.layout.onboarding_page1,
        R.layout.onboarding_page2,
        R.layout.onboarding_page3
    };

    private final LayoutInflater inflater;

    public OnboardingPagerAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(LAYOUTS[viewType], parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        // Static layouts — nothing to bind dynamically
    }

    @Override
    public int getItemCount() {
        return LAYOUTS.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        PageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
