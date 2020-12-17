package com.example.giovanni.upaay;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.example.giovanni.upaay.MainActivity.URL_SERVER;

public class searchPhotoRequest extends StringRequest{


    private static final String REGISTER_REQUEST_URL= URL_SERVER + "Usuario/Procesos/editarFoto.php";
    private Map<String,String> params;
    public searchPhotoRequest(String photo, String username, Response.Listener<String> listener)
    {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("foto", photo);
        params.put("usuario", username);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
