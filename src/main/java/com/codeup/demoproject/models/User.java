package com.codeup.demoproject.models;
import org.javalite.activejdbc.Model;


public class User extends Model{
    static {
        validatePresenceOf("username").message("Please, provide your username");
        validatePresenceOf("email").message("Please, provide your email");
        validateEmailOf("email").message("Enter a valid email");
        validatePresenceOf("password").message("Please, provide your password");

    }
}
