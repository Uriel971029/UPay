package com.example.giovanni.upaay;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.giovanni.upaay.R;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.parser.Line;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    //listado de imagenes
    public int[] listImg = {
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3,
            R.drawable.slide4,
            R.drawable.slide5,
            R.drawable.slide6,
            R.drawable.slide7,
            R.drawable.slide8,
            R.drawable.slide9
    };

    //listado de titulos
    public String[] listTitles = {

            "¿Como realizar un pago?",
            "Antes de empezar",
            "Importante",
            "Paso 1",
            "Paso 2",
            "Paso 3",
            "Paso 4",
            "Paso 5",
            "Paso 6"

    };


    //listado de descripciones
    public String[] listDescrip = {

            "¡Realizar un pago desde la aplicación en realidad es muy fácil!",
            "Lo primero que debes saber, es que nuestra aplicación se apoya en la plataforma de pagos PayPal por cuestiones de seguridad",
            "Así que debes tener una cuenta, si no la tienes, ¡abrir una es múy fácil además de que te será útil para hacer compras en línea!",
            "Haz click en el módulo de trámites y elige el trámite que deseas pagar, se mostrarán los datos de éste en la parte de abajo",
            "Después, presiona el botón 'generar', el cual te creará tu número de referencia para realizar el pago a la universidad ",
            "Ahora debes presionar el botón PayPal que se encuentra en la parte inferior de la pantalla. Esto te redirigirá a la plataforma de PayPal",
            "En la plataforma de PayPal deberás utilizar los datos de tu cuenta para acceder y observar los detalles de tu pago",
            "Una vez verificados todos los detalles, solo debes finalizar el pago presionando el botón 'continuar'",
            "Finalmente, la aplicación nos generará un comprobante de pago en formato PDF, éste puede ser entregado en ventanillas o escaneado"


    };

    //listado de colores de fondo
    public int[] listColores = {

            Color.rgb(239,85,85),
            Color.rgb(255,195,0),
            Color.rgb(22,155,215),
            Color.rgb(1,188,212),
            Color.rgb(163,204,0),
            Color.rgb(255,187,187),
            Color.rgb(171, 71, 188),
            Color.rgb(0, 48, 135),
            Color.rgb(0, 184, 170)
    };

    public SlideAdapter(Context context) {

        this.context = context;
    }

    @Override
    public int getCount() {
        return listTitles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout)object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        //INICIALIZAMOS LOS RECURSOS
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide, container, false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelayout);
        ImageView imgslide = (ImageView) view.findViewById(R.id.imgslide);
        TextView titleslide = (TextView) view.findViewById(R.id.titleslide);
        TextView descslide = (TextView) view.findViewById(R.id.subtitleslide);

        //INSTANCIAMOS LOS VALORES DE LOS RECURSOS
        layoutslide.setBackgroundColor(listColores[position]);

        //imgslide.setImageResource(listImg[position]);
        Picasso.get().load(listImg[position])
                //fit() NOS PERMITE HACER UN RESIZE DE LA IMÁGEN A LO QUE ESPERA EL CONTENEDOR EN DONDE VA A ESTAR
                .fit()
                //CenterInside() NOS PERMITE MOSTRAR TODA LA IMAGEN DENTRO DEL CONTENEOR AUNQUE AVECES NO ABARCA TODO ESTE
                //CenterCrop POR SU PARTE ABARCA TODO EL CONTENEDOR PERO PUEDE QUE NO SE MUESTRE TODA LA IMAGEN
                //.centerCrop()
                .centerInside()
                .into(imgslide);

        titleslide.setText(listTitles[position]);
        descslide.setText(listDescrip[position]);

        //AGREGAMOS LA VISTA PARA SER MOSTRADA
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }


}
