package com.example.myapplication.databse

import android.content.Context
import androidx.room.Room


class Database private constructor(context: Context) {
    val appDatabase: AppDatabase



    init {
        val dbName = "APPDEMO_DB.sqlite"
        appDatabase = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, dbName)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

    }

    companion object {
        private var database: Database? = null

        fun getInstance(context: Context): Database {
            if (database == null || database!!.appDatabase == null) {
                database = Database(context)
            }
            return database as Database
        }

        fun clearInstance() {
            database = null
        }

        fun closeDBAndDestroyInstance() {

            if (checkIsDbOpen()) {
                database?.appDatabase!!.close()
            }

            clearInstance()
        }

        fun checkIsDbOpen(): Boolean {
            return database?.appDatabase!!.isOpen
        }
    }
}