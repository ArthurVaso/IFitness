package br.edu.ifsp.dmo.ifitness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.edu.ifsp.dmo.ifitness.R;
import br.edu.ifsp.dmo.ifitness.model.User;

public class LeaderboardAdapter
        extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;

    public LeaderboardAdapter(Context context) {
        this.userList = new ArrayList<>();
        this.context = context;
    }

    public void setUsers(List<User> users) {
        userList.addAll(users);
        Collections.sort(userList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.leaderboard_activity,
                        parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!userList.isEmpty() && !userList.get(position).getName().isEmpty()) {
            String name = userList.get(position).getName() + " " + userList.get(position).getSurname();
            String point = String.valueOf(Double.parseDouble(userList.get(position).getPoints()) / 1000);

            holder.position.setText(String.valueOf(position + 1));
            holder.name.setText(name);
            holder.point.setText(point);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView position;
        private TextView name;
        private TextView point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            position = itemView.findViewById(R.id.leaderboard_txt_position);
            name = itemView.findViewById(R.id.leaderboard_txt_name);
            point = itemView.findViewById(R.id.leaderboard_txt_point);
        }
    }
}
