package com.sap.recruiting.assistant.repository;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

    List<Problem> findByCompany(Company company);
}