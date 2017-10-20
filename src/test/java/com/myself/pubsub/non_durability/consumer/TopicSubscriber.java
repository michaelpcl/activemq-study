package com.myself.pubsub.non_durability.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * Project name activemq-first
 * <p>
 * Package name com.deppon.subscriber
 * <p>
 * Description:
 * <p>
 * Created by 326007
 * <p>
 * Created date 2017/6/19
 */
public class TopicSubscriber {
    private static String topicName = "FirstTopic";
    private static String url = "tcp://localhost:61616";


    public static void main(String[] args) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(topicName);

        MessageConsumer consumer = session.createConsumer(topic);
        MessageListener listner = new MessageListener() {
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("Received message : " + textMessage.getText() + "'");
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };
        //异步消费，没有次数限制，只要有消息就会触发监听
        consumer.setMessageListener(listner);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.close();
    }
}
