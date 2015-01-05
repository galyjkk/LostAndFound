package com.galy.lostandfound.service;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncTaskHttpClient extends AsyncTask<String, Void, JSONObject> {

    private Context mContext;
    private ILoginListener mListener;
    private List<NameValuePair> mContent;
    private String mTag;

    private static final String BASE_URL = "http://182.92.170.217:8000/";

    public AsyncTaskHttpClient(Context context, String method, ILoginListener listener, List<NameValuePair> content) {
        this.mContext = context;
        this.mListener = listener;
        this.mContent = content;
        this.mTag = method;
    }

    public AsyncTaskHttpClient(Context context, String method, ILoginListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mTag = method;
    }

	@Override
	protected JSONObject doInBackground(String... params) {
		String action = params[0];

		JSONParser jParser = new JSONParser();
        JSONObject json = null;
		// Getting JSON from URL
        if (mTag == "post"){
            json = jParser.post(BASE_URL + action, mContent);
        } else {
            String param = params[1];
            json = jParser.get(BASE_URL + action + param);
        }

		return json;
	}

    protected void onPostExecute(JSONObject result) {
        mListener.complete(result);
    }

    @Override
    protected void onPreExecute() {
        mListener.loading();
    }

    public interface ILoginListener {
        void loading();

        void complete(JSONObject result);
    }
}
