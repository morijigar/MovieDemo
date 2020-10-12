package com.example.apifetch.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apifetch.model.MoviesList
import com.example.apifetch.model.Result
import com.example.myapplication.databse.Database
import com.example.myapplication.ws.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainFragmentViewModel : ViewModel() {

    var listData = MutableLiveData<MoviesList>()
    var listDataLocal = MutableLiveData<MutableList<Result>>()


    private val repository: Repository = Repository()

    /*val listData : LiveData<MutableList<PostModel>> = liveData(Dispatchers.IO)  {
        val retrievedData = repository.getData()
        emit(retrievedData)
    }*/

    fun callPopular(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retrievedData = repository.getPopular(page)
                listData.postValue(retrievedData)
            } catch (throwable: Throwable) {
                onApiError(throwable)
            }

        }

    }

    fun callTopRated(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retrievedData = repository.getTopRated(page)
                listData.postValue(retrievedData)
            } catch (throwable: Throwable) {
                onApiError(throwable)
            }

        }

    }

    fun callUpcoming(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retrievedData = repository.getUpcoming(page)
                listData.postValue(retrievedData)
            } catch (throwable: Throwable) {
                onApiError(throwable)
            }

        }

    }

    fun onApiError(throwable: Throwable) {
        when (throwable) {
            is IOException -> Log.e("TAG", "IO Exception")
            is HttpException -> {
                val code = throwable.code()
                //val errorResponse = convertErrorBody(throwable)
                Log.e("TAG", "HttpException $code")
            }
            else -> {
                Log.e("TAG", "throwable")
            }
        }
    }

    fun getLocalList(offset: Int, limit: Int, context: Context) {
        viewModelScope.launch {
            val data =
                Database.getInstance(context).appDatabase.moviesDao().getAllData(limit, offset)
            listDataLocal.postValue(data)
        }

    }

    fun getPopular(context: Context) {
        viewModelScope.launch {
            val data = Database.getInstance(context).appDatabase.moviesDao().getPopular()
            listDataLocal.postValue(data)
        }

    }

    fun getTopRated(context: Context) {
        viewModelScope.launch {
            val data = Database.getInstance(context).appDatabase.moviesDao().getTopRated()
            listDataLocal.postValue(data)
        }

    }

    fun getUpcoming(context: Context) {
        viewModelScope.launch {
            val data = Database.getInstance(context).appDatabase.moviesDao().getReleaseDate()
            listDataLocal.postValue(data)
        }

    }

    /*  fun getDatabaseList(): MutableList<Rows> {

          //return Database.getInstance(context).appDatabase.PostDao().getAllData(100, offset)
          return Database.getInstance(context).appDatabase.PostDao().getAllData()


      }*/


}