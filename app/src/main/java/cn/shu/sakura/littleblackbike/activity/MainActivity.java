package cn.shu.sakura.littleblackbike.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lapism.searchview.SearchView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.shu.sakura.littleblackbike.R;
import cn.shu.sakura.littleblackbike.model.Bike;

public class MainActivity extends AppCompatActivity {

//    private ArrayList<Bike> mDatas = new ArrayList<>();

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_fab)
    FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);

//        recyclerView.setAdapter(new CommonAdapter(MainActivity.this, R.layout.bike_card, mDatas) {
//            @Override
//            protected void convert(ViewHolder holder, Object o, int position) {
//
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//            }
//        });

//        AVObject testObject = new AVObject("testObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if (e == null) {
////                    Log.d("test", "成功");
//                    Logger.d("成功");
//                } else {
////                    Log.d("test", "失败");
//                    Logger.d("失败");
//                }
//            }
//        });
    }

    @OnClick(R.id.add_fab)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_fab: {
                showAddDialog();
                break;
            }
        }
    }

    private void showAddDialog() {

    }
}
