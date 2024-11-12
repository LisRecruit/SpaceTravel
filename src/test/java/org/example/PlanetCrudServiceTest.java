package org.example;

import org.example.model.Planet;
import org.example.service.PlanetCrudService;
import org.example.util.HibernateUtil;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 class PlanetCrudServiceTest {
    private PlanetCrudService planetCrudService;
    private Flyway flyway;
    @BeforeEach
    public void setUp(){
        planetCrudService = new PlanetCrudService();

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
            session.createQuery("DELETE FROM Planet").executeUpdate(); // Удаляем всех клиентов
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
     void testCreate() {
        String planetId = planetCrudService.create("Marsatron");
        assertNotNull(planetId, "Planet ID should not be null");
        assertEquals("MARSATRON", planetId, "Planet ID should be in uppercase");
    }
    @Test
     void testGetById(){
        String name = "Cybertron";
        String planetId = planetCrudService.create(name);
        String planetName = planetCrudService.getById(planetId);
        assertEquals(name, planetName);
    }

    @Test
     void testSetName() {
        String planetId = planetCrudService.create("Ubuntu");
        String newName = "Ugunto";
        planetCrudService.setName(planetId, newName);
        String updatedClient = planetCrudService.getById(planetId);
        assertNotNull(updatedClient);
        assertEquals(newName, updatedClient, "Planet name not updated");
    }

    @Test
     void testSetName_ClientNotFound() {

        assertThrows(IllegalArgumentException.class, () -> planetCrudService.setName("999L", "New Name"));
    }

    @Test
     void testDeleteById() {
        String planetId = planetCrudService.create("Numerica");
        System.out.println("Created client with ID: " + planetId);

        String createdClient = planetCrudService.getById(planetId);
        assertNotNull(createdClient, "Client should exist before deletion");

        planetCrudService.deleteById(planetId);
        System.out.println("Deleted client with ID: " + planetId);

        String deletedPlanet = planetCrudService.getById(planetId);
        assertNull(deletedPlanet, "Planet should be null after deletion");
    }

    @Test
     void testDeleteById_ClientNotFound() {
        assertThrows(IllegalArgumentException.class, () -> planetCrudService.deleteById("999L"));
    }
    @Test
     void testListAll() {

        planetCrudService.create("Suru");
        planetCrudService.create("Luru");

        List<Planet> planets = planetCrudService.listAll();

        System.out.println("Total clients: " + planets.size());
        for (Planet planet : planets) {
            System.out.println("Planet ID: "+planet.getId()+"  ");
            System.out.println("Planet name: " + planet.getName());
        }

        assertNotNull(planets);
        assertEquals(2, planets.size(), "Wrong clients amount");
    }
}
