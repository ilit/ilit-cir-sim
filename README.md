ilit-cir-sim
============

Educational circuit simulation prototype.

I'm writing simplest circuit simulator for educational purposes by "Circuit Simulation - Farid N. Najm - 2010" book using Java. Project is in active progress.

For now there is:
) code to compose MNA system from some basic elements.
) and code displaying sample circuit.

Project can be used:
) As an example of JUNG framework usage
) To learn circuit simulation code flow with object oriented approach.

Glossary:
Most terms come from the book "Circuit Simulation - Farid N. Najm - 2010".
) MNA - Modified Nodal Analysis - method to compose equations system of a circuit.
) Stamp - particular element effect on system of equations. Modifies equations matrix and vector.
) Element == Component.
) Group1 - group of elements whose stamps does not contain current.
) Group2 - current is included in stamp.
) bearing - non-ground nodes.
) SideVector - right hand side vector of the system of equations.
) matrix - MNA matrix of the system of equations.

Third party framewokds used:
) JUNG - Java Universal Network/Graph Framework - stores circuit graph and displays it.
) matrix-toolkits-java - Java linear algebra library - matrix and vector optimized storage and operations.
) Guice - dependency injection.
) Apache Commons - DualHashBidiMap - bidirectional hashmap.
) TestNG - unit testing.
