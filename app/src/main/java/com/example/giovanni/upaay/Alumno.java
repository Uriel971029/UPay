package com.example.giovanni.upaay;

import android.graphics.drawable.Drawable;


import java.io.Serializable;

public class Alumno implements Serializable {

    private String matricula;
    private String nombre;
    private String apellidoP;
    private String getApellidoM;
    private int cuatrimestre;
    private String grupo;
    private String carrera;
    private String correo;
    private String telefono;
    private String statusAlumno;
    private Drawable imagen;

    public Alumno() {

        super();
    }

    public Alumno(String matricula, String nombre, String apellidoP, String getApellidoM, int cuatrimestre, String grupo, String carrera, String correo, String telefono, String statusAlumno, Drawable imagen) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.getApellidoM = getApellidoM;
        this.cuatrimestre = cuatrimestre;
        this.grupo = grupo;
        this.carrera = carrera;
        this.correo = correo;
        this.telefono = telefono;
        this.statusAlumno = statusAlumno;
        this.imagen = imagen;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getGetApellidoM() {
        return getApellidoM;
    }

    public void setGetApellidoM(String getApellidoM) {
        this.getApellidoM = getApellidoM;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getStatusAlumno() {
        return statusAlumno;
    }

    public void setStatusAlumno(String statusAlumno) {
        this.statusAlumno = statusAlumno;
    }

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }
}
