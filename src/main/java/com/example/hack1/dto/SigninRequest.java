package com.example.hack1.dto;

public class SigninRequest {
    private String username;
    private String password;

    public SigninRequest() {}

    public SigninRequest(String nombreDeUsuario, String password) {
        this.username = nombreDeUsuario;
        this.password = password;}

    public String getUsername() {
        return username;}

    public void setUsername(String username) {
        this.username = username;}

    public String getPassword() {
        return password;}

    public void setPassword(String password) {
        this.password = password;}}