package com.myself.pubsub.non_durability.producer;

/**
 * Project name activemq-first
 * <p>
 * Package name com.deppon.topic
 * <p>
 * Description:
 * <p>
 * Created by 326007
 * <p>
 * Created date 2017/6/19
 *
 * 写在前面——几个概论
 * 传输模式：持久传输-PERSISTENT，非持久传输-NON_PERSISTENT
 * 消息类型：根传输模式有关系，如果某消息使用的是持久传输，则该消息具有 Persistence性质，否则不是——Message Persistence
 * Message Durability：这个只针对订阅发布者模式，如果某个消息被持久订阅者订阅了，那么该消息就具有：Durability，否则就是NonDurability
 *
 * 普通订阅：activemq只是向当前启动的消费者发送消息。关掉的消费者，会错过很多消息，并无法再次接收这些消息
 * 持久订阅：能接收到错过的消息
 *
 */

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {

    public static void main(String[] args) {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer;
        // TextMessage message;
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createTopic("FirstTopic");//session.createQueue("FirstQueue");

            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            /**
             * 设置传输模式
             * 持久传输-PERSISTENT-消息会存储在磁盘，代理broker宕机消息仍然存在，重启后订阅者依然能获取到消息
             * 非持久传输-NON_PERSISTENT
             * 设置不持久化，此处学习，实际根据项目决定
             */
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
            sendMessage(session, producer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer producer) throws Exception {
        TextMessage message = null;
        int i = 0;
        while(true){
            i++;
            System.out.println("***************第"+i+"次发送消息");
            message  = session.createTextMessage("ActiveMq 发送的第"+ i +"条消息" );
            // 发送消息到目的地方
            Thread.sleep(1000);
            System.out.println("发送消息：" + message );
            producer.send(message);
        }
    }
}