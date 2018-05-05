package edu.ucdenver.park.microgrid.persistence;

import edu.ucdenver.park.microgrid.dummy.Student;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * MicrogridHibernateUtil
 * <p>
 * class
 * <p>
 * This class functions as a set of static functions to make interactions with the Hibernate library
 * for convenient.
 * <p>
 * Hibernate is a library we use to manage persistence of Java objects to SQL databases. (MicrogridDatum -> MySQL)
 * Hibernate is an implementation of the JPA standard for Java object persistence.
 */
public class MicrogridHibernateUtil {
    private MicrogridHibernateUtil() {
    }

    /**
     * buildSessionFactory()
     * <p>
     * code I copied and pasted from the tutorial to build the Session Factory Object
     * <p>
     * static method
     * <p>
     * throws exceptions if there are issues with the configuration
     * if the config fails to load, verify that it is present and formatted correctly
     * if it still fails, verify that it is in the root of your working directory (this is configured in your run configuration)
     * <p>
     * if the connection to the db fails, verify the DB is installed and running
     * if it still fails, verify that the address and credentials in the config are correct for the db you are attempting to connect to
     *
     * @return a session factory object for this instance of this application to use
     */
    private static SessionFactory buildSessionFactory() {
        Configuration config = new Configuration();
        config.configure("hibernate.cfg.xml");

        config.addAnnotatedClass(Student.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

        return config.buildSessionFactory(serviceRegistry);
    }

    /**
     * sessionFactory
     * <p>
     * SessionFactory
     * <p>
     * the single Hibernate SessionFactory object for the entire application to share
     */
    public static SessionFactory sessionFactory = buildSessionFactory();
}
