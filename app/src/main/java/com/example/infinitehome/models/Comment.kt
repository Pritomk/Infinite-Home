package com.example.infinitehome.models

import android.os.Parcel
import android.os.Parcelable

data class Comment(
    var imageUrl: String = "",
    var displayName: String = "",
    var commentText: String = ""
)
    : Parcelable {
    constructor(parcel: Parcel) : this() {
        imageUrl = parcel.readString()!!
        displayName = parcel.readString()!!
        commentText = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrl)
        parcel.writeString(displayName)
        parcel.writeString(commentText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }
}

