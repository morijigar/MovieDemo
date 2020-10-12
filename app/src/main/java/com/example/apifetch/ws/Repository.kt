package com.example.myapplication.ws

import com.example.apifetch.ws.RetrofitClientInstance

class Repository {

    var client = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)

    suspend fun getPopular(page: Int) = client.getPopular(page = page)
    suspend fun getTopRated(page: Int) = client.getTopRated(page = page)
    suspend fun getUpcoming(page: Int) = client.getUpcoming(page = page)
    suspend fun getMovieDetail(movieId: String) = client.getMovieDetail(movieId = movieId)

}