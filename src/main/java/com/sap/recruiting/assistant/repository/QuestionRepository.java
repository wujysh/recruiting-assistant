package com.sap.recruiting.assistant.repository;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Question;
import com.sap.recruiting.assistant.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Jiaye Wu on 18-4-9.
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByType(int type);

    Page<Question> findByType(int type, Pageable pageable);

    List<Question> findByTag(Tag tag);

    Page<Question> findByTag(Tag tag, Pageable pageable);

    List<Question> findByCompany(Company company);

    Page<Question> findByCompany(Company company, Pageable pageable);
}
