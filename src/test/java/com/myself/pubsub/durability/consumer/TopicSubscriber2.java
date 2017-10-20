package com.myself.pubsub.durability.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

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
public class TopicSubscriber2 {

    private static String topicName = "FirstTopic";
    private static String url = "tcp://localhost:61616";


    public static void main(String[] args) throws JMSException {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session = null;
        //消息订阅者
        MessageConsumer consumer = null;

        try {
            connectionFactory = new ActiveMQConnectionFactory(url);
            connection = connectionFactory.createConnection();
            /**
             * 持久订阅需要设置clientId
             * 使用相同的“clientID”，则认为是同一个消费者。
             * 两个程序使用相同的“clientID”，则同时只能有一个连接到activemq，第二个连接的会报错
             */
            connection.setClientID("bbb");

            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(topicName);
            //普通订阅
            //MessageConsumer consumer = session.createConsumer(topic);
            /**
             * 持久订阅
             * 指定对应的clientId
             */
            consumer = session.createDurableSubscriber(topic, "bbb");
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
        }
        catch (Exception e){
            e.printStackTrace();
        }
        /*finally {
            if(null != consumer ){
                consumer.close();
            }
            if(null != session){
                session.unsubscribe(topicName);
            }
            if(null != connection){
                connection.close();
            }
        }*/
    }
}
