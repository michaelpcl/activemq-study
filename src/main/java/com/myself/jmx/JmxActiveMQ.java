package com.myself.jmx;

import org.apache.log4j.Logger;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Properties;

public class JmxActiveMQ {

    private static Logger logger = Logger.getLogger(JmxActiveMQ.class);

    private String jmxUrl;
    private JMXConnector connector = null;


    public JmxActiveMQ() {
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getClassLoader().getResourceAsStream("activemq.properties"));
            this.jmxUrl = properties.get("jmx_url").toString();
        } catch (IOException e) {
            logger.warn("Could not load properties. Is file activemq-monitor.properties in classpath?", e);
        }
    }


    private JMXConnector  generatorConnect() {
        try {
            logger.debug("Connecting to url" + jmxUrl);
            JMXServiceURL url = new JMXServiceURL(jmxUrl);
            connector  = JMXConnectorFactory.newJMXConnector(url, new HashMap());
            logger.debug("Connected " + connector.getConnectionId());
        } catch (MalformedURLException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        return connector;
    }


    private void disconnect() {
        try {
            if(connector != null) {
                connector.close();
            }
        } catch (IOException e) {
            logger.error("Error closing connection", e);
        }
    }

}
