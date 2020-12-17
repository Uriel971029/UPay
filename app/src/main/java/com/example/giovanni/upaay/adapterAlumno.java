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

public class adapterAlumno extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Alumno> items;

    public adapterAlumno(Activity activity, ArrayList<Alumno> items) {
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

    public void addAll(ArrayList<Alumno> Alumno) {
        for (int i = 0; i < Alumno.size(); i++) {
            items.add(Alumno.get(i));
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
            v = inf.inflate(R.layout.item_alumno, null);
        }

        //OBTENEMOS UN OBJETO Alumno DE TODO EL ARREGLO DE AlumnoS
        Alumno alum = items.get(position);

        TextView nombreAlumno = (TextView) v.findViewById(R.id.alumno);
        nombreAlumno.setText(alum.getNombre());

        TextView matriculaAlumno = (TextView) v.findViewById(R.id.matricula);
        matriculaAlumno.setText(alum.getMatricula());

        TextView cuatrimestreAlumno = (TextView) v.findViewById(R.id.cuatrimestre);
        cuatrimestreAlumno.setText(String.valueOf(alum.getCuatrimestre()));

        TextView grupoAlumno = (TextView) v.findViewById(R.id.grupo);
        grupoAlumno.setText(alum.getGrupo());

        TextView carreraAlumno = (TextView) v.findViewById(R.id.carrera);
        carreraAlumno.setText(alum.getCarrera());

        ImageView imagen = (ImageView) v.findViewById(R.id.imageView);
        imagen.setImageDrawable(alum.getImagen());

        return v;
    }


}
