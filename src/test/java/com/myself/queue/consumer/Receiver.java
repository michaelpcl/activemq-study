package com.myself.queue.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * Project name activemq-first
 * <p>
 * Package name com.deppon.qconsumer
 * <p>
 * Description:
 * <p>
 * Created by 326007
 * <p>
 * Created date 2017/6/19
 */
public class Receiver {
    private static String queueName = "FirstQueue";
    private static String url = "tcp://localhost:61616";


    public static void main(String[] args) throws JMSException {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue(queueName);

        MessageConsumer consumer = session.createConsumer(queue);
        MessageListener listner = new MessageListener() {
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("receiver1 Received message : " + textMessage.getText() + "'");
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };
        consumer.setMessageListener(listner);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.close();
    }
}
