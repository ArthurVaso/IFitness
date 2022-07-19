package br.edu.ifsp.dmo.ifitness.repository;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.dmo.ifitness.R;
import br.edu.ifsp.dmo.ifitness.SportEditActivity;
import br.edu.ifsp.dmo.ifitness.database.AppDatabase;
import br.edu.ifsp.dmo.ifitness.database.UserDAO;
import br.edu.ifsp.dmo.ifitness.model.PhysicalActivities;
import br.edu.ifsp.dmo.ifitness.model.User;
import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.viewmodel.UserViewModel;

public class UserRepository {

    private static final String BASE_URL = "https://identitytoolkit.googleapis.com/v1/";
    private static final String SIGNUP = "accounts:signUp";
    private static final String SIGNIN = "accounts:signInWithPassword";
    private static final String PASSWORD_RESET = "accounts:sendOobCode";
    private static final String KEY = "?key=AIzaSyCcbFuZD2jZcSGw7R8ZhVVhNeXTtQPXjhw";

    private UserDAO userDAO;

    private FirebaseFirestore firestore;

    private RequestQueue queue;

    private SharedPreferences preference;

    public UserRepository(Application application) {
        userDAO = AppDatabase.getInstance(application).userDAO();

        firestore = FirebaseFirestore.getInstance();
        queue = Volley.newRequestQueue(application);

        preference = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public void createUser(User user) {
        //Log.d("repo", "createUser: inicio");
        JSONObject parameters = new JSONObject();

        try {
            //Log.d("repo", "createUser: try json");
            parameters.put("email", user.getEmail());
            parameters.put("password", user.getPassword());
            parameters.put("returnSecureToken", true);
            parameters.put("Content-Type",
                    "application/json; charset=utf-8");
        } catch (JSONException e) {
            //Log.d("repo", "createUser: catch json");
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + SIGNUP + KEY,
                parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.d("repo", "createUser: try on response");
                            user.setId(response.getString("localId"));
                            //Log.d("repo", "createUser: setID");
                            user.setPassword(response.getString("idToken"));
                            //Log.d("repo", "createUser: setPassword");

                            firestore.collection("user")
                                    .document(user.getId())
                                    .set(user)
                                    .addOnSuccessListener(unused -> {
                                        //Log.d(this.toString(), R.string.user_repository_user +
                                        //user.getEmail() + R.string.user_repository_success);
                                    });
                            //Log.d("repo", "createUser: pos firebase");
                        } catch (JSONException e) {
                            //Log.d("repo", "createUser: catch on response");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("repo", "createUser: error");
                        Log.e(this.toString(), error.getMessage());
                    }
                }
        );
        //Log.d("repo", "createUser: queue");
        queue.add(request);
    }

    public LiveData<User> login(String email, String password) {
        MutableLiveData<User> liveData = new MutableLiveData<>();
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("email", email);
            parameters.put("password", password);
            parameters.put("returnSecureToken", true);
            parameters.put("Content-Type",
                    "application/json; charset=utf-8");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.POST,
                        BASE_URL + SIGNIN + KEY,
                        parameters,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String localId =
                                            response.getString("localId");
                                    String idToken =
                                            response.getString("idToken");

                                    firestore.collection("user")
                                            .document(localId)
                                            .get()
                                            .addOnSuccessListener(snapshot -> {
                                                User user = snapshot.toObject(User.class);

                                                user.setId(localId);
                                                user.setPassword(idToken);

                                                liveData.setValue(user);

                                                preference.edit()
                                                        .putString(UserViewModel.USER_ID, localId)
                                                        .apply();

                                                firestore.collection("user")
                                                        .document(localId).set(user);
                                            });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                NetworkResponse response = error.networkResponse;
                                if (error instanceof ServerError && response != null) {
                                    try {
                                        String res = new String(response.data,
                                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                        JSONObject obj = new JSONObject(res);
                                        Log.d(this.toString(), obj.toString());
                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    } catch (JSONException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                                liveData.setValue(null);
                            }
                        });

        queue.add(request);

        return liveData;
    }

    public void resetPassword(String email) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("email", email);
            parameters.put("requestType",
                    "PASSWORD_RESET");
            parameters.put("Content-Type",
                    "application/json; charset=utf-8");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + PASSWORD_RESET + KEY, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(this.toString(), response.keys().toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                Log.d(this.toString(), obj.toString());
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                });

        queue.add(request);
    }

    public LiveData<UserWithActivities> load(String userId) {
        UserWithActivities userWithActivities = new UserWithActivities();
        MutableLiveData<UserWithActivities> liveData = new MutableLiveData<>();

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userId);

        userRef.get().addOnSuccessListener(snapshot -> {
            User user = snapshot.toObject(User.class);

            user.setId(user.getId());

            userWithActivities.setUser(user);

            userRef.collection("physical-activities").get().addOnCompleteListener(snap -> {
                snap.getResult().forEach(doc -> {
                    PhysicalActivities physicalActivities = doc.toObject(PhysicalActivities.class);
                    physicalActivities.setId(doc.getId());
                    userWithActivities.getPhysicalActivities().add(physicalActivities);
                });

                liveData.setValue(userWithActivities);
            });
        });

        return liveData;
    }

    public Boolean addActivity(UserWithActivities userWithActivities) {
        final Boolean[] updated = {false};
        Log.d("addActivity", "addActivity: adicionando nova atividade");
        DocumentReference userRef = firestore.collection("user")
                .document(userWithActivities.getUser().getId());

        userRef.set(userWithActivities.getUser()).addOnSuccessListener(unused -> {
            updated[0] = true;
        });

        CollectionReference physicalActivitiesRef = userRef.collection("physical-activities");

        PhysicalActivities physicalActivities = userWithActivities.getPhysicalActivities().get(0);

        physicalActivitiesRef.document(physicalActivities.getId())
                .set(physicalActivities)
                .addOnSuccessListener(unused -> {
                    updated[0] = true;
                });

        return updated[0];
    }

    public Boolean updateUser(UserWithActivities userWithActivities) {
        final Boolean[] updated = {false};
        Log.d("loadUser", "updateUser: ");
        DocumentReference userRef = firestore.collection("user").document(userWithActivities.getUser().getId());

        userRef.set(userWithActivities.getUser()).addOnSuccessListener(unused -> {
            updated[0] = true;
        });
/*
        CollectionReference physicalActivitiesRef = userRef.collection("physical-activities");

        PhysicalActivities physicalActivities = userWithActivities.getPhysicalActivities().;

        if(physicalActivities.getId().isEmpty()){
            physicalActivitiesRef.add(physicalActivities).addOnSuccessListener( pa ->{
                physicalActivities.setId(pa.getId());
                updated[0] = true;
            });
        }else{
            physicalActivitiesRef.document(physicalActivities.getId()).set(physicalActivities).addOnSuccessListener(unused -> {
                updated[0] = true;
            });
        }*/

        return updated[0];
    }

    public LiveData<List<PhysicalActivities>> recentActivities(String userId) {
        UserWithActivities userWithActivities = new UserWithActivities();
        MutableLiveData<List<PhysicalActivities>> liveData = new MutableLiveData<>();
        Log.d("frag", "onChanged: inicio recentActivities");

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userId);

        //Log.d("frag", "onChanged: refuser");

        userRef.get().addOnSuccessListener(snapshot -> {
            User user = snapshot.toObject(User.class);

            user.setId(user.getId());
            //Log.d("frag", "onChanged: refuser get id");

            userWithActivities.setUser(user);

            userRef.collection("physical-activities")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(5)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    //Log.d("frag", "onChanged: on complete");
                    if (task.isSuccessful()) {

                        //Log.d("frag", "onChanged: is successful");
                        List<PhysicalActivities> physicalActivitiesList = new ArrayList<>();
                        PhysicalActivities physicalActivities = null;

                        //Log.d("frag", "onChanged: para o loop da lista");
                        for (QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {
                            physicalActivities = queryDocumentSnapshots.toObject(PhysicalActivities.class);
                            physicalActivities.setId(queryDocumentSnapshots.getId());
                            physicalActivitiesList.add(physicalActivities);
                            //Log.d("frag", "onChanged: " + physicalActivities.getTimestamp());
                        }
                        //Log.d("frag", "onChanged: saiu loop " + physicalActivitiesList.size());
                        liveData.setValue(physicalActivitiesList);
                    }
                }
            });
        });

        //Log.d("frag", "onChanged: retornou valor");
        return liveData;
    }

    public LiveData<PhysicalActivities> loadActivitiesById(String userId, String activityId) {
        MutableLiveData<PhysicalActivities> liveData = new MutableLiveData<>();
        //Log.d("act", "onChanged: inicio");

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userId)
                        .collection("physical-activities")
                        .document(activityId);

        //Log.d("act", "onChanged: refuser");

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("load", "onChanged: colocando valor");
                PhysicalActivities physicalActivities =
                        documentSnapshot.toObject(PhysicalActivities.class);
                physicalActivities.setId(documentSnapshot.getId());
                liveData.setValue(physicalActivities);
            }
        });

        //Log.d("act", "onChanged: retornou valor");
        return liveData;
    }

    public Boolean updatePhysicalActivity(UserWithActivities userWithActivities, PhysicalActivities physicalActivities) {
        final Boolean[] updated = {false};
        Log.d("updPA", "onChanged: inicio update");
        userWithActivities.getPhysicalActivities().forEach(pa -> {
            Log.d("updPhysicalActivity", "PA: " + pa.getId() + " -> " + pa.getDistance());
        });

        DocumentReference userRef = firestore.collection("user").document(userWithActivities.getUser().getId());

        userRef.set(userWithActivities.getUser()).addOnSuccessListener(unused -> {

            Log.d("updPA", "onChanged: busca atividade");

            //PhysicalActivities physicalActivities = userWithActivities.getPhysicalActivities().get(0);
            Log.d("updPA", "updatePhysicalActivity: update na atividade" + physicalActivities.getDistance());

            Task<Void> physicalActivitiesRef = userRef.collection("physical-activities")
                    .document(physicalActivities.getId())
                    .update("activityDate", physicalActivities.getActivityDate(),
                            "distance", physicalActivities.getDistance(),
                            "hours", physicalActivities.getHours(),
                            "minutes", physicalActivities.getMinutes());
                    //.whereArrayContains("id", physicalActivities.getId());


            updated[0] = true;
        });

        Log.d("updPA", "updatePhysicalActivity: saindo" + (userWithActivities.getPhysicalActivities().get(0).getId()));
/*
        physicalActivitiesRef.delete().addOnSuccessListener(unused -> {
            Log.d("updPA", "onChanged: exclui");
            updated[0] = true;
        });
        */
        /*
        DocumentReference userRef = firestore.collection("user")
                .document(userWithActivities.getUser().getId());
        Log.d("updPA", "updatePhysicalActivity: update na atividade" + physicalActivities.getDistance());
*/
/*
        DocumentReference physicalActivitiesRef = userRef.collection("physical-activities")
                .document(physicalActivities.getId());
        Log.d("updPA", "updatePhysicalActivity: update na atividade" + physicalActivities.getDistance());
 */


        /*userRef.set(userWithActivities.getUser()).addOnSuccessListener(unused -> {
            updated[0] = true;
        });
*/
        //PhysicalActivities physicalActivities = userWithActivities.getPhysicalActivities().get(0);
        /*
        CollectionReference physicalActivitiesRef = userRef.collection("physical-activities");
                //.document(physicalActivities.getId());
*/
        /*
        physicalActivitiesRef.document(physicalActivities.getId()).set(physicalActivities)
                .addOnSuccessListener(unused -> {
            updated[0] = true;
        });
         */

        //physicalActivitiesRef.set(physicalActivities).addOnSuccessListener(unused -> {
        //    updated[0] = true;
        //});

        return updated[0];
        /*
        final Boolean[] updated = {false};
        Log.d("upd", "onChanged: inicio");

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userId)
                        .collection("physical-activities")
                        .document(activityId);
        Log.d("upd", "onChanged: get valores");

        PhysicalActivities physicalActivities = userWithActivities.getPhysicalActivities().get(0);
        Log.d("upd", "onChanged: busca atividade");


        userRef.set(physicalActivities).addOnSuccessListener(unused -> {
            Log.d("upd", "onChanged: atualiza");
            updated[0] = true;
        });

        Log.d("upd", "onChanged: fim");
        return updated[0];*/
    }

    public Boolean deletePhysicalActivity(UserWithActivities userWithActivities) {
        final Boolean[] updated = {false};
        Log.d("dlt", "onChanged: inicio");

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userWithActivities.getUser().getId());

        Log.d("dlt", "onChanged: busca atividade");
        userRef.set(userWithActivities.getUser()).addOnSuccessListener(unused -> {
            Log.d("dlt", "deletePhysicalActivity: removido com sucesso");
            updated[0] = true;

        });

        PhysicalActivities physicalActivities = userWithActivities.getPhysicalActivities().get(0);

        DocumentReference physicalActivitiesRef = userRef.collection("physical-activities")
                .document(physicalActivities.getId());

        physicalActivitiesRef.delete().addOnSuccessListener(unused -> {
            Log.d("dlt", "onChanged: exclui");
            updated[0] = true;
        });

        Log.d("dlt", "onChanged: finaliza");
        return updated[0];
    }
}
