package org.example;


import org.example.model.Client;
import org.example.service.ClientCrudService;
import org.example.util.HibernateUtil;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

 class ClientCrudServiceTest {
    private ClientCrudService clientCrudService;
    private Flyway flyway;


    @BeforeEach
    public void setUp() {
        clientCrudService = new ClientCrudService();

        System.setProperty("test.db", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        flyway = Flyway.configure()
                .dataSource(System.getProperty("test.db", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"), "sa", "")
                .locations("classpath:db/migrations")
                .load();
        flyway.migrate();
    }

    @BeforeEach
    public void cleanUp() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM Client").executeUpdate(); // Удаляем всех клиентов
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
     void testCreate() {
        long clientId = clientCrudService.create("Homer Simpson");
        assertTrue(clientId > 0, "Client ID should be greater than 0");

        String clientName = clientCrudService.getById(clientId);
        assertEquals("Homer Simpson", clientName, "Client name should match");
    }

    @Test
     void testGetById() {
        String name = "John Johnson";
        long clientId = clientCrudService.create(name);
        String clientName = clientCrudService.getById(clientId);
        assertEquals(name, clientName);
    }

    @Test
     void testSetName() {
        long clientId = clientCrudService.create("Mark Tven");
        String newName = "Mark Antoniy";
        clientCrudService.setName(clientId, newName);
        String updatedClient = clientCrudService.getById(clientId);
        assertNotNull(updatedClient);
        assertEquals(newName, updatedClient, "Client's name not updated");
    }

    @Test
     void testSetName_ClientNotFound() {

        assertThrows(IllegalArgumentException.class, () -> clientCrudService.setName(999L, "New Name"));
    }

    @Test
     void testDeleteById() {
        long clientId = clientCrudService.create("Bart Simpson");
        System.out.println("Created client with ID: " + clientId);

        String createdClient = clientCrudService.getById(clientId);
        assertNotNull(createdClient, "Client should exist before deletion");

        clientCrudService.deleteById(clientId);
        System.out.println("Deleted client with ID: " + clientId);
        String deletedPlanet = clientCrudService.getById(clientId);
        assertNull(deletedPlanet, "Planet should be null after deletion");
    }

    @Test
     void testDeleteById_ClientNotFound() {
        assertThrows(IllegalArgumentException.class, () -> clientCrudService.deleteById(999L));
    }

    @Test
     void testListAll() {

        clientCrudService.create("Marge Simpson");
        clientCrudService.create("Ned Flanders");

        List<Client> clients = clientCrudService.listAll();
        System.out.println("Total clients: " + clients.size());
        for (Client client : clients) {
            System.out.println("Client ID: " + client.getId() + "  ");
            System.out.println("Client name: " + client.getName());
        }

        assertNotNull(clients);
        assertEquals(2, clients.size(), "Wrong clients amount");
    }
}

