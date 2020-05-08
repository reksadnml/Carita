package com.example.carita.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {

    private String title;
    private String content;
    private int photo;

    public News(String title, String content, int photo) {
        this.title = title;
        this.content = content;
        this.photo = photo;
    }

    protected News(Parcel in) {
        title = in.readString();
        content = in.readString();
        photo = in.readInt();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeInt(photo);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}

