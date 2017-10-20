package com.myself.rest_api;

import com.myself.utils.HttpClientUtils;

public class RestApiTest {
    public static void main(String[] args) {
        String restApiUrl = "http://localhost:8161/api/jolokia/read/org.apache.activemq:type=Broker,brokerName=localhost";
        String userName = "admin";
        String password = "admin";

        String resultJson = HttpClientUtils.sendGet(restApiUrl,null,userName,password);
        System.out.println("**************"+ resultJson);
    }
}
