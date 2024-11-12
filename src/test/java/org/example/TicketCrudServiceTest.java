package org.example;

import org.example.model.Client;
import org.example.model.Planet;
import org.example.model.Ticket;
import org.example.service.ClientCrudService;
import org.example.service.TicketCrudService;
import org.example.util.HibernateUtil;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketCrudServiceTest {
    private ClientCrudService clientCrudService;
    private TicketCrudService ticketCrudService;
    private Flyway flyway;

    private Client client;
    private Planet fromPlanet;
    private Planet toPlanet;

    @BeforeEach
    public void cleanUp() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM Ticket").executeUpdate();
            session.createQuery("DELETE FROM Client").executeUpdate();
            session.createQuery("DELETE FROM Planet").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @BeforeEach
    public void setUp() {
        clientCrudService = new ClientCrudService();
        ticketCrudService = new TicketCrudService();

        System.setProperty("test.db", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        flyway = Flyway.configure()
                .dataSource(System.getProperty("test.db", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"), "sa", "")
                .locations("classpath:db/migrations")
                .load();
        flyway.migrate();

        client = new Client();
        client.setName("Homer Simpson");
        long clientId = clientCrudService.create(client.getName());


        fromPlanet = new Planet("Cybertron");
        System.out.println("From planet"+fromPlanet);
        toPlanet = new Planet("Wakanda");
        System.out.println("To planet"+toPlanet);
    }



    @Test
    void testCreateTicket() {
        long ticketId = ticketCrudService.create(client, fromPlanet, toPlanet);

        assertTrue(ticketId > 0, "Ticket ID should be greater than 0");

        Ticket ticket = ticketCrudService.getById(ticketId);

        assertNotNull(ticket, "Ticket should exist after creation");
        assertEquals(client.getId(), ticket.getClient().getId(), "Client ID should match");
        assertEquals(fromPlanet.getId(), ticket.getFromPlanetId().getId(), "From planet ID should match");
        assertEquals(toPlanet.getId(), ticket.getToPlanetId().getId(), "To planet ID should match");
    }

    @Test
    void testGetTicketById() {
        long ticketId = ticketCrudService.create(client, fromPlanet, toPlanet);
        Ticket ticket = ticketCrudService.getById(ticketId);
        assertNotNull(ticket, "Ticket should exist");
        assertEquals(ticketId, ticket.getId(), "Ticket ID should match");
    }

    @Test
    void testDeleteTicketById() {
        long ticketId = ticketCrudService.create(client, fromPlanet, toPlanet);
        Ticket ticket = ticketCrudService.getById(ticketId);
        assertNotNull(ticket, "Ticket should exist before deletion");

        ticketCrudService.deleteById(ticketId);
        ticket = ticketCrudService.getById(ticketId);
        assertNull(ticket, "Ticket should be null after deletion");
    }

    @Test
    void testDeleteTicketById_TicketNotFound() {
        assertThrows(IllegalArgumentException.class, () -> ticketCrudService.deleteById(999L));
    }
    @Test
    void testUpdateTicketNotFound (){
        assertThrows(IllegalArgumentException.class, () -> ticketCrudService.update(999L, client, toPlanet, fromPlanet));

    }
    @Test
    void testUpdate(){
        long ticketId = ticketCrudService.create(client, fromPlanet, toPlanet);
        Ticket ticket = ticketCrudService.getById(ticketId);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ticketCrudService.update(ticketId, client, fromPlanet, toPlanet);
        assertTrue(outContent.toString().contains("Sorry the ticket cannot be updated"),
                "Output should contain the correct message");
        Ticket updatedTicket = ticketCrudService.getById(ticketId);
        assertEquals(ticketId, updatedTicket.getId(), "Ticket ID should remain the same");
    }



    @Test
    void testListAllTickets() {
        Client client1 = new Client();
        client1.setName("Ned Flanders");
        Planet fromPlanet1 = new Planet("qwe");
        Planet toPlanet1 = new Planet("rty");

        ticketCrudService.create(client, fromPlanet, toPlanet);
        ticketCrudService.create(client1, fromPlanet1, toPlanet1);

        List<Ticket> tickets = ticketCrudService.listAll();
        assertNotNull(tickets, "Tickets list should not be null");
        assertEquals(2, tickets.size(), "Wrong number of tickets");
    }
}
