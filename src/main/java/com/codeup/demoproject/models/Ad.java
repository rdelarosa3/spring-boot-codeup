package com.codeup.demoproject.models;
import org.javalite.activejdbc.Model;

public class Ad extends Model {
    static{
        validatePresenceOf("title").message("Please Enter a Title");
        validatePresenceOf("description").message("Please Enter a Description");
        validatePresenceOf("price").message("Please Enter a Price");
        validateNumericalityOf("price").message("Price needs to be a number;");
        validatePresenceOf("location").message("Please Enter a Location");
    }
}
