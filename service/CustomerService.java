package service;

import model.Customer;
import repository.CustomerRepository;
import java.util.Collection;

public class CustomerService implements IService<Customer> {
    
    private CustomerRepository repository;

    public CustomerService() {
        this.repository = CustomerRepository.getInstance();
    }

    @Override
    public void add(Customer entity) {
        repository.create(entity);
        AuditService.getInstance().logAction("add_customer");
    }

    @Override
    public Customer findById(String id) {
        AuditService.getInstance().logAction("find_customer_by_id");
        return repository.read(id);
    }

    @Override
    public Collection<Customer> getAll() {
        AuditService.getInstance().logAction("get_all_customers");
        return repository.readAll();
    }

    public void updateCustomer(Customer customer) {
        repository.update(customer);
        AuditService.getInstance().logAction("update_customer");
    }

    public void deleteCustomer(String id) {
        repository.delete(id);
        AuditService.getInstance().logAction("delete_customer");
    }
}