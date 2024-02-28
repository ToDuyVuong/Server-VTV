package hcmute.kltn.vtv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableScheduling
@SpringBootApplication
public class ServerVtvApplication {

    private static final Logger logger = LoggerFactory.getLogger(ServerVtvApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ServerVtvApplication.class, args);

        try {
            InetAddress ip = InetAddress.getLocalHost();

            logger.info("""
                   
                   
                    IP Address: {}, {}
                    Localhost:  http://localhost:8585/swagger-ui/index.html
                          
                    """, ip.getHostAddress(), ip.getCanonicalHostName());
        } catch (UnknownHostException e) {
            logger.error("An error occurred while getting the local host address.", e);
        }
    }

}
