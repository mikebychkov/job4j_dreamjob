package com.dream.store;

import com.dream.model.Post;
import com.dream.model.Candidate;
import com.dream.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {

    private static final MemStore INST = new MemStore();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private static AtomicInteger POST_ID = new AtomicInteger(4);
    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(4);

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job"));
        posts.put(2, new Post(2, "Middle Java Job"));
        posts.put(3, new Post(3, "Senior Java Job"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Post findPostById(int id) {
        return posts.get(id);
    }

    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    @Override
    public void savePhoto(Candidate candidate, String file) {

    }

    @Override
    public String findPhoto(Candidate candidate) {
        return null;
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public User findUser(String email) {
        return null;
    }
}
