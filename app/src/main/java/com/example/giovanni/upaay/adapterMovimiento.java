package com.example.giovanni.upaay;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adapterMovimiento extends BaseAdapter {


    protected Activity activity;
    protected ArrayList<Movimiento> items;

    public adapterMovimiento(Activity activity, ArrayList<Movimiento> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Movimiento> Movimiento) {
        for (int i = 0; i < Movimiento.size(); i++) {
            items.add(Movimiento.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //SE EJECUTA CUANDO SE LLAMA EL METODO DataSentChange
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            //INFLAMOS EL MODELO QUE CRAMOS PARA CADA ITEM DE LA LISTA
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_movimiento, null);
        }

        //OBTENEMOS UN OBJETO MOVIMIENTO DE TODO EL ARREGLO DE MOVIMIENTOS
        Movimiento mov = items.get(position);

        TextView nombreMovimiento = (TextView) v.findViewById(R.id.movimiento);
        nombreMovimiento.setText(mov.getConcepto());

        TextView folioMovimiento = (TextView) v.findViewById(R.id.folio);
        folioMovimiento.setText(mov.getFolio());

        TextView fechaMovimiento = (TextView) v.findViewById(R.id.fecha);
        fechaMovimiento.setText(mov.getFecha());

        ImageView imagen = (ImageView) v.findViewById(R.id.imageView);
        imagen.setImageDrawable(mov.getImagen());

        return v;
    }
}
