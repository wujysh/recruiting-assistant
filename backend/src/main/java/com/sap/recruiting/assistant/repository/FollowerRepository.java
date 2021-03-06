package com.sap.recruiting.assistant.repository;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
public interface FollowerRepository extends JpaRepository<Follower, Integer> {

    List<Follower> findByCompany(Company company);

    Page<Follower> findByCompany(Company company, Pageable pageable);

    Optional<Follower> findByWxId(String wxId);
}