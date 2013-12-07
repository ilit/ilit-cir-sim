package ilit.cirsim;

public class Main
{
    private static IlitCircuitSimulator ilitCircuitSimulator;

    private Main()
    {
        super();
    }

    public static void main(String[] args)
    {
        ilitCircuitSimulator = GuiceFactory.getSimInjector().getInstance(IlitCircuitSimulator.class);
    }
}
