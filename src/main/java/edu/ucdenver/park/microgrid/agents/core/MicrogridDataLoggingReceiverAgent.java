package edu.ucdenver.park.microgrid.agents.core;

import edu.ucdenver.park.microgrid.data.abs.Datum;
import edu.ucdenver.park.microgrid.live.DatumHandler;
import edu.ucdenver.park.microgrid.persistence.MicrogridHibernateUtil;
import jade.core.behaviours.CyclicBehaviour;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * MicrogridDataLoggingReceiverAgent
 * <p>
 * class: JADE agent
 * <p>
 * performs all the behaviors of a MicrogridReceiverAgent AND saves all data it gets to an SQL
 * database using the Hibernate JPA implementation
 */
public class MicrogridDataLoggingReceiverAgent extends MicrogridReceiverAgent {
    private BlockingQueue<Datum> dataQueue;

    public MicrogridDataLoggingReceiverAgent() {
        super();

        //Setup a blocking queue that will throw an error if we can't write to the db fast enough
        dataQueue = new LinkedBlockingQueue<Datum>(100);

        //Register to receive all of the data from the LiveGrid as it comes in
        this.getLiveGrid().registerDatumHadler(new DatumHandler() {
            public void onDatum(Datum datum) {
                dataQueue.add(datum);
            }
        });

        //Add a behavior to save the queued data to the db ever blockTime ms
        addBehaviour(new DataPersistenceBehavior(10));
    }

    /**
     * DataPersistenceBehavior
     * <p>
     * class
     * private internal class: this class exists INSIDE the MicrogridReceiverAgent class and is private to it (it is used only in setup())
     * behavior: this a JADE behavior class
     * <p>
     * writes the data we receive to the db using Hibernate every blockTime ms
     */
    private class DataPersistenceBehavior extends CyclicBehaviour {
        /**
         * blockTime
         *
         * long
         *
         * see documentation for blocktime on JADE's CyclicBehvaior; this is the amount of time we
         *  give the db to handle our write requests
         */
        private final long blockTime;

        DataPersistenceBehavior(long blockTime) {
            this.blockTime = blockTime;
        }

        public void action() {
            //Open a new session to write to the db
            Session session = MicrogridHibernateUtil.sessionFactory.openSession();

            //Create an entity manager transaction to write multiple data
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();

            //Persist all the data from the queue
            for (Datum datum : dataQueue) {
                em.persist(datum);
            }
            dataQueue.clear();

            //Commit/save the transaction
            tx.commit();
            //Close the session
            session.close();


            block(this.blockTime);
        }
    }
}
