package com.vakilapp.lawbooks.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rajatkhanna on 24/09/17.
 */

public class Chapter implements Parcelable {
    public long id;
    public String chapterName;
    public String content;

    public Chapter(long id, String chapterName, String content) {
        this.id = id;
        this.chapterName = chapterName;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.chapterName);
        dest.writeString(this.content);
    }

    protected Chapter(Parcel in) {
        this.id = in.readLong();
        this.chapterName = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel source) {
            return new Chapter(source);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
}
