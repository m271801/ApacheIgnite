@Configuration
public class IgniteProvider {
    private Log log = LogFactory.getLog(IgniteCacheAdapter.class);
    private final Ignite ignite;
    private boolean started = false;

    public IgniteProvider() {
        try {
            Ignition.ignite("testGrid-client");
            started = true;
        } catch (IgniteIllegalStateException e) {
            log.debug("Using the Ignite instance that has been already started.");
        }
        if (started)
            ignite = Ignition.ignite("testGrid-client");
        else {
            ignite = Ignition.start("ignite/example-hello.xml");
            ((TcpDiscoverySpi) ignite.configuration().getDiscoverySpi())
                    .getIpFinder()
                    .registerAddresses(Collections.singletonList(new InetSocketAddress("localhost", DFLT_PORT)));
        }
    }

    public Ignite getIgnite() {
        return ignite;
    }
}
