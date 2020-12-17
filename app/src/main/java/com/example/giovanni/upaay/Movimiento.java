package com.example.giovanni.upaay;

import android.graphics.drawable.Drawable;

public class Movimiento {

    private String folio;
    private String fecha;
    private String concepto;
    private Drawable imagen;

    public Movimiento(){
        super();
    }

    public Movimiento(String folio, String fecha, String concepto, Drawable imagen) {
        super();
        this.folio = folio;
        this.fecha = fecha;
        this.concepto = concepto;
        this.imagen = imagen;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }
}
