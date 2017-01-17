package com.sravan.ad.popularmovies.utilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HP on 1/17/2017.
 * This is a singleton class created for Volley Request Queue. Only one instance can be created of this class
 * This class can be used in entire application, rather than a specific activity. So we need to pass the Application Context
 * during the creation of object of the FetchSingleton Class
 */

public class FetchSingleton {

    private static FetchSingleton fetchInstance;
    private static Context mContext;
    private RequestQueue fRequestQueue;

    /**
     * The constructor is made as private to make the class a single ton class
     * @param context
     */
    private FetchSingleton(Context context){
        this.mContext = context;
        fRequestQueue = getRequestQueue();
    }

    /**
     * This method creates RequestQueue which can be utilized over entire application;
     * @return
     */
    public RequestQueue getRequestQueue() {
        if(fRequestQueue == null){
            fRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return fRequestQueue;
    }

    public static FetchSingleton getInstance(Context context){
        if (fetchInstance == null){
            fetchInstance = new FetchSingleton(context);
        }
        return fetchInstance;
    }

    /**
     * This method adds the request to the Request Queue;
     * @param request
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> request){
        fRequestQueue.add(request);
    }
}
