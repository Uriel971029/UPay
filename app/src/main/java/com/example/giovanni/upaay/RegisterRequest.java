package com.example.giovanni.upaay;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.example.giovanni.upaay.MainActivity.URL_SERVER;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL= URL_SERVER + "Usuario/Procesos/registrar.php";
    private Map<String,String> params;
    public RegisterRequest(String username, String password, String matricula, String token,  Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("usuario", username);
        params.put("contrasena", password);
        params.put("idNotification", token);
        params.put("matricula", matricula);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
