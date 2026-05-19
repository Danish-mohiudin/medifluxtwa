package com.mediflux.app;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnboardingPagerAdapter extends RecyclerView.Adapter<OnboardingPagerAdapter.PageViewHolder> {

    private static final int[] LAYOUTS = {
        R.layout.onboarding_page1,
        R.layout.onboarding_page2,
        R.layout.onboarding_page3
    };

    private final LayoutInflater inflater;
    private final Runnable onNext;
    private final Runnable onSkip;

    // Keep references to controls in each inflated page
    private final PageViewHolder[] holders = new PageViewHolder[3];

    public OnboardingPagerAdapter(Context context, Runnable onNext, Runnable onSkip) {
        this.inflater = LayoutInflater.from(context);
        this.onNext = onNext;
        this.onSkip = onSkip;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(LAYOUTS[viewType], parent, false);
        PageViewHolder holder = new PageViewHolder(view, viewType);
        holders[viewType] = holder;

        holder.btnNext.setOnClickListener(v -> onNext.run());
        if (holder.btnSkip != null) {
            holder.btnSkip.setOnClickListener(v -> onSkip.run());
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        updateDots(holder, position);
        // Last page: rename button, hide skip
        if (position == 2) {
            holder.btnNext.setText("Get Started");
            if (holder.btnSkip != null) holder.btnSkip.setVisibility(View.GONE);
        } else {
            holder.btnNext.setText("Next");
            if (holder.btnSkip != null) holder.btnSkip.setVisibility(View.VISIBLE);
        }
    }

    /** Called by Activity when the page changes — updates dots + button label on the visible page */
    public void updatePage(int position) {
        PageViewHolder h = holders[position];
        if (h == null) return;
        updateDots(h, position);
        if (position == 2) {
            h.btnNext.setText("Get Started");
            if (h.btnSkip != null) h.btnSkip.setVisibility(View.GONE);
        } else {
            h.btnNext.setText("Next");
            if (h.btnSkip != null) h.btnSkip.setVisibility(View.VISIBLE);
        }
    }

    private void updateDots(PageViewHolder h, int activePage) {
        int activePx = dp(h.itemView.getContext(), 24);
        int inactivePx = dp(h.itemView.getContext(), 8);
        for (int i = 0; i < h.dots.length; i++) {
            h.dots[i].setImageResource(i == activePage ? R.drawable.dot_active : R.drawable.dot_inactive);
            ViewGroup.LayoutParams lp = h.dots[i].getLayoutParams();
            lp.width = (i == activePage) ? activePx : inactivePx;
            h.dots[i].setLayoutParams(lp);
        }
    }

    private int dp(Context ctx, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                ctx.getResources().getDisplayMetrics());
    }

    @Override
    public int getItemCount() { return LAYOUTS.length; }

    @Override
    public int getItemViewType(int position) { return position; }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        Button btnNext;
        TextView btnSkip;   // null on page 3
        ImageView[] dots;

        PageViewHolder(@NonNull View itemView, int pageIndex) {
            super(itemView);
            btnNext = itemView.findViewById(R.id.btnNext);
            btnSkip = itemView.findViewById(R.id.btnSkip);
            dots = new ImageView[]{
                itemView.findViewById(R.id.dot0),
                itemView.findViewById(R.id.dot1),
                itemView.findViewById(R.id.dot2)
            };
        }
    }
}
