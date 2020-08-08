package com.dream.store;

import com.dream.model.Candidate;
import com.dream.model.Post;
import com.dream.model.User;
import com.dream.store.Store;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StoreStub implements Store {
    private Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public Collection<Post> findAllPosts() {
        return null;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return null;
    }

    @Override
    public void save(Post post) throws SQLException {

    }

    @Override
    public void save(Candidate candidate) throws SQLException {

    }

    @Override
    public Post findPostById(int id) {
        return null;
    }

    @Override
    public Candidate findCandidateById(int id) {
        return null;
    }

    @Override
    public void savePhoto(Candidate candidate, String file) {

    }

    @Override
    public String findPhoto(Candidate candidate) {
        return null;
    }

    @Override
    public void saveUser(User user) throws SQLException {
        users.put(user.getEmail(), user);
    }

    @Override
    public User findUser(String email) {
        return users.get(email);
    }
}
