package com.android.teaching.miprimeraapp.basedatos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

        //@Query("SELECT * FROM user")
        //List<User> getAll();

        @Query("SELECT * FROM user WHERE username IS :username")
        User findByUsername(String username);

        @Insert
        void insert(User user);

        @Delete
        void delete(User user);



    }



