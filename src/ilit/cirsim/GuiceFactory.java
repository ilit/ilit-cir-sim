package ilit.cirsim;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Just a reference for Guice insertion in arbitrary non-guice class.
 */
class GuiceFactory
{
    private static final Injector simInjector = Guice.createInjector(new SimModule());

    public static Injector getSimInjector()
    {
        return simInjector;
    }
}
