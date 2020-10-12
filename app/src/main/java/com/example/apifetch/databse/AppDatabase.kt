package com.example.myapplication.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apifetch.databse.MoviesDao
import com.example.apifetch.model.Result

@Database(entities = [Result::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

}


