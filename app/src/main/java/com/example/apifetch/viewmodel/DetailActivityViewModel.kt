package com.example.apifetch.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apifetch.model.MovieDetail
import com.example.myapplication.ws.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetailActivityViewModel : ViewModel() {


    var listData = MutableLiveData<MovieDetail>()


    private val repository: Repository = Repository()


    fun callMovieDetail(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retrievedData = repository.getMovieDetail(movieId)
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
                Log.e("TAG", "throwable " + throwable.message)
            }
        }
    }


}