package edu.ucdenver.park.microgrid.agents.example;

import jade.core.Agent;

/**
 * HelloAgent
 *
 * basic agent that demonstrates JADE is working
 *
 * todo remove this cause it doesn't do anything
 *
 * @author Jake Billings
 */
public class HelloAgent extends Agent
{
    protected void setup()
    {
        System.out.println("Hello World. ");
        System.out.println("My name is "+ getLocalName());
    }
}