package br.edu.ifsp.dmo.ifitness.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import br.edu.ifsp.dmo.ifitness.model.User;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    User login(String email, String password);

    @Insert
    void insert(User user);

    @Update
    void update(User user);
}
