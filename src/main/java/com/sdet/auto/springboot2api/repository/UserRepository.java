package com.sdet.auto.springboot2api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sdet.auto.springboot2api.model.User;
import org.springframework.stereotype.Repository;

@Repository // first value is model, the long is the primary key from the model.
public interface UserRepository extends JpaRepository<User, Long>{
    // this will only find one user due to the unique constraint defined in our User model
    // this code: @Column(name = "USER_NAME", length = 50, nullable = false, unique = true)
    // that's why below we are typing as a User not a list of Users.
    // if method was findByLastname where there can be multiple last names then need to cast as List<User>
    User findByUsername(String username);
}
