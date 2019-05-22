import org.apache.http.HttpResponse;

import org.apache.http.NameValuePair;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;

import org.apache.http.protocol.HTTP;

import java.io.IOException;

import java.net.InetAddress;

import java.net.UnknownHostException;

import java.util.concurrent.TimeoutException;

import org.apache.http.impl.client.*;

import com.rabbitmq.client.Channel;

import com.rabbitmq.client.Connection;

import com.rabbitmq.client.ConnectionFactory;

import com.rabbitmq.client.DeliverCallback;

public class Main {

    private final static String QUEUE_NAME = "ignite-queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("idontknower.ddns.net");

        factory.setPort(15000);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

// channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");

            HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

// String message = "{ \"name\":\"Товар\", \"cost\": 101}";

            try {

                HttpPost request = new HttpPost("http://0.0.0.0:9200/parser/sites");

                StringEntity params = new StringEntity(message, HTTP.UTF_8);

                request.addHeader("content-type", "application/json; charset=utf-8");

                request.setEntity(params);

                HttpResponse response = httpClient.execute(request);

                System.out.println(response);

//handle response here...

            } catch (Exception ex) {

//handle exception here

            }

            System.out.println(" [x] Received '" + message + "'");

        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

    }

}