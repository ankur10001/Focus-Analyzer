package com.example.focusanalyzer.ui.history;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.focusanalyzer.R;
import com.example.focusanalyzer.data.local.SessionEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private List<SessionEntity> sessions = new ArrayList<>();
    private int lastPosition = -1;

    public void setData(List<SessionEntity> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new SessionDiffCallback(this.sessions, newList));
        this.sessions = new ArrayList<>(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SessionEntity session = sessions.get(position);

        String category = session.getCategory() != null ? session.getCategory() : "General";
        holder.categoryText.setText(category);
        
        // Premium touch: Category-based color coding
        int color;
        switch (category.toLowerCase()) {
            case "work": color = Color.parseColor("#6366F1"); break; // Indigo
            case "study": color = Color.parseColor("#10B981"); break; // Emerald
            case "deep work": color = Color.parseColor("#F59E0B"); break; // Amber
            default: color = Color.parseColor("#3B82F6"); break; // Blue
        }
        holder.categoryIndicator.setBackgroundColor(color);

        long durationMillis = session.getEndTime() - session.getStartTime();
        long minutes = (durationMillis / 1000) / 60;
        long seconds = (durationMillis / 1000) % 60;
        
        if (minutes > 0) {
            holder.timeText.setText(String.format(Locale.getDefault(), "%dm %ds", minutes, seconds));
        } else {
            holder.timeText.setText(seconds + "s");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
        holder.subtitleText.setText(sdf.format(new Date(session.getStartTime())));

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.fade_in);
            animation.setDuration(400);
            animation.setStartOffset(position * 50L); // Staggered effect
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryText;
        TextView timeText;
        TextView subtitleText;
        View categoryIndicator;

        public ViewHolder(View view) {
            super(view);
            categoryText = view.findViewById(R.id.categoryText);
            timeText = view.findViewById(R.id.timeText);
            subtitleText = view.findViewById(R.id.subtitleText);
            categoryIndicator = view.findViewById(R.id.categoryIndicator);
        }
    }

    private static class SessionDiffCallback extends DiffUtil.Callback {
        private final List<SessionEntity> oldList;
        private final List<SessionEntity> newList;

        public SessionDiffCallback(List<SessionEntity> oldList, List<SessionEntity> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() { return oldList.size(); }
        @Override
        public int getNewListSize() { return newList.size(); }

        @Override
        public boolean areItemsTheSame(int oldPos, int newPos) {
            return oldList.get(oldPos).getId() == newList.get(newPos).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldPos, int newPos) {
            return oldList.get(oldPos).equals(newList.get(newPos));
        }
    }
}
