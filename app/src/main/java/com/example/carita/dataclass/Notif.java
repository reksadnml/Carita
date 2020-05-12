package com.example.carita.dataclass;

import android.os.Parcel;
import android.os.Parcelable;

public class Notif implements Parcelable {

    private String title;
    private String content;

    public Notif(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Notif() {

    }

    protected Notif(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<Notif> CREATOR = new Creator<Notif>() {
        @Override
        public Notif createFromParcel(Parcel in) {
            return new Notif(in);
        }

        @Override
        public Notif[] newArray(int size) {
            return new Notif[size];
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

    public static Creator<Notif> getCREATOR() {
        return CREATOR;
    }
}
