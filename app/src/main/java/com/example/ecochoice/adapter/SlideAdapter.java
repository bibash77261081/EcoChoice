package com.example.ecochoice.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecochoice.R;
import com.example.ecochoice.model.SlideItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.ViewHolder>{

    private List<SlideItem> slideItems;
    private ViewPager2 viewPager2;
    private Timer timer;
    private Handler handler;
    private Runnable runnable;
    private int currentPosition = 0;

    public SlideAdapter(List<SlideItem> slideItems, ViewPager2 viewPager2) {
        this.slideItems = slideItems;
        this.viewPager2 = viewPager2;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPosition == slideItems.size()) {
                    currentPosition = 0;
                }
                viewPager2.setCurrentItem(currentPosition++, true);
            }
        };

        startAutoSlide();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideAdapter.ViewHolder holder, int position) {
        SlideItem slideItem = slideItems.get(position);
        holder.bind(slideItem);
    }

    @Override
    public int getItemCount() {
        return slideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);;
        }

        void bind (SlideItem slideItem){
            imageView.setImageResource(slideItem.getImage());
        }
    }

    private void startAutoSlide() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 3000, 3000); // Delay in milliseconds between slides (3 seconds)
    }

    //stop the auto slide to save memory when needed
    public void stopAutoSlide() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
