package com.codeup.demoproject.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false,length = 100,unique = true)
    private String email;

    @Column(nullable = false,length = 50,unique = true)
    private String username;

    @Column(nullable = false,length = 100)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Post> posts;

    
    public User(){}
    //read
    public User(long id, String email,String username, String password){
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    //create
    public User(String email,String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }
    // security
    public User(User copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        email = copy.email;
        username = copy.username;
        password = copy.password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
