package com.example.mvvmpracticetask.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvmpracticetask.constants.DatabaseConstants
import java.io.Serializable

@Entity(tableName = DatabaseConstants.TABLE_NAME_USER)
data class User(
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "age") val age : Int,
    @ColumnInfo(name = "dob") val dateOfBirthTimeInMillis : Long,
    @ColumnInfo(name = "address") val address : String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}
