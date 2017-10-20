package com.myself.jmx;


import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class JMXTest {
    public static void main(String[] args) throws Exception {

        String surl = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";

        JMXServiceURL url = new JMXServiceURL(surl);
        JMXConnector jmxc  = JMXConnectorFactory.newJMXConnector(url, new HashMap());
        jmxc.connect();

        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        System.out.println("Domains:---------------");
        String domains[] = mbsc.getDomains();
        for (int i = 0; i < domains.length; i++) {
            System.out.println("\tDomain[" + i + "] = " + domains[i]);
        }

        System.out.println("all ObjectName：---------------");
        Set<ObjectInstance> set = mbsc.queryMBeans(null, null);
        for (Iterator<ObjectInstance> it = set.iterator(); it.hasNext();) {
            ObjectInstance oi = (ObjectInstance) it.next();
            System.out.println("\t" + oi.getObjectName());
        }

        System.out.println("org.apache.activemq:BrokerName=localhost,Type=Broker：---------------");
        ObjectName mbeanName = new ObjectName("org.apache.activemq:brokerName=localhost,type=Broker");
        MBeanInfo info = mbsc.getMBeanInfo(mbeanName);
        //获取MBean的class
        System.out.println("Class: " + info.getClassName());
        if (info.getAttributes().length > 0){
            for(MBeanAttributeInfo mbi : info.getAttributes())
                System.out.println("\t ==> Attriber：" + mbi.getName());
        }
        if (info.getOperations().length > 0){
            for(MBeanOperationInfo m : info.getOperations())
                System.out.println("\t ==> Operation：" + m.getName());
        }

        /**
         * jmx通过代理获取brokerView
         */
        System.out.println("proxy 获取brokerview：---------------");
        BrokerViewMBean proxy = MBeanServerInvocationHandler.newProxyInstance(mbsc, mbeanName, BrokerViewMBean.class, false);
        proxy.start();
        ObjectName[] tt = proxy.getQueues();
        System.out.println("********" + tt.length);
        for(int i=0;i<tt.length;i++){
            System.out.println("********" + tt[i].toString());
            ObjectName queueMbeanName = new ObjectName(tt[i].toString());
            MBeanInfo queueInfo = mbsc.getMBeanInfo(queueMbeanName);
            System.out.println("queue Class: " + queueInfo.getClassName());
            /**
             * 通过代理获取queryView
             * 获取每个queue的消息的数量
             */
            System.out.println("proxy 获取QueueView：---------------");
            QueueViewMBean proxy222 = MBeanServerInvocationHandler.newProxyInstance(mbsc, queueMbeanName, QueueViewMBean.class, false);
            Long messageNumber = proxy222.getEnqueueCount();
            System.out.println("该队列下待消费的数量：********" + messageNumber);
        }

        jmxc.close();
    }
}
