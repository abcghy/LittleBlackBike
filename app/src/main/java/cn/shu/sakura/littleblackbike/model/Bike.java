package cn.shu.sakura.littleblackbike.model;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by sakura on 2016/9/15.
 */
@AVClassName("Bike")
public class Bike extends AVObject {

    public Bike() {
        super();
    }

    public Bike(Parcel in) {
        super(in);
    }

    public static final Creator CREATOR = AVObjectCreator.instance;

    public String getBikeId() {
        return getString("bikeId");
    }

    public void setBikeId(String bikeId) {
        put("bikeId", bikeId);
    }

    public String getPassword() {
        return getString("password");
    }

    public void setPassword(String password) {
        put("password", password);
    }

    public String getUpDeviceId() {
        return getString("deviceId");
    }

    public void setUpDeviceId(String upDeviceId) {
        put("upDeviceId", upDeviceId);
    }

    public int getVote() {
        return getInt("vote");
    }

    public void setVote(int vote) {
        put("vote", vote);
    }

}
