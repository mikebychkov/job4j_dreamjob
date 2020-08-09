package com.dream.store;

import com.dream.model.Candidate;
import com.dream.model.Model;
import com.dream.model.Post;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

import com.dream.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {

        //System.out.println(Paths.get(".").toAbsolutePath().toString());

        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("dreamjob/db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        return ModelStore.findAllModels(new Post(0, ""), pool);
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        Collection<Candidate> coll = ModelStore.findAllModels(new Candidate(0, ""), pool);
        for (Candidate cand : coll) {
            String file = findPhoto(cand);
            cand.setPhoto(file);
        }
        return coll;
    }

    @Override
    public void save(Post post) throws SQLException {
        ModelStore.saveModel(post, pool);
    }

    @Override
    public void save(Candidate candidate) throws SQLException {
        ModelStore.saveModel(candidate, pool);
        savePhoto(candidate, candidate.getPhoto());
    }

    @Override
    public Post findPostById(int id) {
        return ModelStore.findModelById(id, new Post(0, ""), pool);
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate cand = ModelStore.findModelById(id, new Candidate(0, ""), pool);
        if (cand != null) {
            cand.setPhoto(findPhoto(cand));
        }
        return cand;
    }

    @Override
    public void savePhoto(Candidate cand, String file) {
        if (file == null) {
            return;
        }
        String candPhoto = findPhoto(cand);
        String query;
        if (candPhoto == null) {
            query = "INSERT INTO photo(name, owner_id) VALUES (?, ?)";
        } else {
            query = "UPDATE photo SET name = (?) WHERE owner_id = (?)";
        }
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)
        ) {
            ps.setString(1, file);
            ps.setInt(2, cand.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String findPhoto(Candidate cand) {
        String query = "SELECT name FROM photo WHERE owner_id = (?)";
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)
        ) {
            ps.setInt(1, cand.getId());
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return it.getString("name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveUser(User user) throws SQLException {
        User rslUser = findUser(user.getEmail());
        String query;
        if (rslUser == null) {
            query = "INSERT INTO users(name, password, email) VALUES (?, ?, ?)";
        } else {
            query = "UPDATE users SET name = (?), password = (?) WHERE email = (?)";
        }
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findUser(String email) {
        String query = "SELECT id, name, email, password FROM users WHERE email = (?)";
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)
        ) {
            ps.setString(1, email);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    User rsl = new User(it.getInt("id"), it.getString("name"));
                    rsl.setEmail(it.getString("email"));
                    rsl.setPassword(it.getString("password"));
                    return rsl;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
