package org.example.service;

import org.example.model.Client;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ClientCrudService {

    public long create(String name){

        Client client = new Client();
        client.setName(name);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(client);
            transaction.commit();
            return client.getId();
        }

    }
    public String getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Client client = session.get(Client.class, id);
            if (client == null) {
                throw new IllegalArgumentException("Client with this ID not found");
            }
            return client.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void setName(long id, String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Client client = session.get(Client.class, id);
            if (client == null) {
                throw new IllegalArgumentException("Client not found");
            } else {
                client.setName(name);
                session.merge(client);
                transaction.commit();
            }
        }
    }
    public void deleteById (long id){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Client client = session.get(Client.class, id);
            if (client == null) {
                throw new IllegalArgumentException ("Client with this ID not found");
            } else {
                session.remove(client);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error deleting client", e);
        }
    }
    public List<Client> listAll(){
        List<Client> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            result = session.createQuery("FROM Client", Client.class).list();
        }
        return result;
    }

}
