package com.example.administrator.ccoupons.Connections;

import android.content.Context;
import android.os.Handler;

import com.example.administrator.ccoupons.Tools.PasswordEncoder;

import static org.apache.http.protocol.HTTP.USER_AGENT;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class SearchThread extends Thread{
    private UHuiConnection connection;
    private String requestString;
    private String url;
    private Context mContext;
    private Handler handler;

    public SearchThread(String url, String reqest, Handler handler, Context context) {
        this.url = url;
        this.requestString = reqest;
        this.mContext = context;
        this.handler = handler;
    }

    public String getResponse() {
        return connection.getContent();
    }

    private void connect(String url){
        try {
            connection = new UHuiConnection(url, handler);
            connection.setHeader("User-Agent", USER_AGENT);
            connection.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            connection.add("keyWord",this.requestString);
            connection.connect();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void run(){
        connect(this.url);
    }
}
