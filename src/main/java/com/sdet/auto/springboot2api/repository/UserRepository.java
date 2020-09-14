package com.sdet.auto.springboot2api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sdet.auto.springboot2api.model.User;
import org.springframework.stereotype.Repository;

@Repository // first value is model, the long is the primary key from the model.
public interface UserRepository extends JpaRepository<User, Long>{

}
