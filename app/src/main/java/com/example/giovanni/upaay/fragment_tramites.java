package com.example.giovanni.upaay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.braintreepayments.api.models.PayPalRequest.USER_ACTION_COMMIT;
import static com.example.giovanni.upaay.MainActivity.URL_SERVER;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_tramites.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_tramites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_tramites extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /* ----------------------------- VARIABLES PARA IMPLEMENTACIÒN DE SISTEMA DE PAGOS BrainTree SDK ---------------------------------------------------*/
    private String API_CHECK_OUT = "http://upay-com.sites.stackstaging.com/braintree/checkout.php";
    private static final int REQUEST_CODE = 1234;
    //private String amount;
    HashMap<String, String> paramsHash;
    //DIALOGO DE ESPERA
    private Dialog loading = null;
    //, campoClave
    private EditText campoReferencia, campoConcepto, campoImporte, campoDescripcion;
    private FrameLayout mVista;
    private Usuario usuario;
    /* ----------------------------- VARIABLES PARA IMPLEMENTACIÒN DE SISTEMA DE PAGOS BrainTree SDK ---------------------------------------------------*/


    ListView lv;
    //en este array se va a guardar toda la lista de trámites
    ArrayList<String> lista;
    ArrayAdapter<String> adaptador;
    Spinner Spntramites;
    ImageView imagen;
    ImageButton btnPagar;
    Button btnReferencia;

    //String claveTramite="";
    String conceptoTramite = "";
    Double importeTramite = 0.0;
    String descripcionTramite = "";

    //Objeto para generar el comprobante del trámite en formato PDF
    TemplatePDF templatePDF;

    View RootView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public fragment_tramites() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_tramites.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_tramites newInstance(String param1, String param2) {
        fragment_tramites fragment = new fragment_tramites();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.ventana_tramites, container, false);

        templatePDF = new TemplatePDF(this.getActivity().getApplicationContext());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date date = new Date();
        String fecha = dateFormat.format(date);
        //RECUPERAMOS LOS DATOS DEL USUARIO
        Bundle bundle = getArguments();
        //VARIABLE CARGADA CON TODOS LOS DATOS DEL USUARIO + TOKEN DE ACCESO A LA PLATAFORMA DE PAGOS
        usuario = (Usuario) bundle.getSerializable("datosUsuario");

        Spntramites = (Spinner) RootView.findViewById(R.id.listaTramites);
        //campoClave = (TextView) RootView.findViewById(R.id.txtClave);
        campoConcepto = (EditText) RootView.findViewById(R.id.edtConcepto);
        campoImporte = (EditText) RootView.findViewById(R.id.edtImporte);
        campoDescripcion = (EditText) RootView.findViewById(R.id.edtDescripcion);
        campoReferencia = (EditText) RootView.findViewById(R.id.edtReferencia);
        //campoFecha = (EditText) RootView.findViewById(R.id.edtFecha);
        //campoNombre = (EditText) RootView.findViewById(R.id.edtNombre);
        //campoMatricula = (EditText) RootView.findViewById(R.id.edtMatricula);


        //campoFecha.setText(fecha);
        //campoNombre.setText(usuario.getNombre() + " " + usuario.getApellidoP() + " " + usuario.getGetApellidoM());
        //campoMatricula.setText(usuario.getMatricula());

        mVista = (FrameLayout) RootView.findViewById(R.id.frameTramites);

        btnPagar = (ImageButton) RootView.findViewById(R.id.button);
        btnReferencia = (Button) RootView.findViewById(R.id.button6);

        imagen = (ImageView) RootView.findViewById(R.id.imageView2);


        loading = new Dialog(getContext());
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.dialog_loading);
        loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //LLENAR LA LISTA DE TRAMITES DISPONIBLES
        lista = Llenarlista();
        adaptador = new ArrayAdapter<>(this.getContext(), R.layout.textview_spinner, lista);
        adaptador.setDropDownViewResource(R.layout.textview_spinner);
        Spntramites.setAdapter(adaptador);
        Spntramites.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String concepto = Spntramites.getSelectedItem().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //PARA Obtener la datos usamos un onjeto Json
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

                                //RECUPERAMOS TODA LA INFORMACIÓN DEL ALUMNO DE LA BASE DE DATOS
                                //claveTramite = jsonResponse.getString("clave");
                                conceptoTramite = jsonResponse.getString("concepto");
                                importeTramite = jsonResponse.getDouble("importe");
                                descripcionTramite = jsonResponse.getString("descripcion");

                                //ASIGNAMOS EL VALOR A LOS CAMPOS CORRESPONDINTES CON LA INFORMACIÓN DEL TRÁMITE
                                //campoClave.setText(claveTramite);
                                campoConcepto.setText(conceptoTramite);
                                campoImporte.setText(String.valueOf(importeTramite));
                                campoDescripcion.setText(descripcionTramite);


                            } else {

                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                                builder.setMessage("Ocurrió un error al momento de intentar recuperar los datos del trámite")
                                        .setNegativeButton("Cerrar", null)
                                        .create().show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
                TramitesRequest tramitesRequest = new TramitesRequest(concepto, responseListener);
                //RequestQueue queue = Volley.newRequestQueue(getContext());
                VolleySingleton.getInstance(getContext()).addToRequestQueue(tramitesRequest);
                //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
                //queue.add(tramitesRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        //INICIALIMOS EL PROCESO DEL PAGO PAYPAL
        /*Intent intent = new Intent(this.getContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);*/

        //FUNCIÓN A EJECUTAR CUANDO SE LLEVA A CABO UNA OPERACIÓN
        btnPagar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.show();

                if (validar()) {

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get("http://jobbox-com.stackstaging.com/App/API/braintree/main.php", new TextHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String clientToken) {

                            try {

                                BraintreeFragment braintreeFragment = BraintreeFragment.newInstance(getActivity(), clientToken);

                                PayPalRequest request = new PayPalRequest(String.valueOf(importeTramite))
                                        .currencyCode("MXN")
                                        .landingPageType(PayPalRequest.LANDING_PAGE_TYPE_LOGIN)
                                        .shippingAddressRequired(true)
                                        .intent(PayPalRequest.INTENT_SALE);
                                PayPal.requestOneTimePayment(braintreeFragment, request);
                                loading.dismiss();
                                campoReferencia.setText("");


                            } catch (InvalidArgumentException e) {

                                loading.dismiss();
                                Log.d("exception", e.getMessage());
                                Toast.makeText(getActivity().getApplicationContext(), "Ocurrió un problema con el token de autenticación de cliente", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            loading.dismiss();
                            Log.d("***", "onFailure - client-token");
                        }

                    });
                    //loading.dismiss();
                } else {

                    loading.dismiss();
                    Toast.makeText(getContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();
                }

            }

        });

        btnReferencia.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String matricula = usuario.getMatricula().substring(0, 4);
                String seriado = "";
                String referencia = "";

                for (int i = 0; i < 20; i++) {

                    seriado = seriado + (int) (Math.random() * 9) + 1;
                }

                referencia = matricula + seriado;
                campoReferencia.setText(referencia);
            }
        });

        //ESTABLECEMOS LA FOTO DE PERFIL DEL USUARIO SI ES QUE YA LA DETERMINO
        //recuperarFoto();

        return RootView;
    }

    private void submitPayment() {

        loading.show();

        if (validar()) {
            try {

                BraintreeFragment mBraintreeFragment = BraintreeFragment.newInstance(getActivity(), usuario.getToken());

                //ENVIAMOS LA SOLICITUD DE LA TRANSACCIÓN AL SERVIDOR CON EL TOKEN QUE PREVIAMENTE GENERAMOS
                PayPalRequest request = new PayPalRequest(String.valueOf(importeTramite))
                        .currencyCode("MXN")
                        .landingPageType(PayPalRequest.LANDING_PAGE_TYPE_LOGIN)
                        //OBTENER LA ADDRESS
                        .shippingAddressRequired(true)
                        .intent(PayPalRequest.INTENT_SALE);
                PayPal.requestOneTimePayment(mBraintreeFragment, request);

            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            }


        } else {

            loading.dismiss();
            Toast.makeText(getContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();
        }

        loading.dismiss();

    }


    //METODO UTILIZADO CUANDO SE TIENE SU PROPIO BACKEND EN EL SERVIDOR Y SE TRABAJA CON BREINTREE Y NO EL SDK DE PAYPAL
    public void postNonceToServer(PaymentMethodNonce paymentMethodNonce) {


        String nonce = paymentMethodNonce.getNonce();
        if (paymentMethodNonce instanceof PayPalAccountNonce) {
            PayPalAccountNonce payPalAccountNonce = (PayPalAccountNonce) paymentMethodNonce;

            //PREPARAMOS LOS VALORES A ENVIAR AL SERVIDOR

            PostalAddress shippingAddress = payPalAccountNonce.getShippingAddress();


            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("payment_method_nonce", nonce);
            params.put("amount", importeTramite);
            params.put("first_name", payPalAccountNonce.getFirstName());
            params.put("lastname", payPalAccountNonce.getLastName());
            params.put("company", "Universidad Politécnica del Estado de Morelos");
            params.put("street", shippingAddress.getStreetAddress());
            params.put("extended_address", "");
            params.put("locality", shippingAddress.getLocality());
            params.put("region", shippingAddress.getRegion());
            params.put("postal_code", shippingAddress.getPostalCode());
            params.put("description", "Pago por " + conceptoTramite + " desde la aplicación UPay");


            client.post("http://jobbox-com.stackstaging.com/App/API/braintree/checkout.php", params, new TextHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("***", "checkout response: " + responseString);


                    Snackbar.make(mVista, "¡Transacción realizada exitosamente!", Snackbar.LENGTH_SHORT).show();
                    ((menu_bar) getActivity()).cleanUpFragment(fragment_tramites.this);
                    campoReferencia.setText("");
                    loading.dismiss();
                    registrarTransaccion();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    Snackbar.make(mVista, "¡Ocurrió un error con la transacción!", Snackbar.LENGTH_SHORT).show();
                    Log.d("***", "onFailure - checkout");
                    loading.dismiss();

                }

            });

        }

        loading.dismiss();
    }


    private void sendPayment() {

        //loading.show();

        // Solicitar una respuesta de cadena de la URL proporcionada.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("INFORMATION_PAYMENT", response.toString());
                //Toast.makeText(getContext(), "¡Transacción realizada exitosamente!", Toast.LENGTH_SHORT).show();
                Snackbar.make(mVista, "¡Transacción realizada exitosamente!", Snackbar.LENGTH_SHORT).show();
                campoReferencia.setText("");

                registrarTransaccion();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("ERROR_PAYMENT", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (paramsHash == null)
                    return null;
                Map<String, String> params = new HashMap<>();
                for (String key : paramsHash.keySet()) {
                    //OBTENEMOS LAS LLAVES DEL MAPA Y LO RECORREMOS SECUENCIALMENTE Y MIENTRAS RECORREMOS ASIGNAMOS EL VALOR DE CADA LLAVE PARA FORMAR EL MAPA
                    params.put(key, paramsHash.get(key));

                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        //Agregamos a la cola de peticiones el Request que creamos
        //queue.add(stringRequest);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }


    public void registrarTransaccion() {


        //datosAlumno = getActivity().getIntent();
        //OBTENEMOS LOS DATOS DEL ALUMNO
        final String matriculaAlumno = usuario.getMatricula();
        String nombreAlumno = usuario.getNombre();
        String apellidoP = usuario.getApellidoP();
        String apellidoM = usuario.getGetApellidoM();
        final String nombreCompleto = nombreAlumno + " " + apellidoP + " " + apellidoM;


        //FOLIO DEL COMPROBANTE
        final String[] folio = {""};

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String fecha = dateFormat.format(date);

        loading.show();
        //SE REGISTRA LA OPERACION EN LA BD
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    //PARA Obtener la datos usamos un objeto Json
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        //EL TRAMITE SE REGISTRO EN LA BITÀCORA DE LA BD DE FORMA ÈXITOSA
                        folio[0] = jsonResponse.getString("last_id");
                        //GENERAMOS EL PDF
                        GeneratePDF(matriculaAlumno, nombreCompleto, fecha, folio[0]);
                        //loading.dismiss();

                    } else {


                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                        builder.setMessage("Ocurrió un error al momento de intentar registrar los datos del trámite")
                                .setNegativeButton("Cerrar", null)
                                .create().show();

                    }

                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        };

        //enviamos los datos a la clase LoginRequest en la cuál se ejecutará del lado del servidor la solicitud
        //Integer.parseInt(claveTramite)
        RegisterTramiteRequest registerTramiteRequest = new RegisterTramiteRequest(fecha, conceptoTramite, matriculaAlumno, responseListener);
        //RequestQueue queue = Volley.newRequestQueue(getContext());
        //Se envía el objeto LoginRequest cargado con los datos a la cola de peticiones del servidor
        //queue.add(registerTramiteRequest);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(registerTramiteRequest);
    }


    public void GeneratePDF(String matriculaAlumno, String nombreCompleto, String fecha, String folio) {

        String[] header = {"Matricula", "Nombre", "Carrera"};

        boolean pago_realizado = true;

        //convertir imagen
        imagen.buildDrawingCache();
        Bitmap bmap = imagen.getDrawingCache();

        //GENERAMOS EL PDF
        templatePDF = new TemplatePDF(getActivity().getApplicationContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("COMPROBANTE DE PAGO", "FICHA DE PAGO", "Matricula: " + matriculaAlumno);
        //INSERTAR LOGO UPEMOR
        templatePDF.createImage(getActivity().getApplicationContext(), bmap);
        templatePDF.insertImage();

        templatePDF.addTitles("UNIVERSIDAD POLITÉCINICA DEL ESTADO DE MORELOS", "Alumno: " + nombreCompleto, " " + fecha);
        templatePDF.createTable(header, obtenerDatosUsuario());

        //RECUPERAMOS LOS DATOS A MOSTRAR
        String datosOperacionPDF = "";

        datosOperacionPDF = "Folio: " + folio + "     Concepto: " + conceptoTramite + "      Importe: $" + importeTramite;
        //AGREGAR AL PDF LOS DATOS DE LA OPERACION
        templatePDF.addParagraph(datosOperacionPDF);

        //PEGAMOS EL CÓDIGO QR PERO ANTES RECOPILAMOS LOS DATOS QUE ESTE VA A TENER
        Bitmap QR;
        String text2QR = "Folio: " + folio + "   Trámite: " + conceptoTramite + "   Importe: " + importeTramite + "   Matricula-Alumno: " + matriculaAlumno;
        text2QR = text2QR.trim();

        GeneratorQR generatorActivity = new GeneratorQR();
        QR = generatorActivity.generarQR(text2QR);

        //CREAMOS LA IMAGEN COMO RECURSO DENTRO DEL DISPOSITIVO EN EL STORAGE
        templatePDF.createImage(getActivity().getApplicationContext(), QR);
        //INSERTAMOS LA IMAGEN EN EL DOCUMENTO PDF
        templatePDF.insertImage();

        templatePDF.closeDocument();
        loading.dismiss();
        //LLAMAMOS AL METODO PARA VISUALIZAR EL PDF
        pdfView();

    }

    //MODULO DE PRUEBA PARA TERMINAR EL PROCESO PAYPAL
    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public ArrayList<String> Llenarlista() {

        //Objeto que hará la solicitud
        //RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = URL_SERVER + "RecuperarCatalogo.php";
        final ArrayList<String> listaTramites = new ArrayList<>();

        loading.show();
        // Solicitar una respuesta de cadena de la URL proporcionada.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //loading.dismiss();
                //transformamos el string response en una lista que después insertamos en listaTramites
                listaTramites.addAll(Arrays.asList(response.split(" ")));
                //Notifica al adaptador que la información que contiene ha cambiado para que este refleje los cambios
                adaptador.notifyDataSetChanged();
                loading.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //loading.dismiss();

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setMessage("Ocurrió un error")
                        .setNegativeButton("Ok", null)
                        .create().show();
            }
        });

        //Agregamos a la cola de peticiones el Request que creamos
        //queue.add(stringRequest);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
        return listaTramites;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void pdfView() {
        templatePDF.viewPDF();
    }

    private ArrayList<String[]> obtenerDatosUsuario() {


        ArrayList<String[]> rows = new ArrayList<>();
        //ENVIAMOS LOS CAMPOS SOLICITADOS POR EL HEADER
        rows.add(new String[]{usuario.getMatricula(), usuario.getNombre(),
                usuario.getCuatrimestre() + " " + usuario.getGrupo() + " " + usuario.getCarrera()});
        return rows;
    }

    public void enviar_mensaje(boolean pagado) {

        String Mensaje = "Pago Realizado";

        if (pagado == true) {
            Toast.makeText(this.getActivity().getApplicationContext(), Mensaje, Toast.LENGTH_SHORT).show();

        } else {

            Mensaje = "Saldo insuficiente";
            Toast.makeText(this.getActivity().getApplicationContext(), Mensaje, Toast.LENGTH_SHORT).show();
        }
    }


    public boolean validar() {

        campoReferencia.setError(null);

        //OBTENEMOS LA REFERENCIA DE PAGO Y LE QUITAMOS ESPACIOS EN BLANCO A LOS LADOS POR SI LOS TUVIESE
        String referencia = campoReferencia.getText().toString().trim();
        int espacio = 0;

        //LA REFERENCIA NO DEBE TENER ESPACIOS ENTRE ELLA, EL NUMERO DE CARACTERES ESTA BASADO EN YUNA MIESTRA DE LA ULTIMA REINSCRIPCION EN EL 2018
        espacio = referencia.indexOf(" ");
        if (TextUtils.isEmpty(referencia) || (referencia.length() != 24) || (espacio != -1)) {
            campoReferencia.setError("Primero genere una referencia por favor");
            campoReferencia.requestFocus();
            return false;

        }

        if (!validarReferencia(referencia)) {
            campoReferencia.setError("Formato de referencia inválido");
            campoReferencia.requestFocus();
            return false;

        }

        return true;
    }

    public boolean validarReferencia(String referencia) {

        final String LETRAS;
        final String NUMEROS;

        LETRAS = referencia.substring(0, 4);
        NUMEROS = referencia.substring(4, 23);

        if (LETRAS.matches(".*[0-9].*")) {

            return false;

        } else if (NUMEROS.matches(".*[a-z].*")) {

            return false;

        }
        return true;
    }


}
