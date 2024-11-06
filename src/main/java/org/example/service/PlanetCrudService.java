package org.example.service;

import org.example.model.Client;
import org.example.model.Planet;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PlanetCrudService {
    public String create(String name){

        Planet planet = new Planet(name);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(planet);
            transaction.commit();
            return planet.getId();
        }

    }
    public String getById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Planet planet = session.get(Planet.class, id);
            if (planet == null) {
                throw new IllegalArgumentException("Planet with this ID not found and can not be puleld from DB");
            }
            return planet.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void setName(String id, String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Planet planet = session.get(Planet.class, id);
            if (planet == null) {
                throw new IllegalArgumentException("Planet not found");
            } else {
                planet.setName(name);
                session.merge(planet);
                transaction.commit();
            }
        }
    }
    public void deleteById (String id){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Planet planet = session.get(Planet.class, id);
            if (planet == null) {
                throw new IllegalArgumentException ("Planet with this ID not found and can not be deleted");
            } else {
                session.remove(planet);
                transaction.commit();
            }
        }
    }
    public List<Planet> listAll(){
        List<Planet> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            result = session.createQuery("FROM Planet", Planet.class).list();
        }
        return result;
    }
}
