package com.example.giovanni.upaay;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.example.giovanni.upaay.MainActivity.URL_SERVER;

public class searchUsuarioRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL= URL_SERVER + "buscarUsuario.php";
    private Map<String,String> params;
    public searchUsuarioRequest(String matricula, Response.Listener<String> listener)
    {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("matricula", matricula);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
