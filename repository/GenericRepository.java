package repository;

import java.util.List;

public interface GenericRepository<T> {
    void create(T entity);
    T read(String id);
    List<T> readAll();
    void update(T entity);
    void delete(String id);
}