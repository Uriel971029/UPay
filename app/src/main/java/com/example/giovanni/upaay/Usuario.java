package com.example.giovanni.upaay;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Usuario extends Alumno implements Serializable{

    private String username;
    private String password;
    //private String idCustomer;
    //TOKEN DE PAYPAL
    private String token;
    private static Usuario usuario;

    private Usuario() {
    }

    public static Usuario getUsuario(){

        if(usuario == null){

            usuario = new Usuario();
        }

        return usuario;
    }


    public static Usuario getUsuario(String matricula, String nombre, String apellidoP, String apellidoM, int cuatrimestre, String grupo, String carrera, String correo, String telefono, String statusAlumno, Drawable imagen, String username, String password, String token) {

        if(usuario == null){

            usuario = new Usuario(matricula, nombre, apellidoP, apellidoM, cuatrimestre, grupo, carrera, correo, telefono, statusAlumno, imagen, username, password, token);
        }

        return usuario;
    }

    private Usuario(String matricula, String nombre, String apellidoP, String getApellidoM, int cuatrimestre, String grupo, String carrera, String correo, String telefono, String statusAlumno, Drawable imagen, String username, String password, String token) {
        super(matricula, nombre, apellidoP, getApellidoM, cuatrimestre, grupo, carrera, correo, telefono, statusAlumno, imagen);
        this.username = username;
        this.password = password;
        //this.idCustomer = idCustomer;
        this.token = token;
    }

    public static void setUsuario(Usuario usuario) {
        Usuario.usuario = usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }




}
