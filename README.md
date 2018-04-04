# research-microgrid

This repository contains code written for Dr. Park's microgrid research at University of Colorado Denver. The purpose is to implement a JADE agent that receives near real-time data from a distributed system of power-grid control/monitoring devices. It should store the data in SQL and send it to a GUI that maps the entire system.

I approached the problem as event-driven graph data. The power control/monitoring network is represented as a graph. The data is "pushed" to the "data agent" implemented in this repository by other agents.

Each data structure is abstracted in the "abs" package such that the code could theoretically be reused for an entirely different purpose. See `Graph` and `MicrogridGraph`.

## Architecture
### Data Flow
1. Measured by Sender Agent, which interfaces with control hardware
![Hardware](docs/screenshots/hardware.jpg)
2. Sent via JADE to Receiver Agent
3. Sent from Receiver Agent to Frontend Client via Socket.io

![JUnit Run Config](docs/screenshots/simplifiedDataFlow.png)

## Prerequisites
1. You will need Administrator-level access to a computer
1. You will need to have cloned this git repository using `git clone`
1. You will need to have IntelliJ (or be able to translate my instructions from IntelliJ to whatever you're using)

## Setup Javascript Frontend
1. Install NodeJS
![Install Node](docs/screenshots/InstallNodeJS.png)
1. Check that NodeJS is actually installed `node --version`,  `npm --version`
1. Using the NodeJS package manager, install a better package manager, called Yarn. `npm i -g yarn`
1. Verify Yarn is installed using `yarn -v`
![Verify NPM and Yarn](docs/screenshots/TerminalNpmAndYarn.png)
1. Move into the frontend project directory using `cd src/main/resources/frontend`
1. Install the frontend dependencies using `yarn install`
![Yarn Install](docs/screenshots/TerminalYarnInstall.png)

## Setup JADE/Java
1. Resolve dependencies using maven (use IntelliJ to do this) `mvn install` might also work
1a. Open the Maven Toolkit Window
![Open the Maven Toolkit Window](docs/screenshots/MavenProjects.png)

1b. Open the Maven project by pressing the plus icon the selecting the pom.xml file
1c. Run Maven install to install all of the Java dependencies (JADE, Jackson, netty-socketio, etc...)
![Maven Install](docs/screenshots/MavenInstall.png)

1d. Run the JUnit tests using Maven to make sure everything installed properly. You should see 0 failures. However, if you see "Failures: n" where n is some number there is a code issue (not a dependency issue). If you don't get test results, then there's probably an issue resolving dependencies or with maven configuration that must be addressed.
![Maven Test](docs/screenshots/MavenTest.png)

2. Run JUnit Tests (again but using IntelliJ test runner instead of Maven)
2a. Open run configurations
![Open run configuration](docs/screenshots/IntelliJRunConfig.png)

2b. Create a new run configuration for the JUnit tests
![JUnit Run Config](docs/screenshots/RunConfigTest.png)

2c. Using the new run configuration, run the JUnit tests to make sure all the tested code works.
![JUnit Run Config](docs/screenshots/IntelliJNicerJUnit.png)

3. Create run configurations for the JADE agents
3a. Open run configurations
![Open run configuration](docs/screenshots/IntelliJRunConfig.png)

3b. Create a run configuration for the receiver agent
`java jade.Boot -gui -local-port 1100 ReceiverAgent:edu.ucdenver.park.microgrid.agents.MicrogridReceiverAgent`
![Run Config](docs/screenshots/IntelliJRunConfigReceiverAgent.png)

![JADE GUI](docs/screenshots/JADEInterfaceWithReceiverAgent.png)

3c. Create a run configuration for the dummy sender agent (for testing purposes)
`java jade.Boot -gui -local-port 1100 DummySenderAgent:edu.ucdenver.park.microgrid.dummy.DummyMicrogridSenderAgent`
![Run Config](docs/screenshots/IntelliJRunConfigDummySenderAgent.png)

3d. Create a run configuration for the receiver agent AND dummy sender agent (for testing purposes)
`java jade.Boot -gui -local-port 1100 DummySenderAgent:edu.ucdenver.park.microgrid.dummy.DummyMicrogridSenderAgent`
![Run Config](docs/screenshots/dummySenderRunConfig.png)

![JADE GUI](docs/screenshots/JADEInterfaceWithBothAgents.png)

3e. Run each agent and ensure that it displays appropriately in the JADE GUI

## Goals
The scrolling graph may eventually look like this oscilloscope.
![oscilloscope](docs/screenshots/oscilloscope.png)