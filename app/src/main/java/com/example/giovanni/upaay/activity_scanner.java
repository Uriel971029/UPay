package com.example.giovanni.upaay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class activity_scanner extends AppCompatActivity {

    TextView textImporte, textFecha, textConcepto, textPagado;
    Button btnAceptar, buttRechazar;
    String folio, idSecretaria;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        intent = getIntent();

        textFecha = findViewById(R.id.Fecha);
        textConcepto = findViewById(R.id.Concepto);
        textImporte = findViewById(R.id.Importe);
        textPagado = findViewById(R.id.Pagado);
        //formatearCadena(intent.getStringExtra("cadena"));

    }

    //OBTENEMOS LOS DATOS EN ESPECÍFICO DEL CÓDIGO QR QUE NOS PERMITEN IDENTIFICAR EL PAGO EN LA BD
    /*public String formatearCadena(String cadena){


        int pos = cadena.indexOf(" ");
        folio = cadena.substring((pos+1),(pos+3));

        //HACEMOS LA PETICIÓN HTTP CON EL NUMERO DE FOLIO DEL QR PARA VALIDAR EL PAGO CAMBIANDO SU CAMPO STATUS A 1 EN LA BD

        final ProgressDialog loading = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        loading.setTitle("Procesando datos . . .");
        loading.setMessage("Espere por favor . . .");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //PARA Obtener la datos usamos un onjeto Json
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //RECUPERAMOS TODA LA INFORMACIÓN DEL TRAMITE DE LA BASE DE DATOS
                        String fecha = jsonResponse.getString("fecha");
                        textFecha.setText(fecha);
                        String concepto = jsonResponse.getString("concepto");
                        textConcepto.setText(concepto);
                        String importe = jsonResponse.getString("importe");
                        textImporte.setText(importe);
                        textPagado.setText("PENDIENTE");


                        /*Intent ven = new Intent(activity_scanner.this, menu_bar.class);
                        //INSERTAMOS UN DATO EN ESA ACTIVIDAD PARA PODER COMPARTIRLO DESPUÉS EN OTRO ACTIVITY
                        ven.putExtra("fecha", fecha);
                        ven.putExtra("concepto", concepto);
                        ven.putExtra("importe", importe);


                    } else {

                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(activity_scanner.this);
                        builder.setMessage("Sucedio un error al intentar recuperar los datos del pago")
                                .setNegativeButton("Reintentar",null)
                                .create().show();

                    }

                } catch (JSONException e) {

                    loading.dismiss();
                    e.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_scanner.this);
                    builder.setMessage("Error")
                            .setNegativeButton("Reintentar", null)
                            .create().show();
                }
            }
        };
        //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
        ComprobanteRequest comprobanteRequest = new ComprobanteRequest(folio, responseListener);
        RequestQueue queue = Volley.newRequestQueue(activity_scanner.this);

        //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
        //queue.add(comprobanteRequest);

        return null;
    }*/


    public void Validar (View view){

        idSecretaria = intent.getStringExtra("idSecretaria");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //PARA Obtener la datos usamos un onjeto Json
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {


                        textPagado.setText("PAGADO");
                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(activity_scanner.this);
                        builder.setMessage("Pago Validado")
                                .setPositiveButton("Ok",null)
                                .create().show();

                    } else {


                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(activity_scanner.this);
                        builder.setMessage("COMPROBANTE NO ENCONTRADO")
                                .setNegativeButton("Reintentar",null)
                                .create().show();

                    }

                } catch (JSONException e) {


                    e.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_scanner.this);
                    builder.setMessage("Sucedió un error al validar el pago")
                            .setNegativeButton("Reintentar", null)
                            .create().show();
                }
            }
        };
        //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
        //ValidarPagoRequest validarPagoRequest = new ValidarPagoRequest(folio, idSecretaria, responseListener);
        RequestQueue queue = Volley.newRequestQueue(activity_scanner.this);

        //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
        //queue.add(validarPagoRequest);

    }


    public void Rechazar (View view){

        idSecretaria = intent.getStringExtra("idSecretaria");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //PARA Obtener la datos usamos un onjeto Json
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        textPagado.setText("RECHAZADO");

                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(activity_scanner.this);
                        builder.setMessage("Pago Rechazado")
                                .setPositiveButton("Ok",null)
                                .create().show();

                        /*Intent ven = new Intent(activity_scanner.this, menu_bar.class);
                        //INSERTAMOS UN DATO EN ESA ACTIVIDAD PARA PODER COMPARTIRLO DESPUÉS EN OTRO ACTIVITY
                        ven.putExtra("fecha", fecha);
                        ven.putExtra("concepto", concepto);
                        ven.putExtra("importe", importe);*/


                    } else {


                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(activity_scanner.this);
                        builder.setMessage("COMPROBANTE NO ENCONTRADO")
                                .setNegativeButton("Reintentar",null)
                                .create().show();

                    }

                } catch (JSONException e) {


                    e.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity_scanner.this);
                    builder.setMessage("Error")
                            .setNegativeButton("Reintentar", null)
                            .create().show();
                }
            }
        };
        //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
        //RechazarPagoRequest rechazarPagoRequest = new RechazarPagoRequest(folio, idSecretaria, responseListener);
        RequestQueue queue = Volley.newRequestQueue(activity_scanner.this);

        //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
        //queue.add(rechazarPagoRequest);

    }




}
