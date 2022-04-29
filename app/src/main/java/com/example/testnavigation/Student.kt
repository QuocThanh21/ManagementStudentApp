package com.example.testnavigation
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Student(
    var name: String,
    @PrimaryKey
    var id: Int,
    var image: String
) : Parcelable, RealmObject() {

    constructor() : this("", 0, "")

    override fun toString(): String {
        return "Student: [ID: ${this.id}, Name: ${this.name}, image: ${this.image}]"
    }

}
