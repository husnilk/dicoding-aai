package net.husnilkamil.dicodingstory.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
@Parcelize
data class StoryItem(

    @field:SerializedName("photoUrl")
    @ColumnInfo(name = "photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    @ColumnInfo(name = "createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String? = null,

    @field:SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String? = null,

    @field:SerializedName("lon")
    @ColumnInfo(name = "lon")
    val lon: Double? = null,

    @field:SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @field:SerializedName("lat")
    @ColumnInfo(name = "lat")
    val lat: Double? = null
) : Parcelable
