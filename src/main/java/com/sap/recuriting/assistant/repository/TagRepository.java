package com.sap.recuriting.assistant.repository;

import com.sap.recuriting.assistant.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findByName(String name);
}
