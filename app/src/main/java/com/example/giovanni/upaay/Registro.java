package com.example.giovanni.upaay;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;


public class Registro extends AppCompatActivity {

    private Toolbar toolbar;

    EditText matricula, nombre, apellidoP , apellidoM, correo, telefono;
    Button btnSiguiente;
    Intent intent;

    public Registro(Registro registro) {

        registro = this;
    }

    public Registro() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);
        getSupportActionBar().setTitle("");

        matricula = (EditText) findViewById(R.id.etmat);
        nombre = (EditText) findViewById(R.id.etnom);
        apellidoP = (EditText) findViewById(R.id.etapellidoP);
        apellidoM = (EditText) findViewById(R.id.etapellidoM);
        correo = (EditText) findViewById(R.id.etcorreo);
        telefono = (EditText) findViewById(R.id.ettelefono);
        //DESHABILITAMOS EL BOTÓN HASTA QUE SE COMPRUEBE LA MATRÍCULA
        btnSiguiente = (Button) findViewById(R.id.btnNext);
        btnSiguiente.setEnabled(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.action_settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }



    //BUSCAMOS LOS DATOS DEL ALUMNO POR MEDIO DE SU MATRICULA
    public void buscar (View v) {

        boolean comprobar;
        comprobar = validar();

        if(comprobar) {

            Response.Listener<String> respoListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean succes = jsonResponse.getBoolean("success");

                        if (succes) {
                            //RECUPERAMOS INFORMACIÓN DEL ALUMNO
                            String matricula = jsonResponse.getString("matriculaAlumno");
                            String name = jsonResponse.getString("nombre");
                            String apeP = jsonResponse.getString("apellidoP");
                            String apeM = jsonResponse.getString("apellidoM");
                            String carrera = jsonResponse.getString("carrera");
                            String cuatrimestre = jsonResponse.getString("cuatrimestre");
                            String grupo = jsonResponse.getString("grupo");
                            String email = jsonResponse.getString("correo");
                            String cel = jsonResponse.getString("telefono");
                            String statusAlumno = jsonResponse.getString("idStatus");

                            //LA PRESENTAMOS EN EL FORMULARIO
                            nombre.setText(name);
                            apellidoP.setText(apeP);
                            apellidoM.setText(apeM);
                            correo.setText(email);
                            telefono.setText(cel);

                            //ENVIAMOS LOAD DATOS QUE FALATAN POR MOSTRAR AL SIGUIENTE ACTIVITY (DATOS ACADÉMICOS)
                            intent = new Intent(Registro.this, RegistroComplemento.class);
                            intent.putExtra("matricula", matricula);
                            intent.putExtra("nombre", name);
                            intent.putExtra("apeP", apeP);
                            intent.putExtra("apeM", apeM);
                            intent.putExtra("correo", email);
                            intent.putExtra("telefono", cel);


                            intent.putExtra("carrera", carrera);
                            intent.putExtra("cuatrimestre", cuatrimestre);
                            intent.putExtra("grupo", grupo);
                            intent.putExtra("statusAlumno", statusAlumno);
                            //PARA terminarRegistro CON LA ACTIVIDAD CUANDO SE COMPLETE EL REGISTRO EN LA SIGUIENTE ACTIVIDAD
                            intent.putExtra("finisher", new ResultReceiver(null){
                                @Override protected void onReceiveResult(int resultCode, Bundle resultData) {
                                    terminarRegistro();
                                }
                            });
                            
                            //HABILITAMOS EL BOTÓN PARA PODER PASAR A COMPLETAR EL REGISTRO DE USUARIO
                            btnSiguiente.setEnabled(true);

                            btnSiguiente.setBackground(getResources().getDrawable(R.drawable.rounded_success));
                            btnSiguiente.setTextColor(getResources().getColor(R.color.success));


                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                            builder.setMessage("No se encontró al alumno en la base de datos")
                                    .setPositiveButton("Reintentar", null)
                                    .create().show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };


            searchUsuarioRequest search = new searchUsuarioRequest(matricula.getText().toString(), respoListener);
            //RequestQueue queue = Volley.newRequestQueue(this);
            //queue.add(search);
            VolleySingleton.getInstance(this).addToRequestQueue(search);


        }

    }


    //ENVÍAMOS A LA SIGUIENTE ACTIVITY PARA QUE COMPLETE EL REGISTRO
    public void completarRegistro (View v) {

        //INCLUIR EXTRAS EN EL INTENT PARA LLENAR EL FORMULARIO CON LOS DATOS ENCONTRADOS EN CASO DE MATRICULA VÁLIDA
        startActivityForResult(intent, 1);
    }

    public boolean validar() {

        matricula.setError(null);

        //OBTENEMOS LA MATRICULA Y LE QUITAMOS ESPACIOS EN BLANCO A LOS LADOS POR SI LOS TUVIESE
        String matriculaAlumno = matricula.getText().toString().trim();
        int espacio = 0;

        //LA MATRICULA NO DEBE TENER ESPACIOS ENTRE ELLA
        espacio = matriculaAlumno.indexOf(" ");
        if (TextUtils.isEmpty(matriculaAlumno) || (matriculaAlumno.length() != 10) || (espacio != -1)) {
            matricula.setError("La matrícula debe contener 10 caracteres válidos");
            matricula.requestFocus();
            return false;

        }

        if (!validarMatricula(matriculaAlumno)){
            matricula.setError("Formato de matrícula invalido (AAAA/123456)");
            matricula.requestFocus();
        }

        return true;
    }

    private boolean validarMatricula(String matricula){

        String newMatricula;
        String LETRAS;
        String NUMEROS;

        newMatricula = matricula.toLowerCase();

        LETRAS = newMatricula.substring(0,4); 
        NUMEROS = newMatricula.substring(5, 10);

        if (LETRAS.matches(".*[0-9].*")){

            return false;

        }else if(NUMEROS.matches(".*[a-z].*")){

            return false;

        }
        return true;
    }

    public void cancelar (View view){

        finish();

    }

    public void terminarRegistro(){

        //CERRAMOS EL ACTIVITY DE LA PRIMER PARTE DEL REGISTRO
        Registro.this.finish();
        //CERRAMOS LA PRIMER VISTA DE INICIO DE SESIÓN YA QUE NO SE USO (Y ADEMAS CREAMOS OTRA AL TERMINAR EL REGISTRO CON DATOS DIFERENTES)
        ((ResultReceiver)getIntent().getParcelableExtra("finisher")).send(1, new Bundle());

    }

    }

