package com.sap.recuriting.assistant.repository;

import com.sap.recuriting.assistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String username, String password);
}
