package br.edu.ifsp.dmo.ifitness.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.edu.ifsp.dmo.ifitness.R;
import br.edu.ifsp.dmo.ifitness.model.User;

public class LeaderboardAdapter
        extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<User> userList;
    //private Map<String, String> userMap;
    private Context context;

    public LeaderboardAdapter(Context context) {
        this.userList = new ArrayList<>();
        //this.userMap = new HashMap<String, String>();
        this.context = context;
    }

    public void setUsers(List<User> users) {
        userList.addAll(users);
        Collections.sort(userList);
        //userList = users.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        //users.
        //Collections.sort(userList., Collections.reverseOrder());
        /*
        users.forEach(user -> {
            Log.d("leaderbordAdapter", "setUsers: " + user.getName());
            userMap.put(user.getName()
                            + " "
                            + user.getSurname(),
                    user.getPoints());
        });

         */
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
        //Log.d("userMap", "onBindViewHolder: " + position + " -> " + userMap.isEmpty());
        Log.d("userMap", "onBindViewHolder: " + position + " -> " + userList.isEmpty());
//        if (!userMap.isEmpty()) {
        if (!userList.isEmpty() && !userList.get(position).getName().isEmpty()) {
            String name = //user.getKey();
//                    (String) userMap.values().toArray()[position];
//                    (String) userList.values().toArray()[position];
                    userList.get(position).getName() + " " + userList.get(position).getSurname();
            String point = //user.getValue();
//                    (String) userMap.keySet().toArray()[position];
//                    (String) userList.keySet().toArray()[position];
                    String.valueOf(Double.parseDouble(userList.get(position).getPoints()) / 1000);
            //for (Map.Entry<String, String> user : userMap.entrySet()) {
            holder.position.setText(String.valueOf(position + 1));
            holder.name.setText(name);
            holder.point.setText(point);
            //position++;
            //}
        }
    }

    @Override
    public int getItemCount() {
//        Log.d("leaderbordAdapter", "getItemCount: " + userMap.size());
        Log.d("leaderbordAdapter", "getItemCount: " + userList.size());
        //return 1;
//        return userMap.size();
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView position;
        private TextView name;
        private TextView point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Log.d("phy", "ViewHolder: external");
            position = itemView.findViewById(R.id.leaderboard_txt_position);
            name = itemView.findViewById(R.id.leaderboard_txt_name);
            point = itemView.findViewById(R.id.leaderboard_txt_point);
        }
    }
}
