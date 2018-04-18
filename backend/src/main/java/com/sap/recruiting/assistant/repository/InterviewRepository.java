package com.sap.recruiting.assistant.repository;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    List<Interview> findByCompany(Company company);

    Page<Interview> findByCompany(Company company, Pageable pageable);

    Optional<Interview> findByWxIdAndCompany(String wxId, Company company);
}