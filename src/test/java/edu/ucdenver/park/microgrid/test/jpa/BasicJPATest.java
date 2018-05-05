package edu.ucdenver.park.microgrid.test.jpa;

import edu.ucdenver.park.microgrid.dummy.Student;
import edu.ucdenver.park.microgrid.persistence.MicrogridHibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;

/**
 * BasicJPATest
 * <p>
 * class: JUnit Test
 * <p>
 * This test is intended to verify some basic functionality of JPA for the purposes of this project.
 * This test will break if JPA isn't setup properly or fails to connect with a database.
 * This test doesn't depend on any of the microgrid code except for MicrogridHibernateUtil. MicrogridHibernateUtil
 * reads the config and starts Hibernate (Hibernate is a JPA implementation that connects Java objects with SQL).
 * If tests are failing in here, it's because we aren't properly connecting to the database.
 * <p>
 * Before running this test,
 * 1. Ensure all Maven dependencies are installed.
 * 2. Ensure a database (e.g. MySQL or PostgreSQL) is installed and running
 * 3. Ensure database configuration facilitates communication between this project and the db
 */
public class BasicJPATest {
    /**
     * makeStudent()
     * <p>
     * static method
     *
     * @return a student object for the purposes of persistence testing
     */
    private static Student makeStudent() {
        return new Student("Jake", "Billings", 893738378);
    }

    /**
     * studentObjectShouldWork()
     * <p>
     * test
     * <p>
     * verifies the student object from makeStudent() works so that we can count on it in subsequent tests
     */
    @Test
    void studentObjectShouldWork() {
        Student s = makeStudent();

        assert s.getFirstName().equals("Jake");
        assert s.getLastName().equals("Billings");
        assert s.getStudentId() == 893738378;
        assert s.getGreeting().equals("Hi! My name is Jake!");
    }

    /**
     * sessionFactoryShouldExist()
     * <p>
     * test
     * <p>
     * verifies that MicrogridHibernateUtil initialized the Hibernate library, which implements JPA
     * use loading MicrogridHibernateUtil will cause exceptions in startup if we are unable to
     * connect to the database
     */
    @Test
    void sessionFactoryShouldExist() {
        SessionFactory factory = MicrogridHibernateUtil.sessionFactory;
        assert factory != null;
    }

    /**
     * shouldPersistAndReadStudentObject()
     *
     * test
     *
     * verifies that basic read/write operations function on JPA
     *
     * writes a dummy student
     * checks if it's there
     * deletes a dummy student
     * checks that it's not there
     */
    @Test
    void shouldPersistAndReadStudentObject() {
        //Get the app's instance of SessionFactory
        SessionFactory sessionFactory = MicrogridHibernateUtil.sessionFactory;

        //---Write the Student to the DB---
        //Use the SessionFactory to open a session
        Session session = sessionFactory.openSession();
        //Start a transaction to group all of the changes we plan to apply to the database
        Transaction tx = session.beginTransaction();
        //Instantiate a mock student object to store
        Student student = makeStudent();
        //Add an instruction to persist the student to the db to the transaction object
        session.persist(student);
        //Commit the transaction to send the write
        tx.commit();
        // Close resources
        session.close();

        //---Read the Student from the DB---
        //Open a new session
        Session session2 = sessionFactory.openSession();
        //Query the db for the student
        Student studentFromDb = (Student) session2.get(Student.class, (long) 893738378);
        // Close resources
        session2.close();

        assert studentFromDb.getFirstName().equals("Jake");
        assert studentFromDb.getLastName().equals("Billings");
        assert studentFromDb.getStudentId() == 893738378;
        assert studentFromDb.getGreeting().equals("Hi! My name is Jake!");

        //---Delete the Student from the DB---
        //Open a new session
        Session session3 = sessionFactory.openSession();
        //Create a transaction to store our db modifications
        Transaction tx2 = session3.beginTransaction();
        //Delete the student
        session3.delete(studentFromDb);
        //Write our changes to the db
        tx2.commit();
        // Close resources
        session3.close();

        //---Read the Student from the DB---
        //Open a new session
        Session session4 = sessionFactory.openSession();
        //Query the db for the student (it shouldn't be there this time)
        Student studentFromDb2 = (Student) session4.get(Student.class, (long) 893738378);
        // Close resources
        session4.close();

        assert studentFromDb2 == null;
    }
}
