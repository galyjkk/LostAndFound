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

    private static final String BASE_URL = "http://182.92.170.217:8000/";

    public AsyncTaskHttpClient(Context context, ILoginListener listener, List<NameValuePair> content) {
        this.mContext = context;
        this.mListener = listener;
        this.mContent = content;
    }

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub

		String action = params[0];

		JSONParser jParser = new JSONParser();
		// Getting JSON from URL
		JSONObject json = jParser.getJSONFromUrl(BASE_URL+action, mContent);

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
