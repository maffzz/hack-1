package com.example.hack1.dto;

public class SigninRequest {
    private String nombreDeUsuario;
    private String password;

    public SigninRequest() {}

    public SigninRequest(String nombreDeUsuario, String password) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.password = password;}

    public String getNombreDeUsuario() {
        return nombreDeUsuario;}

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;}

    public String getPassword() {
        return password;}

    public void setPassword(String password) {
        this.password = password;}}