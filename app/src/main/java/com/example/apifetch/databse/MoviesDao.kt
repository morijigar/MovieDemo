package com.example.apifetch.databse

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.apifetch.model.Result


@Dao
abstract class MoviesDao {

    @Query("DELETE FROM tbl_movies")
    abstract fun deleteAllRow()

    @Insert(onConflict = REPLACE)
    abstract fun save(item: Result): Long

    @Insert(onConflict = REPLACE)
    abstract suspend fun save(item: List<Result>)

    @Update
    abstract fun update(item: Result?): Int

    @Query("SELECT * FROM tbl_movies")
    abstract fun selectAll(): Cursor

    @Query("select * from tbl_movies")
    abstract fun getNumberOfRows(): Int

    @Query("select * from tbl_movies")
    abstract suspend fun getAllData(): MutableList<Result>

    @Query("SELECT * FROM tbl_movies LIMIT :limit OFFSET :offset")
    abstract suspend fun getAllData(limit: Int, offset: Int): MutableList<Result>

    @Query("SELECT * FROM tbl_movies ORDER BY voteAverage DESC")
    abstract suspend fun getTopRated(): MutableList<Result>

    @Query("SELECT * FROM tbl_movies ORDER BY popularity DESC")
    abstract suspend fun getPopular(): MutableList<Result>

    @Query("SELECT * FROM tbl_movies ORDER BY releaseDate DESC")
    abstract suspend fun getReleaseDate(): MutableList<Result>

}