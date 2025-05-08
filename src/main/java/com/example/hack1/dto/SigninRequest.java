package com.example.hack1.dto;

public class SigninRequest {
    private String email;
    private String password;

    public SigninRequest() {}

    public SigninRequest(String email, String password) {
        this.email = email;
        this.password = password;}

    public String getUsername() {
        return email;}

    public void setUsername(String email) {
        this.email = email;}

    public String getPassword() {
        return password;}

    public void setPassword(String password) {
        this.password = password;}}