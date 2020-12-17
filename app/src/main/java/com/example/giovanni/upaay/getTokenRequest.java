package com.example.giovanni.upaay;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class getTokenRequest extends StringRequest {

    private static final String REQUEST_URL = "http://upay-com.sites.stackstaging.com/braintree/main.php";

    private Map<String,String> params;
    public getTokenRequest(Response.Listener<String> listener)
    {
        super(Request.Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        //params.put("idCustomer", idCustomer);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
