package com.sap.recruiting.assistant.service;

import com.sap.recruiting.assistant.repository.FollowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jiaye Wu on 18-4-17.
 */
@Service
public class FollowerService {

    private final FollowerRepository followerRepository;

    @Autowired
    public FollowerService(FollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
    }

    public FollowerRepository getFollowerRepository() {
        return followerRepository;
    }
}
