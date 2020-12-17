package com.example.giovanni.upaay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.giovanni.upaay.menu_bar.toolbar;


public class fragment_home extends Fragment {

    private TextView fechaAcceso;
    private ImageView buttonTramites, buttonHistorial, buttonMetodos, buttonAyuda, buttonComentarios, buttonInformacion;
    View RootView;
    private menu_bar funcionesMenu = null;
    private Usuario usuario;
    private Bundle bundle;
    private Bundle auxBundle;


    //MAXIMO NUMERO DE RETROCESOS EN LA listaFragments DE FRAGMENTOS
    //listaFragments EN LA QUE VAMOS A ALMACENAR LOS FRAGMENTOS NAVEGADOS
    ArrayList<Fragment> listaFragments = new ArrayList<>();


    @SuppressLint("ValidFragment")
    public fragment_home(menu_bar menu) {
        this.funcionesMenu = menu;
    }

    public fragment_home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RootView=inflater.inflate(R.layout.ventana_home, container, false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);
        fechaAcceso = (TextView) RootView.findViewById(R.id.txtAcceso);
        buttonTramites = (ImageView) RootView.findViewById(R.id.btnTramites);
        buttonHistorial = (ImageView) RootView.findViewById(R.id.btnHistorial);
        buttonMetodos = (ImageView) RootView.findViewById(R.id.btnMetodos);
        buttonAyuda = (ImageView) RootView.findViewById(R.id.btnAyuda);
        buttonComentarios = (ImageView) RootView.findViewById(R.id.btnComentarios);
        buttonInformacion = (ImageView) RootView.findViewById(R.id.btnInformacion);

        bundle = getArguments();
        usuario = (Usuario) bundle.getSerializable("datosUsuario");

        fechaAcceso.setText(getResources().getText(R.string.accesoInfo) + " " + fecha);
        //PRUEBA DE LIBRERÍA PARA MEJORAR LA CARGA DE IMAGENES DENTRO DE LA APP
        Picasso.get().load(R.drawable.realizar_tramite)
                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                .fit()
                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                .centerInside()
                .into(buttonTramites);

        Picasso.get().load(R.drawable.historico)
                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                .fit()
                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                .centerInside()
                .into(buttonHistorial);

        Picasso.get().load(R.drawable.pagar)
                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                .fit()
                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                .centerInside()
                .into(buttonMetodos);

        Picasso.get().load(R.drawable.ayuda)
                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                .fit()
                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                .centerInside()
                .into(buttonAyuda);

        Picasso.get().load(R.drawable.comentarios)
                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                .fit()
                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                .centerInside()
                .into(buttonComentarios);

        Picasso.get().load(R.drawable.informa)
                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                .fit()
                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                .centerInside()
                .into(buttonInformacion);

        buttonTramites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auxBundle = new Bundle();
                auxBundle.putSerializable("datosUsuario", usuario);
                if(funcionesMenu.getTramites() == null){

                    funcionesMenu.setTramites(new fragment_tramites());
                }

                //PASAMOS ARGUMENTOS NECESARIOS
                funcionesMenu.getTramites().setArguments(auxBundle);
                toolbar.setTitle("TRÁMITES");
                funcionesMenu.revisarCarga(funcionesMenu.getTramites(),5);
            }
        });

        buttonHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(funcionesMenu.getHistorial() == null){

                    funcionesMenu.setHistorial(new fragment_historial());
                }

                //PASAMOS ARGUMENTOS NECESARIOS
                funcionesMenu.getHistorial().setArguments(bundle);
                toolbar.setTitle("MOVIMIENTOS");
                funcionesMenu.revisarCarga(funcionesMenu.getHistorial(),6);

            }
        });

        buttonMetodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(funcionesMenu.getMetodos() == null){

                    funcionesMenu.setMetodos(new fragment_metodos());
                }

                toolbar.setTitle("FORMAS DE PAGO");
                funcionesMenu.revisarCarga(funcionesMenu.getMetodos(),10);
            }
        });


        buttonAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(funcionesMenu.getAyuda() == null){

                    funcionesMenu.setAyuda(new fragment_ayuda());
                }

                toolbar.setTitle("AYUDA");
                funcionesMenu.revisarCarga(funcionesMenu.getAyuda(),10);
            }
        });


        buttonComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(funcionesMenu.getComentarios() == null){

                    funcionesMenu.setComentarios(new fragment_comentarios());
                }

                //PASAMOS ARGUMENTOS NECESARIOS
                funcionesMenu.getComentarios().setArguments(bundle);
                toolbar.setTitle("COMENTARIOS Y SUGERENCIAS");
                funcionesMenu.revisarCarga(funcionesMenu.getComentarios(),10);
            }
        });


        buttonInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(funcionesMenu.getInformacion() == null){

                    funcionesMenu.setInformacion(new fragment_informacion());
                }

                toolbar.setTitle("ACERCA DE");
                funcionesMenu.revisarCarga(funcionesMenu.getInformacion(),10);
            }
        });


        //OBTENEMOS EL TOKEN DEL CLIENTE PAERA FUTURA TRANSACCIÓN
        //usuario.getClientToken(getContext());
        return RootView;

    }

    
}
