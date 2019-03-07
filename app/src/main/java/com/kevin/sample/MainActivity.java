package com.kevin.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.kevin.klhttp.databus.RegisterBus;
import com.kevin.klhttp.databus.RxBus;
import com.kevin.klhttp.net.KLHttpMethod;
import com.kevin.klhttp.net.RestClient;
import com.kevin.klhttp.net.callback.IFailure;
import com.kevin.klhttp.net.callback.ISuccess;
import com.kevin.klhttp.net.exception.RestException;
import com.kevin.klhttp.ui.LoaderStyle;
import com.kevin.sample.bean.Douban;
import com.kevin.sample.bean.DoubanResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kevinliu
 */
public class MainActivity extends AppCompatActivity {

    RecyclerView mRecycleView;
    Button requestBtn;
    ArrayList<Douban> mDoubans= new ArrayList<>();
    DoubanAdapter mDoubanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestBtn = findViewById(R.id.request_btn);
        mRecycleView = findViewById(R.id.recycleview);


        setRecycleView();

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoubanMovies();
            }
        });

        RxBus.getInstance().register(this);



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }


    @RegisterBus
    public void doubanMovies(DoubanResponse doubanResponse){
        mDoubans.clear();
        mDoubans.addAll(doubanResponse.getSubjects());
        mDoubanAdapter.notifyDataSetChanged();
    }

    private void setRecycleView(){
        mDoubanAdapter = new DoubanAdapter(this,mDoubans);
        mRecycleView.setLayoutManager(new GridLayoutManager(this,4));
        mRecycleView.setAdapter(mDoubanAdapter);
    }



    private void getDoubanMovies(){


        //请求豆瓣电影列表
        RestClient.builder()
                .url("/v2/movie/top250")
                .param("start", "0")
                .param("count", "20")
                .loader(this)
                .method(KLHttpMethod.GET)
                .entityType(DoubanResponse.class)
                .showErrorToast(true)
                .success(new ISuccess<DoubanResponse>() {
                    @Override
                    public void onSuccess(DoubanResponse response) {

                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(RestException e) {
                        if (e.isNetWorkError()){

                        }
                        else if (e.isServerError()){

                        }
                        else if (e.isDataError()){

                        }
                    }
                })
                .build()
                .request();
    }
}
