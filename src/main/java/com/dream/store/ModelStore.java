package com.dream.store;

import com.dream.model.Model;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ModelStore {

    public static void saveModel(Model model, BasicDataSource pool) {
        Map<String, String> stringFields = new HashMap<>();
        if (model.getId() == 0) {
            ModelStore.create(model, stringFields, pool);
        } else {
            ModelStore.update(model, stringFields, pool);
        }
    }

    public static void saveModelWithFields(Model model, Map<String, String> stringFields, BasicDataSource pool) {
        if (model.getId() == 0) {
            ModelStore.create(model, stringFields, pool);
        } else {
            ModelStore.update(model, stringFields, pool);
        }
    }

    public static <T extends Model> T create(T model, Map<String, String> stringFields, BasicDataSource pool) {
        TreeMap<String, String> sortedFields = new TreeMap<>(stringFields);
        StringJoiner fields = new StringJoiner(",");
        StringJoiner params = new StringJoiner(",");
        fields.add("name");
        params.add("?");
        for (Map.Entry<String, String> ent : sortedFields.entrySet()) {
            fields.add(ent.getKey());
            params.add("?");
        }
        //
        String query = String.format("INSERT INTO %s(%s) VALUES (%s)", model.getTableName(), fields.toString(), params.toString());
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            int paramIndex = 1;
            ps.setString(paramIndex++, model.getName());
            for (Map.Entry<String, String> ent : sortedFields.entrySet()) {
                ps.setString(paramIndex++, ent.getValue());
            }
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

    public static void update(Model model, Map<String, String> stringFields, BasicDataSource pool) {
        TreeMap<String, String> sortedFields = new TreeMap<>(stringFields);
        StringJoiner fieldsWithParams = new StringJoiner(",");
        fieldsWithParams.add("name = (?)");
        for (Map.Entry<String, String> ent : sortedFields.entrySet()) {
            fieldsWithParams.add(ent.getKey() + "=(?)");
        }
        //
        String query = String.format("UPDATE %s SET %s WHERE id = (?)", model.getTableName(), fieldsWithParams.toString());
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)
        ) {
            int paramIndex = 1;
            ps.setString(paramIndex++, model.getName());
            for (Map.Entry<String, String> ent : sortedFields.entrySet()) {
                ps.setString(paramIndex++, ent.getValue());
            }
            ps.setInt(paramIndex, model.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends Model> Collection<T> findAllModels(T model, BasicDataSource pool) {
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

    public static <T extends Model> T findModelById(int id, T model, BasicDataSource pool) {
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
