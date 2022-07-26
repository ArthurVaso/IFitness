package br.edu.ifsp.dmo.ifitness.repository;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("email", user.getEmail());
            parameters.put("password", user.getPassword());
            parameters.put("returnSecureToken", true);
            parameters.put("Content-Type",
                    "application/json; charset=utf-8");
        } catch (JSONException e) {
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
                            user.setId(response.getString("localId"));
                            user.setPassword(response.getString("idToken"));

                            firestore.collection("user")
                                    .document(user.getId())
                                    .set(user)
                                    .addOnSuccessListener(unused -> {
                                    });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.toString(), error.getMessage());
                    }
                }
        );

        queue.add(request);
    }

    public LiveData<List<User>> loadUsers() {
        MutableLiveData<List<User>> liveData = new MutableLiveData<>();

        CollectionReference userRef =
                firestore.collection("user");

        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> userList = new ArrayList<>();
                    User user = null;

                    for (QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {
                        user = queryDocumentSnapshots.toObject(User.class);
                        user.setId(queryDocumentSnapshots.getId());
                        userList.add(user);
                    }

                    liveData.setValue(userList);
                }
            }
        });

        return liveData;
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

        DocumentReference userRef = firestore.collection("user").document(userWithActivities.getUser().getId());

        userRef.set(userWithActivities.getUser()).addOnSuccessListener(unused -> {
            updated[0] = true;
        });

        return updated[0];
    }

    public LiveData<List<PhysicalActivities>> recentActivities(String userId) {
        UserWithActivities userWithActivities = new UserWithActivities();
        MutableLiveData<List<PhysicalActivities>> liveData = new MutableLiveData<>();

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userId);

        userRef.get().addOnSuccessListener(snapshot -> {
            User user = snapshot.toObject(User.class);

            user.setId(user.getId());

            userWithActivities.setUser(user);

            userRef.collection("physical-activities")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limitToLast(5)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<PhysicalActivities> physicalActivitiesList = new ArrayList<>();
                        PhysicalActivities physicalActivities = null;

                        for (QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {
                            physicalActivities = queryDocumentSnapshots.toObject(PhysicalActivities.class);
                            physicalActivities.setId(queryDocumentSnapshots.getId());
                            physicalActivitiesList.add(physicalActivities);

                        }

                        liveData.setValue(physicalActivitiesList);
                    }
                }
            });
        });

        return liveData;
    }

    public LiveData<PhysicalActivities> loadActivitiesById(String userId, String activityId) {
        MutableLiveData<PhysicalActivities> liveData = new MutableLiveData<>();

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userId)
                        .collection("physical-activities")
                        .document(activityId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PhysicalActivities physicalActivities =
                        documentSnapshot.toObject(PhysicalActivities.class);
                physicalActivities.setId(documentSnapshot.getId());

                liveData.setValue(physicalActivities);
            }
        });

        return liveData;
    }

    public LiveData<List<PhysicalActivities>> loadActivitiesByType(String userId, String sportType) {
        UserWithActivities userWithActivities = new UserWithActivities();
        MutableLiveData<List<PhysicalActivities>> liveData = new MutableLiveData<>();

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userId);

        userRef.get().addOnSuccessListener(snapshot -> {
            User user = snapshot.toObject(User.class);

            user.setId(user.getId());

            userWithActivities.setUser(user);

            userRef.collection("physical-activities")
                    .whereEqualTo("activityKind", sportType)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<PhysicalActivities> physicalActivitiesList = new ArrayList<>();
                        PhysicalActivities physicalActivities = null;

                        for (QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {
                            physicalActivities = queryDocumentSnapshots.toObject(PhysicalActivities.class);
                            physicalActivities.setId(queryDocumentSnapshots.getId());
                            physicalActivitiesList.add(physicalActivities);
                        }

                        liveData.setValue(physicalActivitiesList);
                    }
                }
            });
        });
        return liveData;
    }

    public Boolean updatePhysicalActivity(UserWithActivities userWithActivities, PhysicalActivities physicalActivities) {
        final Boolean[] updated = {false};

        DocumentReference userRef = firestore.collection("user")
                .document(userWithActivities.getUser().getId());

        userRef.set(userWithActivities.getUser()).addOnSuccessListener(unused -> {
            Task<Void> physicalActivitiesRef = userRef.collection("physical-activities")
                    .document(physicalActivities.getId())
                    .update("activityDate", physicalActivities.getActivityDate(),
                            "distance", physicalActivities.getDistance(),
                            "hours", physicalActivities.getHours(),
                            "minutes", physicalActivities.getMinutes());

            updated[0] = true;
        });

        return updated[0];
    }

    public Boolean deletePhysicalActivity(UserWithActivities userWithActivities) {
        final Boolean[] updated = {false};

        DocumentReference userRef =
                firestore.collection("user")
                        .document(userWithActivities.getUser().getId());

        userRef.set(userWithActivities.getUser()).addOnSuccessListener(unused -> {
            updated[0] = true;
        });

        PhysicalActivities physicalActivities = userWithActivities.getPhysicalActivities().get(0);

        DocumentReference physicalActivitiesRef = userRef.collection("physical-activities")
                .document(physicalActivities.getId());

        physicalActivitiesRef.delete().addOnSuccessListener(unused -> {
            updated[0] = true;
        });

        return updated[0];
    }
}
