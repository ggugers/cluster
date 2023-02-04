package com.example.cluster.config;

import org.apache.catalina.Context;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomTomcatServletWebServerFactoryCustomizer
        implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers(new TomcatClusterContextCustomizer());
    }

    public class TomcatClusterContextCustomizer implements TomcatContextCustomizer {
        @Override
        public void customize(final Context context) {
            context.setDistributable(true);
            System.out.println("================================================ context.getDistributable() = " + context.getDistributable());
            System.out.println("================================================ context.getDistributable() = " + context.getDistributable());
        }
    }

}
