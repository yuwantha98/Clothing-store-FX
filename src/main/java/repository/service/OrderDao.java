package repository.service;

import entity.Order;
import repository.CrudDao;

public interface OrderDao extends CrudDao<Order, Integer> {
    // Additional methods for orders can be added here
}
