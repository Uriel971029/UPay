package com.example.giovanni.upaay;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.braintreepayments.api.PaymentMethod;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.io.Serializable;
import java.util.ArrayList;



public class menu_bar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, fragment_ajustes.OnFragmentInteractionListener, fragment_tramites.OnFragmentInteractionListener,
        PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener {



    fragment_home inicio = null;
    fragment_ajustes ajustes = null;
    fragment_tramites tramites = null;
    fragment_historial historial = null;
    fragment_informacion informacion = null;
    fragment_ayuda ayuda = null;
    fragment_comentarios comentarios = null;
    fragment_metodos metodos = null;

    String CURRENT_TAG = "";
    String OLD_TAG = "";
    int CURRENT_FRAGMENT_POSITION = 0;
    static Toolbar toolbar;
    //MAXIMO NUMERO DE RETROCESOS EN LA listaFragments DE FRAGMENTOS
    //listaFragments EN LA QUE VAMOS A ALMACENAR LOS FRAGMENTOS NAVEGADOS
    ArrayList<Fragment> listaFragments = new ArrayList<>();
    //EN CASO DE QUERER DELIMITAR EL TAMAÑO DEL HISTORIAL USESE ESTA VARIABLE Y LOS CASOS EN DONDE SE VE IMPLICADA (ESTAN COMENTADOS)
    //int MAX_HISTORIC = 5;
    private Bundle bundle;
    private Usuario usuario;
    //VARIABLE QUE LE INDICA A LA VARIABLE DEVOLVER RESPUESTA DEL ESCUCHADOR DE BRAINTREE AL FRAGMENTO COMO CONSECUENCIA DEL TERMINO DE UN FLUJO DE PAYPAL
    //private boolean isPaymentMethodNonceReceived = false;
    //private PaymentMethodNonce paymentInformation = null;

    public fragment_home getInicio() {
        return inicio;
    }

    public void setInicio(fragment_home inicio) {
        this.inicio = inicio;
    }

    public fragment_ajustes getAjustes() {
        return ajustes;
    }

    public void setAjustes(fragment_ajustes ajustes) {
        this.ajustes = ajustes;
    }

    public fragment_tramites getTramites() {
        return tramites;
    }

    public void setTramites(fragment_tramites tramites) {
        this.tramites = tramites;
    }

    public fragment_historial getHistorial() {
        return historial;
    }

    public void setHistorial(fragment_historial historial) {
        this.historial = historial;
    }

    public fragment_informacion getInformacion() {
        return informacion;
    }

    public void setInformacion(fragment_informacion informacion) {
        this.informacion = informacion;
    }

    public fragment_ayuda getAyuda() {
        return ayuda;
    }

    public void setAyuda(fragment_ayuda ayuda) {
        this.ayuda = ayuda;
    }

    public fragment_comentarios getComentarios() {
        return comentarios;
    }

    public void setComentarios(fragment_comentarios comentarios) {
        this.comentarios = comentarios;
    }

    public fragment_metodos getMetodos() {
        return metodos;
    }

    public void setMetodos(fragment_metodos metodos) {
        this.metodos = metodos;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bar);

        //SE RECUPERAN LOS DATOS DE LA BD PARA INSERTARLOS EN EL FRAGMENT INICIAL
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("Usuario");
        usuario =(Usuario) bundle.getSerializable("datosUsuario");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //CREAR FUNCION PARA DAR EL SALUDO DE ACUERO A LA HORA
        toolbar.setTitle("Bienvenido(a)" + " " + usuario.getNombre());
        setSupportActionBar(toolbar);


        //EL DRAWER LAYOUT CORRESPONDE A NUESTRO ACTION BAR O MENU DESPLEGABLE, SE LE INSERTA EL CONTENIDO SIMPLEMENTE ACCEDIENDO A SUS PARTES
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //BUSCAMOS LA CABECERA DEL MENU DESPLEGABLE PARA DESPUÉS GESTIONAR SU CONTENIDO
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        TextView txtUsuario = (TextView) hView.findViewById(R.id.Usuario);
        TextView correo = (TextView)hView.findViewById(R.id.Correo);

        String user = usuario.getNombre() +" "+ usuario.getApellidoP() +" "+ usuario.getGetApellidoM();
        txtUsuario.setText(user);
        correo.setText(usuario.getCorreo());

        //PARA ENVIAR DATOS DEL USUARIO AL FRAGMENTO QUE LO REQUIERA
        bundle.putSerializable("datosUsuario", usuario);

        navigationView.setNavigationItemSelectedListener(this);
        //MOSTRAMOS LA PRIMER VISTA (FRAGMENTO) DEL MENU
        inicio = new fragment_home(this);
        inicio.setArguments(bundle);
        revisarCarga(inicio,0);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_obsoleto; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the datosUsuario/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

            //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //AJUSTES DE LA CUENTA DEL ALUMNO
            //PRIMERA VEZ QUE SE CARGA
            if(ajustes == null){
                ajustes = new fragment_ajustes();
            }
            ajustes.setArguments(bundle);
            toolbar.setTitle("AJUSTES DE LA CUENTA");

            revisarCarga(ajustes,4);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //PARTE DEL CÓDIGO RELACIONADA CON LA NAVEGACIÓN ENTRE FRAGMENTOS
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        //NUEVO MODULO CON CUADROS DE IMAGEN ALUDIENDO A LOS TRÁMITES
            if(inicio == null){

                inicio = new fragment_home();
            }

            inicio.setArguments(bundle);
            toolbar.setTitle("INICIO");

            revisarCarga(inicio,1);



        } else if (id == R.id.nav_tramites) {

            //usuario.getClientToken(this);
             
            if(tramites == null){

                tramites = new fragment_tramites();
            }

             tramites.setArguments(bundle);
             toolbar.setTitle("TRÁMITES");

             revisarCarga(tramites,2);

        } else if (id == R.id.nav_estado) {

            if(historial == null){

                historial = new fragment_historial();
            }
            historial.setArguments(bundle);
            toolbar.setTitle("MOVIMIENTOS");

            revisarCarga(historial,3);

        //FALTA PROGRAMAR MODULO
        } else if (id == R.id.nav_salir) {

            confirmar();

        } else if (id == R.id.nav_informacion) {

            if(informacion == null){
                informacion = new fragment_informacion();
            }
            toolbar.setTitle("ACERCA DE");
            revisarCarga(informacion,4);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //EN LUGAR DE REPLACE USAREMOS LAS FUNCIONES SHOW Y HIDDE PARA CONSERVAR EL ESTADO DE LOS FRAGMENTOS
    public void revisarCarga(Fragment newFragment, int status){
        
        /*if(isPaymentMethodNonceReceived){

            bundle.putSerializable("PayPal", (Serializable) paymentInformation);
            tramites.setArguments(bundle);
            
        }*/

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //BUSCAMOS EL FRAGMENTO VISIBLE
        Fragment currentFragment = null;
        //if (status < 4) {
            currentFragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
        /*}else{
            if(inicio == null)
                inicio = new fragment_home();
            currentFragment = inicio;*/
        //}
        //ACTUALIZAMOS CON EL NUEVO FRAGMENTO AL TAG CURRENT
        CURRENT_TAG = newFragment.getClass().getName();
        //PRIMERA CARGA DE FRAGMENTO
        if(status != 0) {
            //SI NO SE TRATA DEL MISMO FRAGMENTO SE PROCEDE
            if (!currentFragment.getTag().equalsIgnoreCase(CURRENT_TAG)) {
                if (newFragment.isAdded()) {

                    fragmentTransaction
                            .hide(currentFragment)
                            .show(newFragment);

                    OLD_TAG = currentFragment.getTag();
                    CURRENT_TAG = newFragment.getClass().getName();

                } else {

                    fragmentTransaction
                            .hide(currentFragment)
                            .add(R.id.contenedor, newFragment, CURRENT_TAG);
                }
            }else{
                //NO HACEMOS EL PROCESO PUES SE SIGUE EN EL MISMO FRAGMENT
            }
            //PRIMER CARGA
        }else{

            CURRENT_TAG = newFragment.getClass().getName();
            OLD_TAG = newFragment.getClass().getName();
            fragmentTransaction.add(R.id.contenedor, newFragment, CURRENT_TAG);

        }

        //EJECUTAS LA TRANSACCIÓN ES DECIR MOSTRAR EL FRAGMENTO AÑADIDO
        fragmentTransaction.commit();
        //ALMACENAMOS EL NUEVO FRAGMENTO SI ES QUE ES DIFERENTE DEL ACTUAL
        if(currentFragment != null) {
            if (!currentFragment.getTag().equalsIgnoreCase(CURRENT_TAG)) {
                agregarlistaFragments(newFragment);
            }
        }else{
            //PRIMER AGREGADO
            agregarlistaFragments(newFragment);
        }

    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(listaFragments.size() > 1) {
            recuperarFragment();
        }else{
            confirmar();
        }
    }

    public void confirmar(){

        //DIALOGO DE CONFIRMACION PARA SALIR
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(menu_bar.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_confirmation, null);

        mBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //LIBERAMOS LA MEMORIA DE LA LISTA
                        destruirLista();
                        menu_bar.super.onBackPressed();
                    }
                }
        );

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //actualizar();
            }
        });
        mBuilder.setView(mView);

        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        Button cancelar = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button aceptar = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if(cancelar != null && aceptar != null){
            cancelar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            aceptar.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    public void recuperarFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String toolbarName = "";
        //SI HAY MAS DE UN FRAGMENTO EN LA LISTA PARA QUE VALGA LA PENA REGRESAR
        if(listaFragments.size() != 0) {

            //SI EL FRAGMENTO ACTUAL ES MENOR QUE EL ÚLTIMO DE LA LISTA QUIERE DECIR QUE ESTA VOLVIENDO A UNA VENTA YA VISITADA
            if(CURRENT_FRAGMENT_POSITION >= listaFragments.size() - 1) {
                //NO SE HA REVASADO EL HISTORIAL DE FRAGMENTOS PERMITIDOS
                //if (CURRENT_FRAGMENT_POSITION != MAX_HISTORIC) {
                //FRAGMENTO A RECUPERAR
                Fragment oldFragment = listaFragments.get(CURRENT_FRAGMENT_POSITION - 1); //getSupportFragmentManager().findFragmentByTag(OLD_TAG);
                //FRAGMENTO A REMPLAZAR
                Fragment currentFragment = listaFragments.get(CURRENT_FRAGMENT_POSITION);//getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);

                //if (listaFragments.contains(oldFragment)) {
                if (currentFragment.isVisible() && oldFragment.isHidden()) {
                    fragmentTransaction
                            .hide(currentFragment)
                            .show(oldFragment);


                    //ACTUALIZAMOS LOS INDICADORES
                    CURRENT_TAG = oldFragment.getTag();
                    OLD_TAG = currentFragment.getTag();
                    //BORRAMOS EL FRAGMENT EN DONDE PRESIONAMOS BACK
                    listaFragments.remove(currentFragment);
                    //DONDE ESTABA EL FRAGMENTO ACTUAL AHORA VA A ESTAR EL FRAGMENTO RECUPERADO
                    //listaFragments.add(CURRENT_FRAGMENT_POSITION, oldFragment);
                    CURRENT_FRAGMENT_POSITION--;
                }

                //FRAGMENTO REPETIDO
            }else{

                //FRAGMENTO A RECUPERAR
                Fragment oldFragment = listaFragments.get(listaFragments.size() - 1);
                //FRAGMENTO A REMPLAZAR SERÍA EL ÚLTIMO DE LA LISTA
                Fragment currentFragment = listaFragments.get(CURRENT_FRAGMENT_POSITION);

                if (currentFragment.isVisible() && oldFragment.isHidden()) {
                    fragmentTransaction
                            .hide(currentFragment)
                            .show(oldFragment);

                    //ACTUALIZAMOS LOS INDICADORES
                    CURRENT_TAG = oldFragment.getTag();
                    OLD_TAG = currentFragment.getTag();
                    //BORRAMOS EL FRAGMENT EN DONDE PRESIONAMOS BACK
                    //listaFragments.remove(currentFragment);
                    //DONDE ESTABA EL FRAGMENTO ACTUAL AHORA VA A ESTAR EL FRAGMENTO RECUPERADO
                    //listaFragments.add(CURRENT_FRAGMENT_POSITION, oldFragment);
                    CURRENT_FRAGMENT_POSITION = listaFragments.size() - 1;
                }
            }


            //ASIGNAMOS NOMBRE AL TOOLBAR
            toolbarName = CURRENT_TAG.substring(36, CURRENT_TAG.length());
            switch (toolbarName){

                case "ajustes":
                    toolbar.setTitle("AJUSTES");
                    break;

                case "ayuda":
                    toolbar.setTitle("AYUDA");
                    break;

                case "comentarios":
                    toolbar.setTitle("COMENTARIOS Y SUGERENCIAS");
                    break;

                case "informacion":
                    toolbar.setTitle("ACERCA DE");
                    break;

                case "home":
                    toolbar.setTitle("INICIO");
                    break;

                case "metodos":
                    toolbar.setTitle("FORMAS DE PAGO");
                    break;

                case "tramites":
                    toolbar.setTitle("TRÁMITES");
                    break;

                case "historial":
                    toolbar.setTitle("MOVIMIENTOS");
                    break;

                default:

                    break;
            }
                fragmentTransaction.commit();

        }
    }


    public void agregarlistaFragments(Fragment nuevo){

        //Fragment nuevo = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);

        if(!listaFragments.isEmpty()) {
            //SI ES MENOR AL NUMERO PERMITIDO DE FRAGMENTOS EN LA LISTA
            //if(listaFragments.size() < MAX_HISTORIC) {

                //SI EL FRAGMENTO AUN NO ESTA EN LA LISTA SE AGREGA
                if (!listaFragments.contains(nuevo)) {

                    listaFragments.add(nuevo);
                    CURRENT_FRAGMENT_POSITION = listaFragments.indexOf(nuevo);

                } else {
                    //NO SE AGREGA EL FRAGMENTO PARA NO REPETIR
                    //SE OBTIENE LA POSICIÓN EN EL ARREGLO DEL FRAGMENTO EN CUESTIÓN
                    CURRENT_FRAGMENT_POSITION = listaFragments.indexOf(nuevo);
                }
            //}else{
            //NO AGREGA NADA
            //}

        }else{
            //AGREGAMOS EL PRIMERO CONTENIDO DE LA LISTA
            listaFragments.add(nuevo);
        }

    }

    public void destruirLista(){

        listaFragments.removeAll(listaFragments);
        //eliminamos la sesión del usuario
        Usuario.setUsuario(null);
    }


        @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {

        //postNonceToServer(paymentMethodNonce);

        if(paymentMethodNonce != null) {
            //isPaymentMethodNonceReceived = true;
            tramites.postNonceToServer(paymentMethodNonce);
            //paymentInformation = paymentMethodNonce;
            //revisarCarga(tramites, 2);
        }

    }


    public void cleanUpFragment(Fragment fragment){
        //reiniciamos el fragmento
        tramites = null;
        tramites = new fragment_tramites();
        //lo recargamos
        revisarCarga(tramites, 2);

    }

    @Override
    public void onCancel(int requestCode) {

        Log.d("code", String.valueOf(requestCode));
    }

    @Override
    public void onError(Exception error) {

        Log.d("error", error.getMessage());
    }

}
