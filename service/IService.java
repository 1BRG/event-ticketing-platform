package service;

import java.util.Collection;

public interface IService<T> {
    void add(T entity);
    T findById(String id);
    Collection<T> getAll();
}