package org.example.service;

import org.example.model.Client;
import org.example.model.Planet;
import org.example.model.Ticket;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;


public class TicketCrudService {
    public Long create(Client client, Planet fromPlanet, Planet toPlanet) {
        Ticket ticket = new Ticket();
        ticket.setClient(client);
        ticket.setFromPlanetId(fromPlanet);
        ticket.setToPlanetId(toPlanet);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
            return ticket.getId();
        }
    }

    public Ticket getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Ticket ticket = session.get(Ticket.class, id);
            if (ticket == null) {
                throw new IllegalArgumentException("Ticket with this ID not found");
            }
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(long id, Client client, Planet fromPlanet, Planet toPlanet) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Ticket ticket = session.get(Ticket.class, id);
            if (ticket == null) {
                throw new IllegalArgumentException("Ticket not found");
            } else {
                System.out.println("Sorry the ticket cannot be updated");
            }
        }
    }

    public void deleteById(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Ticket ticket = session.get(Ticket.class, id);
            if (ticket == null) {
                throw new IllegalArgumentException("Ticket with this ID not found");
            } else {
                session.remove(ticket);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error deleting ticket", e);
        }
    }

    public List<Ticket> listAll() {
        List<Ticket> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            result = session.createQuery("FROM Ticket", Ticket.class).list();
        }
        return result;
    }

}

