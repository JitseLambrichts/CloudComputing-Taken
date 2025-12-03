package cloud_comp.soap;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import cloud_comp.soap.service.TeamService;
import cloud_comp.soap.service.TeamServiceImpl;

public class Main
{
    public static void main( String[] args )
    {
        try {
            System.out.println("=== SOAP Service starten ===");

            TeamServiceImpl impl = new TeamServiceImpl();
            System.out.println("✓ TeamServiceImpl geïnstantieerd");

            JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
            System.out.println("✓ JaxWsServerFactoryBean aangemaakt");

            factory.setServiceClass(TeamService.class);
            factory.setAddress("http://0.0.0.0:8080/TeamService");
            factory.setServiceBean(impl);

            org.apache.cxf.endpoint.Server server = factory.create();
            System.out.println("✓ Server aangemaakt!");
            System.out.println("✓ Service gestart op: http://0.0.0.0:8080/TeamService");
            System.out.println("✓ WSDL beschikbaar op: http://0.0.0.0:8080/TeamService?wsdl");
            System.out.println("\nServer draait... (Druk CTRL+C om te stoppen)");

            System.in.read();
        } catch (Exception e) {
            System.err.println("✗ FOUT:");
            e.printStackTrace();
        }
    }
}
