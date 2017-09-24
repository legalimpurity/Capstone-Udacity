package com.vakilapp.lawbooks.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rajatkhanna on 24/09/17.
 */

public class Book implements Parcelable {

    public int id;
    public String name;
    public int version;
    public boolean downloaded;
    public int numberOfChapters;

    private ArrayList<Chapter> chapters;

    public Book(int id, String name, int version, boolean downloaded, int numberOfChapters) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.downloaded = downloaded;
        this.numberOfChapters = numberOfChapters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.version);
        dest.writeByte(this.downloaded ? (byte) 1 : (byte) 0);
        dest.writeList(this.chapters);
    }

    protected Book(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.version = in.readInt();
        this.downloaded = in.readByte() != 0;
        this.chapters = new ArrayList<Chapter>();
        in.readList(this.chapters, Chapter.class.getClassLoader());
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
