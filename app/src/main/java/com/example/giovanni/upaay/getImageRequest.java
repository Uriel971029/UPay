package com.example.giovanni.upaay;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.example.giovanni.upaay.MainActivity.URL_SERVER;


public class getImageRequest extends StringRequest{

    //indicamos el URL al que le vamos a hacer la petición
    private static final String UNPLOAD_IMAGE_REQUEST_URL= URL_SERVER + "getPicture.php";
    private Map<String,String> params;
    //constructor que recibe datos enviados desde el mainActivity
    public getImageRequest(String username, Response.Listener<String> listener)
    {
        //se usa la función del super en este caso de la clase StringRequest
        super(Request.Method.POST, UNPLOAD_IMAGE_REQUEST_URL, listener, null);
        //insertamos en el map los datos recibidos por la función[clave, valor]
        params = new HashMap<>();
        params.put("usuario",username);

    }

    @Override
    public Map<String, String> getParams(){

        return params;
    }
}
