package com.sap.recruiting.assistant.repository;

import com.sap.recruiting.assistant.entity.Company;
import com.sap.recruiting.assistant.entity.Question;
import com.sap.recruiting.assistant.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-4-9.
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByType(int type);

    Page<Question> findByTypeOrderByIdDesc(int type, Pageable pageable);

    List<Question> findByTag(Tag tag);

    Page<Question> findByTagOrderByIdDesc(Tag tag, Pageable pageable);

    List<Question> findByCompany(Company company);

    Page<Question> findByCompanyOrderByIdDesc(Company company, Pageable pageable);

    List<Question> findByCompanyAndType(Company company, int type);

    Page<Question> findByCompanyAndTypeOrderByIdDesc(Company company, int type, Pageable pageable);

    List<Question> findByTagAndType(Tag tag, int type);

    Page<Question> findByTagAndTypeOrderByIdDesc(Tag tag, int type, Pageable pageable);

    List<Question> findByCompanyAndTagAndType(Company company, Tag tag, int type);

    Page<Question> findByCompanyAndTagAndTypeOrderByIdDesc(Company company, Tag tag, int type, Pageable pageable);

    Optional<Question> findFirstByTypeAndContentIsLike(int type, String content);
}
