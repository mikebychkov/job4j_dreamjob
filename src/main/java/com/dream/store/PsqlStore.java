package com.dream.store;

import com.dream.model.Candidate;
import com.dream.model.Model;
import com.dream.model.Post;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.dbcp2.BasicDataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        return findAllModels(new Post(0, ""));
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        Collection<Candidate> coll = findAllModels(new Candidate(0, ""));
        for (Candidate cand : coll) {
            String file = findPhoto(cand);
            cand.setPhoto(file);
        }
        return coll;
    }

    private <T extends Model> Collection<T> findAllModels(T model) {
        List<T> models = new ArrayList<>();
        String query = String.format("SELECT id, name FROM %s", model.getTableName());
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(query)
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Model m = model.of(it.getInt("id"), it.getString("name"));
                    models.add((T) m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    @Override
    public void save(Post post) {
        saveModel(post);
    }

    @Override
    public void save(Candidate candidate) {
        saveModel(candidate);
        savePhoto(candidate, candidate.getPhoto());
    }

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

    public void saveModel(Model model) {
        if (model.getId() == 0) {
            create(model);
        } else {
            update(model);
        }
    }

    private <T extends Model> T create(T model) {
        String query = String.format("INSERT INTO %s(name) VALUES (?)", model.getTableName());
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, model.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    model.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) model;
    }

    private void update(Model model) {
        String query = String.format("UPDATE %s SET name = (?) WHERE id = (?)", model.getTableName());
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)
        ) {
            ps.setString(1, model.getName());
            ps.setInt(2, model.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Post findPostById(int id) {
        return findModelById(id, new Post(0, ""));
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate cand = findModelById(id, new Candidate(0, ""));
        if (cand != null) {
            cand.setPhoto(findPhoto(cand));
        }
        return cand;
    }

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

    private <T extends Model> T findModelById(int id, T model) {
        String query = String.format("SELECT id, name FROM %s WHERE id = (?)", model.getTableName());
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    model.setId(id);
                    model.setName(it.getString("name"));
                } else {
                    model = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
