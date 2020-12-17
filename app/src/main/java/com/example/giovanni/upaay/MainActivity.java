package com.example.giovanni.upaay;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    EditText etUsuario, etContrasenia;
    TextView registrar;
    //ProgressBar progressBar;
    private String correo;
    private Usuario usuario;
    private ImageView logo;
    //VARIABLE QUE VAMOS A UTILIZAR DURANTE TODAS LAS CONEXIONES            LOCAL                       SERVIDOR
    public static final String URL_SERVER = "http://192.168.1.70/UPay/"; //http://192.168.1.70/UPay/ - http://upay-com.sites.stackstaging.com/UPay/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("");
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setContentTitle("Cuenta Activada")
                .setContentText("Su cuenta ha sido activada por un administrador exitosamente")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/

        //progressBar = (ProgressBar) findViewById(R.id.loading);
        //Se recupera el elemento que se requiere ubicado dentro del layout
        etUsuario= (EditText) findViewById(R.id.etusuario);
        etContrasenia= (EditText) findViewById(R.id.etcontrasena);
        logo = (ImageView) findViewById(R.id.logo);

        Picasso.get().load(R.drawable.wolf)
                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                .fit()
                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                .centerInside()
                .into(logo);

        //Función utilizada cuando se registra un usuario y se devuelve a la interfaz de inicio de sesión con msg de retroalimentación
        enviarMensaje();

    }
    //Función para comprobar la conexión a internet obtenida de StackOverFlow
    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
    //Función para comprobar la conexión a una red obtenida de StackOverFlow
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void enviarMensaje(){

        if(this.getIntent().getExtras() != null) {

            Intent intent = getIntent();
            int registroExitoso = intent.getIntExtra("registro",-1);
            //String username = intent.getStringExtra("usuario");
            //POR SI ES UN REGRESO A LA VENTANA DEL LOGIN DESPUÉS DE HABER HECHO UN REGISTRO
            if (registroExitoso == 1) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Su solicitud de registro será evaluada durante las próximas 24 horas")
                        .setPositiveButton("Aceptar", null)
                        .create().show();

            }

            //VAMOS A REGISTRAR AL USUARIO COMO SUBSCRIPTOR PARA COMENZAR A RECIBIR NOTIFICACIONES DE LA APP, EN PRIMERA INSTANCIA PODER SER ACTIVADA SU CUENTA POR UN ADMIN
            //sendRegistrationToServer();



        }
    }

    public void ingresar(View v){

        final String username = etUsuario.getText().toString();
        final String password = etContrasenia.getText().toString();

        if(validar(username, password)) {
            //VERIFICAMOS SI ESTA CONECTADO A UNA RED
            if (isNetDisponible()) {
                //SI HAY CONEXIÓN A INTERNET
                if (isOnlineNet()) {

                    //DIALOGO DE ESPERA
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_loading);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.show();


                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                //PARA Obtener la datos usamos un onjeto Json
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    //RECUPERAMOS DATOS DE ALUMNO

                                    dialog.dismiss();

                                        //RECUPERAMOS TODA LA INFORMACIÓN DEL ALUMNO DE LA BASE DE DATOS
                                        String matricula = jsonResponse.getString("matricula");
                                        String nombre = jsonResponse.getString("nombre");
                                        String apellidoP = jsonResponse.getString("apellidoP");
                                        String apellidoM = jsonResponse.getString("apellidoM");
                                        String carrera = jsonResponse.getString("carrera");
                                        String cuatrimestre = jsonResponse.getString("cuatrimestre");
                                        String grupo = jsonResponse.getString("grupo");
                                        String correo = jsonResponse.getString("correo");
                                        String telefono = jsonResponse.getString("telefono");
                                        String status = jsonResponse.getString("activo");
                                        String user = jsonResponse.getString("username");
                                        String password = "";//jsonResponse.getString("password");
                                        //String idCustomer = jsonResponse.getString("idCustomer");
                                        Drawable drawable = null;
                                        //POR SEGURIDAD VAMOS A SOLICITAR EL TOKEN DE CLIENTE HASTA QUE SEA REALMENTE NECESARIO
                                        String token = "";
                                        //LA CUENTA DE USUARIO A SIDO VALIDADA
                                        if(Integer.parseInt(status) == 1) {
                                            //, idCustomer
                                            usuario = Usuario.getUsuario(matricula, nombre, apellidoP, apellidoM, Integer.parseInt(cuatrimestre), grupo, carrera,correo, telefono, status, drawable, user, password, token);
                                            sendActivity();

                                        }else{
                                            android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                                            builder.setMessage("La cuenta no ha sido validada por un administrador, intetelo más tarde")
                                                    .setPositiveButton("Aceptar",null)
                                                    .create().show();
                                        }


                                } else {

                                    //Descartar el diálogo de progreso
                                    dialog.dismiss();

                                    android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Usuario o contraseña incorrectos")
                                            .setPositiveButton("Reintentar",null)
                                            .create().show();

                                }

                            } catch (JSONException e) {

                                dialog.dismiss();
                                e.printStackTrace();

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Error al iniciar sesión")
                                        .setNegativeButton("Reintentar", null)
                                        .create().show();
                            }
                        }
                    };
                    //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
                    LoginRequest loginRequest = new LoginRequest(username, password,  responseListener);
                    //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
                    //queue.add(loginRequest);
                    VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);


                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("NO HAY CONEXIÓN A INTERNET")
                            .setNegativeButton("ACEPTAR", null)
                            .create().show();
                }


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("NO ESTA CONECTADO A UNA RED")
                        .setNegativeButton("ACEPTAR", null)
                        .create().show();
            }

        }
    }

    public void sendActivity(){

        //SE DEFINE LA VENTANA HACIA DONDE VAMOS A PASAR DESPUÉS DEL LOGEO
        Intent ven = new Intent(MainActivity.this, menu_bar.class);
        //INSERTAMOS LOS DATOS EN ESA ACTIVIDAD PARA PODER COMPARTIRLO DESPUÉS EN OTRO ACTIVITY
        Bundle bundle = new Bundle();
        bundle.putSerializable("datosUsuario", usuario);
        ven.putExtra("Usuario", bundle);
        //RESETEAMOS LOS CAMPOS DE ACCESO POR SEGURIDAD
        etUsuario.setText("");
        etContrasenia.setText("");
        startActivity(ven);
    }

    //FUNCIÓN PARA VALIDAR EL LLENADO DE LOS CAMPOS DE ACCESO PARA EL INICIO DE SESIÓN
    public boolean validar(String user, String pass){

        if (user.isEmpty()){

            etUsuario.setError("Complete los campos por favor");
            etUsuario.requestFocus();
            return false;

        }else if (pass.isEmpty()){

            etContrasenia.setError("Complete los campos por favor");
            etContrasenia.requestFocus();
            return false;
        }

        return true;
    }

    //FUNCIÓN PARA REGISTRAR A UN NUEVO ALUMNO
    public void registro(View v){

            Intent ven=new Intent(this,Registro.class);
            ven.putExtra("finisher", new ResultReceiver(null){

                @Override
                protected void onReceiveResult(int resultCode, Bundle resultData) {
                    MainActivity.this.finish();
                }
            });

            startActivityForResult(ven,1);

    }

    //FUNCIÓN TENTITIVAMENTE INNECESARIA POR AHORA 14/05/2019
    private void sendRegistrationToServer() {


        SharedPreferences preferences = getSharedPreferences("misPreferencias", MODE_PRIVATE);


        //final Usuario usuario = Usuario.getUsuario();
        String token = preferences.getString("id", "");
        String username = preferences.getString("username", "");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //PARA Obtener la datos usamos un objeto Json
                    JSONObject jsonResponse = new JSONObject(response);
                    int success = jsonResponse.getInt("success");

                    if (success == 1) {

                        System.out.println();

                    } else {

                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Ocurrió un error")
                                .setNegativeButton("Reintentar",null)
                                .create().show();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Error de excepción")
                            .setNegativeButton("Reintentar", null)
                            .create().show();
                }
            }
        };
        //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
        SendNotificationRequest notificationRequest = new SendNotificationRequest(token, username,  responseListener);
        //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
        //queue.add(loginRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(notificationRequest);
    }


}
