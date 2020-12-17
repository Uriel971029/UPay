package com.example.giovanni.upaay;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class createCustomerBraintreeRequest extends StringRequest {

    private static final String REQUEST_URL = "http://upay-com.sites.stackstaging.com/braintree/braintree_create_custumer.php";

    private Map<String,String> params;
    public createCustomerBraintreeRequest(String name, String firstLastname, String secondLastname, String email, String phone, Response.Listener<String> listener)
    {
        super(Request.Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("nombre", name);
        params.put("apellidoP", firstLastname);
        params.put("apellidoM", secondLastname);
        params.put("correo", email);
        params.put("telefono", phone);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
