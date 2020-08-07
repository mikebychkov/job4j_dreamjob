package com.dream.store;

import com.dream.model.Candidate;
import com.dream.model.Post;
import com.dream.model.User;

import java.util.Collection;

public interface Store {

    static Store defaultStore() {
        return PsqlStore.instOf();
    }

    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    void save(Candidate candidate);

    Post findPostById(int id);

    Candidate findCandidateById(int id);

    void savePhoto(Candidate candidate, String file);

    String findPhoto(Candidate candidate);

    void saveUser(User user);

    User findUser(String email);
}
