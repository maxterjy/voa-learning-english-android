package minimalism.voalearning.newslist

import android.os.Parcel
import android.os.Parcelable

class NewsInfo(var mTitle: String, var mDuration: String, var mAudioUrl: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mTitle)
        parcel.writeString(mDuration)
        parcel.writeString(mAudioUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsInfo> {
        override fun createFromParcel(parcel: Parcel): NewsInfo {
            return NewsInfo(parcel)
        }

        override fun newArray(size: Int): Array<NewsInfo?> {
            return arrayOfNulls(size)
        }
    }
}