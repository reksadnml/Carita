package com.example.carita.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {

    private String title;
    private String content;
    private String link;
    private String urlPhoto;

    public News(String title, String content, String link, String urlPhoto) {
        this.title = title;
        this.content = content;
        this.link = link;
        this.urlPhoto = urlPhoto;
    }

    public News() {

    }

    protected News(Parcel in) {
        title = in.readString();
        content = in.readString();
        link = in.readString();
        urlPhoto = in.readString();
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
        parcel.writeString(link);
        parcel.writeString(urlPhoto);
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public static Creator<News> getCREATOR() {
        return CREATOR;
    }
}

