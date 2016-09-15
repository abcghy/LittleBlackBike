package cn.shu.sakura.littleblackbike.model;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by sakura on 2016/9/15.
 */
@AVClassName("ThumbUp")
public class ThumbUp extends AVObject {
    public ThumbUp() {
        super();
    }

    public ThumbUp(Parcel in) {
        super(in);
    }

    public static final Creator CREATOR = AVObjectCreator.instance;

    public String getDeviceId() {
        return getString("deviceId");
    }

    public void setDeviceId(String deviceId) {
        put("deviceId", deviceId);
    }

    public List getThumbUp() {
        return getList("thumbUp");
    }

    public void setThumbUp(List thumbUp) {
        put("thumbUp", thumbUp);
    }
}
