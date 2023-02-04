package com.example.cluster.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class TomcatStaticClusterConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Value("${objname}")
    private String objname;

    @Override
    public void customize(final TomcatServletWebServerFactory factory) {

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        factory.addContextCustomizers(new TomcatStaticClusterContextCustomizer(objname));
    }
}
