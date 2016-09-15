package cn.shu.sakura.littleblackbike;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import cn.shu.sakura.littleblackbike.model.Bike;
import cn.shu.sakura.littleblackbike.model.ThumbUp;

/**
 * Created by sakura on 2016/9/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AVObject.registerSubclass(ThumbUp.class);
        AVObject.registerSubclass(Bike.class);
        //如果使用美国节点，请加上这行代码 AVOSCloud.useAVCloudUS();
        AVOSCloud.initialize(this, "EisMGGknUa5qxb8VHc1a0Ncm-gzGzoHsz", "SksHIhJYyPucAwnjll7hJh9D");

        Logger
                .init("test")                   // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
//                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(2);                // default 0
//                .logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
    }

}
