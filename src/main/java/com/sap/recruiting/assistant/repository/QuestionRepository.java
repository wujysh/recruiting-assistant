package com.sap.recruiting.assistant.repository;

import com.sap.recruiting.assistant.entity.Question;
import com.sap.recruiting.assistant.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Jiaye Wu on 18-4-9.
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByType(int type);

    List<Question> findByTag(Tag tag);
}
