package cn.shu.sakura.littleblackbike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.blankj.utilcode.utils.PhoneUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.shu.sakura.littleblackbike.R;
import me.wangyuwei.particleview.ParticleView;

public class SplashActivity extends AppCompatActivity {

    private int count = 0;

    @BindView(R.id.particle_view)
    ParticleView particleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        particleView.startAnim();
        particleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                count++;
                goToMainIfBothFinished();
            }
        });

        logInAndSignUp();
    }

    private void logInAndSignUp() {
        // 首先登陆 LeanCloud, 如果此设备没有注册用户, 那就注册了之后登陆
        // 1.拿到设备号（独一无二）
        final String deviceId = PhoneUtils.getPhoneIMEI(SplashActivity.this);
        Logger.d("设备号：" + deviceId);
        AVUser.logInInBackground(deviceId, "DefaultPassword", new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    // 登陆成功，说明注册过了
                    Logger.d("登陆成功");
                    // 登陆成功，进入 MainActivity
                    count++;
                    goToMainIfBothFinished();
                } else {
                    // 登陆失败，分析失败原因，如果没有注册，注册
                    // https://leancloud.cn/docs/error_code.html#_211
                    if (e.getCode() == 211) {
                        // 注册
                        avUser = new AVUser();
                        avUser.setUsername(deviceId);
                        avUser.setPassword("DefaultPassword");
                        avUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    // 成功
                                    Logger.d("注册成功");
                                    logInAndSignUp();
                                } else {
                                    // 失败
                                    Logger.d("注册失败");
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void goToMainIfBothFinished() {
        if (count >= 2) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
