package ilit.cirsim.view;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import org.apache.commons.collections15.Transformer;
import ilit.cirsim.circuit.CircuitGraph;
import ilit.cirsim.circuit.elements.Node;
import ilit.cirsim.circuit.elements.Wire;
import ilit.cirsim.circuit.elements.base.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Modifies default JUNG graph visualization.
 */
@Singleton
public class GraphVisualization
{
    private static final int LAYOUT_WIDTH = 1100;
    private static final int LAYOUT_HEIGHT = 900;

    private RenderContext<Node, Component> renderContext;
    private CircuitGraph circuitGraph;

    @Inject
    public GraphVisualization(CircuitGraph circuitGraph)
    {
        this.circuitGraph = circuitGraph;
    }

    public void show()
    {
        ISOMLayout<Node, Component> layout = new ISOMLayout(circuitGraph.graph);
        layout.setSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
        layout.step(); layout.step(); layout.step(); layout.step(); layout.step(); layout.step(); layout.step();
        BasicVisualizationServer<Node, Component> visualServer = new BasicVisualizationServer<Node, Component>(layout);
        visualServer.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT)); //Sets the viewing area size

        renderContext = visualServer.getRenderContext();

        modifyDefaultGraphView();

        setFrame(visualServer);
    }

    private void modifyDefaultGraphView()
    {
        setEdgeLineWidth();
        setEdgeLineShape();
        setEdgeLineColor();

        setEdgeLabelContent();
        setEdgeLabelFont();
        disableLabelRotation();

        setNodeLabelContent();
        setNodeLabelFont();
        smallNode();
    }

    private void setNodeLabelFont()
    {
        Transformer<Node,Font> fontTransformer =
        new Transformer<Node, Font>()
        {
            public Font transform(Node node)
            {
                if (node.isGround())
                    return new Font("Symbola", Font.PLAIN, 30);

                return new Font("Verdana", Font.PLAIN, 8);
            }
        };
        renderContext.setVertexFontTransformer(fontTransformer);
    }

    private void setNodeLabelContent()
    {
        Transformer<Node,String> nodeStringTransformer =
                new Transformer<Node, String>()
                {
                    public String transform(Node node)
                    {
                        if (node.isGround())
                            return "⏚grd";

                        return "" + node.getId();
                    }
                };
        renderContext.setVertexLabelTransformer(nodeStringTransformer);
    }

    private void setEdgeLineWidth()
    {
        Transformer<Component, Stroke> edgeStrokeTransformer =
                new Transformer<Component, Stroke>()
                {
                    public Stroke transform(Component component)
                    {
                        float width = component.getView().getGraphEdgeWidth();
                        return new BasicStroke(width, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f);
                    }
                };
        renderContext.setEdgeStrokeTransformer(edgeStrokeTransformer);
        renderContext.setEdgeArrowStrokeTransformer(edgeStrokeTransformer);
    }


    private void setEdgeLineShape()
    {
        /**
         * Curves for wires. Straight lines for other components.
         */
            Transformer<Context<Graph<Node, Component>, Component>, Shape> edgeShapeTransformer =
        new EdgeShape.CubicCurve<Node, Component>()
        {
            @Override
            public Shape transform(Context<Graph<Node, Component>, Component> context)
            {
                String element = context.element.getComponentClassName();
                String resistor = Wire.class.getSimpleName();

                if (element.equals(resistor))
                    /** Curve */
                    return super.transform(context);
                else
                    /** Straight line */
                    return new Line2D.Double(0, 0, 1, 0);
            }
        };

        renderContext.setEdgeShapeTransformer(edgeShapeTransformer);
    }


    private void setEdgeLineColor()
    {
        Transformer<Component,Paint> paintTransformer =
                new Transformer<Component, Paint>()
                {
                    public Paint transform(Component component)
                    {
                        return component.getView().getGraphColor();
                    }
                };
        renderContext.setEdgeDrawPaintTransformer(paintTransformer);
    }

    private void setFrame(BasicVisualizationServer<Node, Component> visualServer)
    {
        JFrame frame = new JFrame("My Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(visualServer);
        frame.pack();
        frame.setVisible(true);
    }

    private void setEdgeLabelContent()
    {
        Transformer<Component,String> edgeStringTransformer =
                new Transformer<Component, String>()
                {
                    public String transform(Component component)
                    {
                        String content = component.getView().getGraphLabel();
                        return content;
                    }
                };
        renderContext.setEdgeLabelTransformer(edgeStringTransformer);
    }

    private void setEdgeLabelFont()
    {
        Transformer<Component,Font> fontTransformer =
                new Transformer<Component, Font>()
                {
                    public Font transform(Component component)
                    {
                        return new Font("Verdana", Font.BOLD, 13);
                    }
                };
        renderContext.setEdgeFontTransformer(fontTransformer);
    }

    private void disableLabelRotation()
    {
        EdgeLabelRenderer edgeLabelRenderer = renderContext.getEdgeLabelRenderer();
        edgeLabelRenderer.setRotateEdgeLabels(false);
        renderContext.setEdgeLabelRenderer(edgeLabelRenderer);
    }

    private void smallNode()
    {
        Transformer<Node,Shape> vertexShapeTransformer =
                new Transformer<Node,Shape>()
                {
                    public Shape transform(Node node)
                    {
                        return new Ellipse2D.Float(-1, -1, 2, 2);
                    }
                };
        renderContext.setVertexShapeTransformer(vertexShapeTransformer);
    }
}
