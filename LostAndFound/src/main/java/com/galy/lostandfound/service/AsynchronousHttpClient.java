package com.galy.lostandfound.service;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.*;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class AsynchronousHttpClient {

    private String mUrl;
    private ILoadListener mListener;
    private AsyncHttpClient mClient;
    private Context mContext;

    public AsynchronousHttpClient(String url, ILoadListener listener, Context context){
        this.mClient = new AsyncHttpClient();
        this.mUrl = url;
        this.mListener = listener;
        this.mContext = context;
    }

    // Http get
    public void get(){
        mClient.get(mUrl, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                mListener.complete(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("Error when get:", String.valueOf(statusCode));
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onStart() {
                mListener.loading();
            }
        });
    }

    // Http post
    public void post(JSONObject params){
//        RequestParams params = new RequestParams(p);
        StringEntity entity = null;
        try {
            entity = new StringEntity(params.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mClient.post(mContext ,mUrl, entity, "application/json", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                mListener.complete(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("Error when get:", String.valueOf(statusCode));
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onStart() {
                mListener.loading();
            }
        });
    }

    public interface ILoadListener{
        void loading();

        void complete(JSONArray j);
    }

//    private static final String BASE_URL = "http://182.92.170.217:8000/giveMeSomeData1/";
//
//    private static AsyncHttpClient client = new AsyncHttpClient();
//
//    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.get(getAbsoluteUrl(url), params, responseHandler);
//    }
//
//    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.post(getAbsoluteUrl(url), params, responseHandler);
//    }
//
//    private static String getAbsoluteUrl(String relativeUrl) {
//        return BASE_URL + relativeUrl;
//    }

}
