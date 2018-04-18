package com.sap.recruiting.assistant.service;

import com.sap.recruiting.assistant.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jiaye Wu on 18-3-30.
 */
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagRepository getTagRepository() {
        return tagRepository;
    }
}
