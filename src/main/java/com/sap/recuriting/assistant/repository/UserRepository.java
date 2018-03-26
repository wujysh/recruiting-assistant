package com.sap.recuriting.assistant.repository;

import com.sap.recuriting.assistant.entity.Company;
import com.sap.recuriting.assistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    List<User> findByCompany(Company company);
}
