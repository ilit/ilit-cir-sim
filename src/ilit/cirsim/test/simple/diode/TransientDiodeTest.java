package ilit.cirsim.test.simple.diode;

import ilit.cirsim.circuit.elements.Ground;
import ilit.cirsim.circuit.elements.Load;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.base.Resistor;
import ilit.cirsim.circuit.elements.nonlinear.Diode;
import ilit.cirsim.circuit.elements.sources.VoltageSource;
import ilit.cirsim.circuit.elements.util.UniqueIDManager;
import ilit.cirsim.simulator.IdToMatrixIndexRelations;
import ilit.cirsim.test.AbstractSolutionTest;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import org.apache.commons.math3.util.Precision;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TransientDiodeTest extends AbstractSolutionTest
{
    @AfterMethod
    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test
    public void oneDiodeTest()
    {
        initModules();

        /** Instantiate all components */
        /** This voltage value has no effect */
        VoltageSource voltageSource = new VoltageSource(234);
        Diode diode = new Diode();
        Resistor resistor = new Load(1);

        Ground gr = new Ground();
        Assert.assertEquals(gr.getId(), UniqueIDManager.GROUND_NODE_ID);
        Node node1 = new Node();
        Node node2 = new Node();

        /**
         * Topology:
         *     - +    + -
         * g---(V)--1--D--2--R---g
         */
        initComponent(voltageSource, gr, node1);
        initComponent(diode, node1, node2);
        initComponent(resistor, node2, gr);

        equations.prepareSystemSize();

        /** Change voltage source voltage */
        voltageSource.removeStamp(equations);
        voltageSource.setVoltage(2.0);
        /** Solver will place new stamp by himself */

        solve();

        /** Check results */

        //Assert.assertEquals(diode.getResistance(), Diode.FORWARD_RESISTANCE);

        Assert.assertEquals(diode.stampIsPlaced(), true);
        Assert.assertEquals(resistor.stampIsPlaced(), true);
        Assert.assertEquals(voltageSource.stampIsPlaced(), true);

        Assert.assertEquals(voltageSource.getId(), 1);
        Assert.assertEquals(diode.getId(), 2);
        Assert.assertEquals(resistor.getId(), 3);
        Assert.assertEquals(node1.getId(), 4);
        Assert.assertEquals(node2.getId(), 5);

        Assert.assertEquals(voltageSource.cathode.getId(), node1.getId());
        Assert.assertEquals(voltageSource.anode.getId(), gr.getId());

        Assert.assertEquals(equations.getXVector().size(), 3);

        Assert.assertEquals(IdToMatrixIndexRelations.instance.getIndex(voltageSource), 0);
        Assert.assertEquals(IdToMatrixIndexRelations.instance.getIndex(node1), 1);
        Assert.assertEquals(IdToMatrixIndexRelations.instance.getIndex(node2), 2);

        Matrix matrix = equations.getMatrix();
        SparseVector rhs = equations.getSideVector();

        /** Set by voltage source stamp */
        Assert.assertEquals(rhs.get(0), 2.0);
        int sourceCathodeIndex =
                IdToMatrixIndexRelations.instance.getIndex(voltageSource.cathode);
        Assert.assertEquals(sourceCathodeIndex, 1);
        Assert.assertEquals(matrix.get(0, 1), 1.0);
        Assert.assertEquals(matrix.get(1, 0), 1.0);
        Assert.assertEquals(matrix.get(2, 0), 0.0);
        Assert.assertEquals(matrix.get(0, 2), 0.0);

        /** Source current */
        double sourceCurrent = equations.getSolutionCurrent(voltageSource);
        double approxSourceCurrent = Precision.round(sourceCurrent, ROUNDING_SCALE);
        Assert.assertEquals(approxSourceCurrent, -1.9802);
        /** Node 1 voltage */
        Assert.assertEquals(equations.getSolutionNodeVoltage(node1), 2.0);
        /** Node 2 voltage */
        double node2Voltage = equations.getSolutionNodeVoltage(node2);
        double approxNode2Voltage = Precision.round(node2Voltage, ROUNDING_SCALE);
        Assert.assertEquals(approxNode2Voltage, 1.9802);

        /**
         * Step two.
         * Source voltage lowers from 2.0 to 1.0.
         */
        voltageSource.removeStamp(equations);
        voltageSource.setVoltage(1.0);
        /** Solver will place new stamp by himself */

        solve();
        /** Source current */
        sourceCurrent = equations.getSolutionCurrent(voltageSource);
        approxSourceCurrent = Precision.round(sourceCurrent, ROUNDING_SCALE);
        Assert.assertEquals(approxSourceCurrent, -0.9901);

        /**
         * Step three.
         * Source voltage lowers from 1.0 to 0.0.
         */
        voltageSource.removeStamp(equations);
        voltageSource.setVoltage(0.0);
        /** Solver will place new stamp by himself */

        solve();
        /** Source current */
        sourceCurrent = equations.getSolutionCurrent(voltageSource);
        approxSourceCurrent = Precision.round(sourceCurrent, ROUNDING_SCALE);
        Assert.assertEquals(approxSourceCurrent, 0.0);

        /**
         * Step three.
         * Source voltage lowers from 0.0 to -1.0.
         */
        voltageSource.removeStamp(equations);
        voltageSource.setVoltage(-1.0);
        /** Solver will place new stamp by himself */
        /** Diode should swap state now to stopping resistance from permitting */

        solve();
        /** Source current */
        sourceCurrent = equations.getSolutionCurrent(voltageSource);
        approxSourceCurrent = Precision.round(sourceCurrent, ROUNDING_SCALE);
        /** Almost no current when diode is in blocking state */
        Assert.assertEquals(approxSourceCurrent, 1.0e-5);
    }
}
