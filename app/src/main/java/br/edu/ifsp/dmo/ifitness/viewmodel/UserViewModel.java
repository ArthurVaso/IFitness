package br.edu.ifsp.dmo.ifitness.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Optional;

import br.edu.ifsp.dmo.ifitness.model.User;
import br.edu.ifsp.dmo.ifitness.model.UserWithActivities;
import br.edu.ifsp.dmo.ifitness.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    public static final String USER_ID = "USER_ID";

    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public void createUser(User user){
        userRepository.createUser(user);
    }

    public LiveData<User> login(String email, String password) {
        return userRepository.login(email, password);
    }

    public void logout(){
        PreferenceManager.getDefaultSharedPreferences(getApplication())
                .edit().remove(USER_ID)
                .apply();
    }

    public LiveData<UserWithActivities> islogged() {
        SharedPreferences sharedPreferences =
                PreferenceManager
                        .getDefaultSharedPreferences(
                                getApplication());
        Optional<String> id =
                Optional.ofNullable(sharedPreferences
                        .getString(USER_ID, null));
        if(!id.isPresent()){
            return new MutableLiveData<>(null);
        }
        return userRepository.load(id.get());
    }

    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }

    public void update(UserWithActivities userWithActivities) {
        userRepository.update(userWithActivities);
    }

}
