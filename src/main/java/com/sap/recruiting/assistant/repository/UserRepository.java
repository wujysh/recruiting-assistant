package com.sap.recruiting.assistant.repository;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    List<User> findByCompany(Company company);
}
