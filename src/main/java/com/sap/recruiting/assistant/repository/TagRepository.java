package com.sap.recruiting.assistant.repository;

import com.sap.recruiting.assistant.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Jiaye Wu on 18-3-26.
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByName(String name);
}
