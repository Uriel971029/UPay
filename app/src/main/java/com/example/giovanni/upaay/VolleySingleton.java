package com.example.giovanni.upaay;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import okhttp3.OkHttpClient;

public class VolleySingleton {

    private static VolleySingleton volleyInstance;
    private RequestQueue request;
    private ImageLoader imageLoader;
    private static Context contexto;



    private VolleySingleton(Context context) {
        contexto= context;
        request = getRequestQueue();

        imageLoader = new ImageLoader(request,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (volleyInstance == null) {
            volleyInstance = new VolleySingleton(context);
        }
        return volleyInstance;
    }


    public RequestQueue getRequestQueue() {
        if (request == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            request = Volley.newRequestQueue(contexto.getApplicationContext(), new OkHttpStack(new com.squareup.okhttp.OkHttpClient()));
        }
        return request;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

}




