package com.codeup.demoproject.repos;


import com.codeup.demoproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
    User getUserByEmail(String email);

}
