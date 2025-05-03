/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.List;

/**
 *
 * @author PC
 */
public interface IService<T> {
    boolean create(T o);
    boolean delete(T o);
    boolean update(T o);
    List<T> findAll();
    T findById(int id);
}
