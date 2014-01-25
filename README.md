ilit-cir-sim
============

Simple circuit simulation prototype on Java.

I'm writing simplest circuit simulator for
* My own projects.
* To share educational efforts with community. This can be helpful to anyone learning subject of circuit simulation.
* To impress my possible employer.

Primary source of theory is "Circuit Simulation - Farid N. Najm - 2010".
Secondary is "Electronic Circuit and System - Lawrence Pillage".

Project is in active progress.

For now program:
* Generates random simple circuit consisting of resistors(loads) and some voltage source.
    Wires are resistors too with resistance of 1 Ohm.
* Composes MNA equations system from this circuit.
* Solves the system with iterative method.
* Displays generated circuit onscreen by JUNG visualization function.
* Outputs all sources currents as a circuit solution to debug console.

Latest code can be not operational due to nonlinear solver early implementation.

![mult](http://i965.photobucket.com/albums/ae139/ilitvinov/sample1_zpsbe648fd6.png)

Project can be used:
* As an example of JUNG framework usage.
* To learn circuit simulation code flow with object oriented approach.

Glossary:
Most terms come from the book "Circuit Simulation - Farid N. Najm - 2010".
* MNA - Modified Nodal Analysis - method to compose equations system of a circuit.
* Stamp - particular element effect on system of equations. Modifies equations matrix and vector.
* Element == Component.
* Group1 - group of elements whose stamps does not contain current.
* Group2 - current is included in stamp.
* bearing - non-ground nodes.
* SideVector - right hand side vector of the system of equations.
* matrix - MNA matrix of the system of equations.

Third party frameworks used:
* JUNG - Java Universal Network/Graph Framework - stores circuit graph and displays it.
* matrix-toolkits-java - Java linear algebra library - matrix and vector optimized storage and operations.
* Guice - dependency injection.
* Apache Commons - DualHashBidiMap - bidirectional hashmap.
* TestNG - unit testing.
