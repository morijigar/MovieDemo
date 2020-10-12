package com.example.apifetch.databse

import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FactRes(
    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    var title: String = "",

    var rows: MutableList<Rows> = mutableListOf()
) : Parcelable

@Parcelize
@Entity(tableName = "tbl_post")
data class Rows(
    @ColumnInfo(name = "ID") @PrimaryKey(autoGenerate = true)
    var ID: Long = 0,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    var title:  String? = null,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    var description: String? = null,

    @ColumnInfo(name = "imageHref")
    @SerializedName("imageHref")
    @Expose
    var imageHref:  String? = null
) : Parcelable{

    fun getTitleStr():String{

        return if(TextUtils.isEmpty(title)){
            ""
        }else{
            title!!
        }

    }

    fun getDescriptionStr():String{
        return if(TextUtils.isEmpty(description)){
            ""
        }else{
            description!!
        }
    }

    fun getImageHrefStr():String{
        return if(TextUtils.isEmpty(imageHref)){
            ""
        }else{
            imageHref!!
        }
    }
}

/*@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Log.e("Image",imageUrl+"")
    Glide.with(view.getContext())
        .load(imageUrl)*//*.apply(RequestOptions().circleCrop())*//*
        .override(100,100)
        .into(view)
}*/

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Log.e("Image",imageUrl+"")
    Glide.with(view.getContext())
        .load(imageUrl)/*.apply(RequestOptions().circleCrop())*/
        .into(view)
}

