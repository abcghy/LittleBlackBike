package cn.shu.sakura.littleblackbike.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.blankj.utilcode.utils.StringUtils;
import com.lapism.searchview.SearchView;
import com.orhanobut.logger.Logger;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.shu.sakura.littleblackbike.R;
import cn.shu.sakura.littleblackbike.model.Bike;
import cn.shu.sakura.littleblackbike.model.ThumbUp;

public class MainActivity extends AppCompatActivity {

    private final static int EMPTY    = 1;
    private final static int LOADING  = 2;
    private final static int FINISHED = 3;
    private final static int ERROR    = 4;

    private ArrayList<Bike> resources = new ArrayList<>();

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_fab)
    FloatingActionButton addFab;
    @BindView(R.id.state_view)
    RelativeLayout stateView;

    private LinearLayoutManager layoutManager;

    // 此时整个应用的状态
    private int mState = EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);

        searchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                search(searchView.getQuery().toString());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CommonAdapter<Bike>(MainActivity.this, R.layout.bike_card, resources) {

            @Override
            protected void convert(ViewHolder holder, Bike bike, int position) {
                holder.setText(R.id.bike_id, bike.getBikeId());
                holder.setText(R.id.bike_password, bike.getPassword());
                holder.setText(R.id.text_thumb_up, String.valueOf(bike.getVote()));

//                holder.setOnClickListener(R.id.layout_thumb_up, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // 先查看，自己是否点过该号码其他的？
//                        AVQuery<ThumbUp> thumbUpAVQuery = AVQuery.getQuery(ThumbUp.class);
//                        thumbUpAVQuery.whereEqualTo("deviceId", AVUser.getCurrentUser().getUsername());
//                        thumbUpAVQuery.findInBackground(new FindCallback<ThumbUp>() {
//                            @Override
//                            public void done(List<ThumbUp> list, AVException e) {
//
//                            }
//                        });
//                    }
//                });
            }

        });

        refreshState();
    }

    /**
     * 根据 query 搜索相关数据
     * @param query searchView 中的字符串
     */
    private void search(String query) {
        searchView.hideKeyboard();
        searchView.clearFocus();

        mState = LOADING;
        refreshState();

        AVQuery<Bike> bikeAVQuery = AVQuery.getQuery(Bike.class);
        bikeAVQuery.whereEqualTo("bikeId", query);
        bikeAVQuery.findInBackground(new FindCallback<Bike>() {
            @Override
            public void done(List<Bike> list, AVException e) {
                if (e == null) {
                    // 成功
                    Logger.d("搜索成功");

                    resources.clear();
                    if (list == null || list.size() == 0) {
                        mState = EMPTY;
                        refreshState();
                    } else if (list.size() > 0){
                        resources.addAll(list);
                        mState = FINISHED;
                        refreshState();
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    // 失败
                    Logger.d("搜索失败");
                    e.printStackTrace();

                    mState = ERROR;
                    refreshState();
                }
            }
        });
    }

    /**
     * 点击事件
     * @param view 所点击的视图
     */
    @OnClick(R.id.add_fab)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fab: {
                showAddDialog();
                break;
            }
        }
    }

    /**
     * 展示添加对话框
     */
    private void showAddDialog() {
        String searchResult = searchView.getQuery().toString();

        View view = LayoutInflater
                .from(MainActivity.this)
                .inflate(R.layout.layout_add_password, null, true);

        final MaterialEditText addBikeId = (MaterialEditText) view.findViewById(R.id.add_bike_id);
        final MaterialEditText addBikePassword = (MaterialEditText) view.findViewById(R.id.add_bike_password);

        new MaterialDialog.Builder(MainActivity.this)
                .title("添加密码")
                .customView(view, false)
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final String addBikeIdString = addBikeId.getText().toString();
                        final String addBikePasswordString = addBikePassword.getText().toString();

                        if (!StringUtils.isSpace(addBikeIdString) && !StringUtils.isSpace(addBikePasswordString)) {
                            // 先判断，这个用户是否上传过这个车子，如果上传过，那就不行
                            AVQuery<Bike> upDeviceIdAVQuery = AVQuery.getQuery(Bike.class);
                            upDeviceIdAVQuery.whereEqualTo("upDeviceId", AVUser.getCurrentUser().getUsername());
                            AVQuery<Bike> bikeIdAVQuery = AVQuery.getQuery(Bike.class);
                            bikeIdAVQuery.whereEqualTo("bikeId", addBikeIdString);

                            AVQuery<Bike> bikeAVQuery = AVQuery.and(Arrays.asList(upDeviceIdAVQuery, bikeIdAVQuery));
                            bikeAVQuery.findInBackground(new FindCallback<Bike>() {
                                @Override
                                public void done(List<Bike> list, AVException e) {
                                    // 如果有，说明不能上传，如果为零，可以上传
                                    if (list != null && list.size() > 0) {
                                        // 不可上传
                                        Toast.makeText(MainActivity.this, "你已上传过该车辆", Toast.LENGTH_SHORT).show();
                                    } else if (list == null || list.size() == 0) {
                                        // 上传该车辆
                                        Bike bike = new Bike();
                                        bike.setBikeId(addBikeIdString);
                                        bike.setPassword(addBikePasswordString);
                                        bike.setUpDeviceId(AVUser.getCurrentUser().getUsername());
                                        bike.setVote(0);
                                        bike.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    // 成功
                                                    Logger.d("上传成功");
                                                    // 提示用户上传成功
                                                    Toast.makeText(MainActivity.this, R.string.add_success_toast, Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // 失败
                                                    Toast.makeText(MainActivity.this, R.string.add_error_toast, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            // 不能为空呀
                            Toast.makeText(MainActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();

        if (!StringUtils.isSpace(searchResult)) {
            // 如果搜索不为空，就需要开始输入密码
            addBikeId.setText(searchResult);
            addBikePassword.requestFocus();
        }
    }

    private void refreshState() {
        switch (mState) {
            case EMPTY: {
                View view = LayoutInflater
                        .from(MainActivity.this)
                        .inflate(R.layout.layout_empty, new RelativeLayout(MainActivity.this), false);
                stateView.removeAllViews();
                stateView.addView(view);
                stateView.setVisibility(View.VISIBLE);
                break;
            }
            case LOADING: {
                View view = LayoutInflater
                        .from(MainActivity.this)
                        .inflate(R.layout.layout_loading, new RelativeLayout(MainActivity.this), true);
                stateView.removeAllViews();
                stateView.addView(view);
                stateView.setVisibility(View.VISIBLE);
                break;
            }
            case FINISHED: {
                stateView.setVisibility(View.INVISIBLE);
                break;
            }
            case ERROR: {
                View view = LayoutInflater
                        .from(MainActivity.this)
                        .inflate(R.layout.layout_error, new RelativeLayout(MainActivity.this), true);
                stateView.removeAllViews();
                stateView.addView(view);
                stateView.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

}
