package com.example.myapplication.ws


import com.example.apifetch.model.MovieDetail
import com.example.apifetch.model.MoviesList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {


    @GET("popular")
    suspend fun getPopular(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = "43db70c0c49d201f657e350efe02980a"
    ): MoviesList

    @GET("top_rated")
    suspend fun getTopRated(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = "43db70c0c49d201f657e350efe02980a"
    ): MoviesList

    @GET("upcoming")
    suspend fun getUpcoming(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = "43db70c0c49d201f657e350efe02980a"
    ): MoviesList

    @GET("{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: String,
        @Query("api_key") api_key: String = "43db70c0c49d201f657e350efe02980a"
    ): MovieDetail
}