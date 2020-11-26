package com.codeup.demoproject.models;

import org.javalite.activejdbc.Model;

import java.io.Serializable;

public class Category extends Model implements Serializable {
    private long id;
    private String title;
}


