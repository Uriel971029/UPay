package com.example.giovanni.upaay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegistroComplemento extends AppCompatActivity {

    EditText carrera, cuatrimestre, grupo, usuario, contrasena;
    String matricula, nombre, apellidoP, apellidoM, correo, telefono;
    private String BraintreeIDCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complemento_registro);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();

        carrera = (EditText) findViewById(R.id.etcarrera);
        cuatrimestre = (EditText) findViewById(R.id.etcuatrimestre);
        grupo = (EditText) findViewById(R.id.etgrupo);

        usuario = (EditText) findViewById(R.id.etuser);
        contrasena = (EditText) findViewById(R.id.etcontra);

        //RECUPERAMOS LOS DATOS ENVIADOS POR EL ANTERIOR ACTIVITY
        carrera.setText(intent.getStringExtra("carrera"));
        cuatrimestre.setText(intent.getStringExtra("cuatrimestre"));
        grupo.setText(intent.getStringExtra("grupo"));
        matricula = intent.getStringExtra("matricula");
        nombre = intent.getStringExtra("nombre");
        apellidoP = intent.getStringExtra("apeP");
        apellidoM = intent.getStringExtra("apeM");
        correo = intent.getStringExtra("correo");
        telefono = intent.getStringExtra("telefono");


    }


    public void BraintreeCreateIdUser(View view){

        /*Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    //OBTENEMOS EL NUEVO ID DEL USUARIO EN LA PLATAFORMA DE PAGOS BRAINTREE (EN NUESTRO SERVIDOR)
                    BraintreeIDCustomer = response;
                    //REGISTRAMOS AL ALUMNO EN LA APLICACIÓN
                    registrar();
            }
        };
        
        createCustomerBraintreeRequest customerBraintreeRequest= new createCustomerBraintreeRequest(nombre, apellidoP, apellidoM, correo, telefono, respoListener);
        VolleySingleton.getInstance(this).addToRequestQueue(customerBraintreeRequest);*/
        //REGISTRAMOS AL ALUMNO EN LA APLICACIÓN
        registrar();
    }


    public void registrar () {

        //recuperamos de las preferencias de usuario el token de FireBase
        SharedPreferences preferences = getSharedPreferences("misPreferencias", MODE_PRIVATE);
        //final Usuario usuario = Usuario.getUsuario();
        String token = preferences.getString("id", "");

        //VALIDAR CAMPOS DEL REGISTRO
        boolean comprobar = false;
        comprobar = validar();


        if (comprobar) {
            //USAREMOS LA FUNCIÓN TRIM PARA ELIMINAR TODOS LOS ESPACIOS EN BLANCO DE LA CADENA
            final String username = usuario.getText().toString().trim();
            final String password = contrasena.getText().toString().trim();

            Response.Listener<String> respoListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        response = response.substring(1, response.length());
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean succes = jsonResponse.getBoolean("success");
                        int status = jsonResponse.getInt("status");

                        if(succes && status == 1)
                        {
                            //Drawable drawable = null;
                            //GENERAMOS UN NUEVO ID DE NOTIFICACIÓN ELIMINANDO EL ANTERIOR GENERADO POR FIREBASE
                            //Usuario user = Usuario.getUsuario(matricula, nombre, apellidoP, apellidoM, Integer.parseInt(cuatrimestre.getText().toString()), grupo.getText().toString(), carrera.getText().toString(),correo, telefono, "0" , drawable, usuario.getText().toString(), password, "");
                            SharedPreferences preferences = getSharedPreferences("misPreferencias", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", usuario.getText().toString());
                            editor.commit();

                            //TERMINAMOS LA ACTIVIDAD ANTERIOR YA QUE EL REGISTRO SE COMPONE DE 2 ACTIVIDADES
                            ((ResultReceiver)getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

                            Intent intent= new Intent(RegistroComplemento.this, MainActivity.class);
                            intent.putExtra("registro",1);
                            //intent.putExtra("usuario", user.getUsername());
                            finish();
                            startActivity(intent);

                        }else if(status == -1){

                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroComplemento.this);
                            builder.setMessage("Usuario o matricula existente, por favor verifique sus datos")
                                    .setPositiveButton("Reintentar", null)
                                    .create().show();

                        }else{

                            usuario.requestFocus();
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroComplemento.this);
                            builder.setMessage("Error de Registro")
                                    .setNegativeButton("Reintentar", null)
                                    .create().show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };

            //BraintreeIDCustomer
            RegisterRequest registerRequest= new RegisterRequest(username, password, matricula, token, respoListener);
            //RequestQueue queue = Volley.newRequestQueue(RegistroComplemento.this);
            //queue.add(registerRequest);
            VolleySingleton.getInstance(this).addToRequestQueue(registerRequest);

        }
    }


    public boolean validar() {


        usuario.setError(null);
        contrasena.setError(null);

        String user = usuario.getText().toString().trim();
        String password = contrasena.getText().toString().trim();


        if (TextUtils.isEmpty(user)) {
            usuario.setError("Campo Obligatorio");
            usuario.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password) || (password.length() < 5)) {
            contrasena.setError("La contraseña debe contener por lo menos 5 caracteres");
            contrasena.requestFocus();
            return false;

        }

        return true;
    }


    public void cancelar (View view){

        super.onBackPressed();

    }

}
