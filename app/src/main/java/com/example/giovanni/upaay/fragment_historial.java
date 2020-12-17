package com.example.giovanni.upaay;

import android.app.ProgressDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class fragment_historial extends Fragment {

    TextView campoFecha;
    ImageView imageView;

    ListView lv;
    ArrayList<Movimiento> lista;
    adapterMovimiento adaptador;
    View RootView;
    private Bundle bundle;
    private Usuario usuario;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RootView=inflater.inflate(R.layout.ventana_historial, container, false);
        lv = (ListView)RootView.findViewById(R.id.lvRegistros);
        campoFecha = (TextView) RootView.findViewById(R.id.txtFecha);
        imageView = (ImageView) RootView.findViewById(R.id.imageView);
        Bundle bundle = getArguments();
        //VARIABLE CARGADA CON TODOS LOS DATOS DEL USUARIO + TOKEN DE ACCESO A LA PLATAFORMA DE PAGOS
        usuario = (Usuario) bundle.getSerializable("datosUsuario");

        lista = consultar();
        adaptador = new adapterMovimiento(getActivity(), lista);
        lv.setAdapter(adaptador);


        return RootView;
    }




    public ArrayList<Movimiento> consultar() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        String matricula = usuario.getMatricula();
        final ArrayList<Movimiento> listaMovimientos = new ArrayList<>();

        campoFecha.setText(fecha);

        final ProgressDialog loading = new ProgressDialog(getActivity(), R.style.AppTheme_Dark_Dialog);
        loading.setTitle("Obteniendo sus movimientos . . .");
        loading.setMessage("Espere por favor . . .");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.show();

        //CONSULTAR LA INFORMACIÒN DE LA BD PARA LLENAR EL ListView
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //PARA Obtener la datos usamos un objeto Json
                    JSONArray jsonResponse = new JSONArray(response);
                    //
                    listaMovimientos.addAll(parseJson(jsonResponse));
                    //boolean success = jsonResponse.getBoolean("success");

                    //if(success){

                        //listaMovimientos.add("Folio: 00000"+jsonResponse.getString("folio")+"    Tipo de operación: "+
                        //jsonResponse.getString("concepto")+ "   Fecha: "+ jsonResponse.getString("fecha"));
                        //listaMovimientos.addAll(Arrays.asList(jsonResponse.getJSONArray("results").toString()));
                    adaptador.notifyDataSetChanged();
                    loading.dismiss();


                    /*}else{

                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(getContext());
                        builder.setMessage("Ocurrió un error al momento de consultar los movimientos del alumno")
                                .setNegativeButton("Cerrar",null)
                                .create().show();

                    }*/

                } catch (JSONException e) {

                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        };

        //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
        ListaTramitesRequest listaTramitesRequest = new ListaTramitesRequest(matricula, responseListener);
        //RequestQueue queue = Volley.newRequestQueue(getContext());
        VolleySingleton.getInstance(getContext()).addToRequestQueue(listaTramitesRequest);
        //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
        //queue.add(listaTramitesRequest);

        return listaMovimientos;

    }

    public ArrayList<Movimiento> parseJson(JSONArray jsonArray){
        // Variables locales
        ArrayList<Movimiento> movimientos = new ArrayList();

        for(int i=0; i<jsonArray.length(); i++){

            try {
                JSONObject objeto= jsonArray.getJSONObject(i);

                Movimiento movimiento = new Movimiento(
                        objeto.getString("folio"),
                        objeto.getString("fecha"),
                        objeto.getString("concepto"),
                        getContext().getDrawable(R.drawable.credit_card));


                movimientos.add(movimiento);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return movimientos;
    }

    public void salir(View v){

        this.getActivity().finish();
    }

    public void onPreStartConnection() {
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    public void onConnectionFinished() {
        getActivity().setProgressBarIndeterminateVisibility(false);
    }

    public void onConnectionFailed(String error) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

}
