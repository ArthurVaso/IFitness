package br.edu.ifsp.dmo.ifitness.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.dmo.ifitness.SportEditActivity;
import br.edu.ifsp.dmo.ifitness.R;
import br.edu.ifsp.dmo.ifitness.model.PhysicalActivities;

public class ActivityAdapter
        extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<PhysicalActivities> activities;
    private Context context;

    public ActivityAdapter(Context context) {
        this.activities = new ArrayList<>();
        this.context = context;
    }

    public void setActivities(List<PhysicalActivities> activities) {
        Log.d("phy", "setActivities one: " + activities.size());
        this.activities = activities;
        Log.d("phy", "setActivities two: " + this.activities.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_activity,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("phy", "onBindViewHolder: " + activities.size() + " pos: " + position);
        if(activities != null && activities.size() >= position) {
            Log.d("phy", "onBindViewHolder: " + activities.size());
            PhysicalActivities physicalActivities = activities.get(position);
            holder.activity.setText(physicalActivities.getActivityCategory());
            holder.date.setText(physicalActivities.getActivityDate());
            holder.distance.setText(context.getString(R.string.activity_adapter_you_did)
                    + String .valueOf(Double.parseDouble(physicalActivities.getDistance())/1000)
                    + context.getString(R.string.activity_adapter_km_in));
            holder.time.setText(physicalActivities.getHours()
                    +":"
                    +physicalActivities.getMinutes()
                    + context.getString(R.string.activity_adapter_hours));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SportEditActivity.class);
                    intent.putExtra("activity", physicalActivities);
                    context.startActivity(intent);
                }
            });
        }
        Log.d("phy", "onBindViewHolder: " + activities.size() + " pos: " + position);
    }

    @Override
    public int getItemCount() {
        Log.d("phy", "getItemCount: " + activities.size());
        return activities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView activity;
        private TextView date;
        private TextView time;
        private TextView distance;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("phy", "ViewHolder: external");
            activity = itemView.findViewById(R.id.txtItemActivity);
            date = itemView.findViewById(R.id.txtItemDate);
            time = itemView.findViewById(R.id.txtItemTime);
            distance = itemView.findViewById(R.id.txtItemDistance);
            cardView = itemView.findViewById(R.id.cardItemActivity);
        }
    }
}
