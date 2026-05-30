package service;

import model.Customer;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class CustomerService implements IService<Customer> {
    private Map<String, Customer> customers;

    public CustomerService() {
        this.customers = new HashMap<>();
    }

    @Override
    public void add(Customer entity) {
        customers.put(entity.getId(), entity);
        System.out.println("Client adaugat cu succes: " + entity.getName());
    }

    @Override
    public Customer findById(String id) {
        return customers.get(id);
    }

    @Override
    public Collection<Customer> getAll() {
        return customers.values();
    }
}