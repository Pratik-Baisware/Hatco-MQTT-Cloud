package com.teplCloud.MQTT_Broker;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;


@Configuration
public class MqttBeans {
    
    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = MqttConnectOptions();

        options.setServerURIs(new String[] {"tcp://localhost:1883"});
        options.setUserName("admin");
        String pass = "12345678";
        options.setPassword(pass.toCharArray());
        options.setCleanSession(true);       

        factory.setConnectionOptions(options);

        return factory;
    }

    private MqttConnectOptions MqttConnectOptions() {
		// TODO Auto-generated method stub
		return null;
	}

    // Subscribing
    
    @Bean
	public MessageChannel mqttInputChannel(){
		return new DirectChannel();
        
    }
    
    
    @Bean
    public MessageProducer inbound() {
    	MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn", mqttPahoClientFactory(), "#");
    	
    	adapter.setCompletionTimeout(5000);
    	adapter.setConverter(new DefaultPahoMessageConverter());
    	adapter.setQos(2);
    	adapter.setOutputChannel(mqttInputChannel());
    	
    	return adapter;
    }
    
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
    	return new MessageHandler() {
			
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
				if(topic.equals("mytopic")) {
					System.out.println("This is our topic");
				}
				System.out.println(message.getPayload());
			}
		};
    }
    
    // Publishing 
    
    @Bean
    public MessageChannel mqttOutboundChannel() {
    	return new DirectChannel();
    }
    
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
    	MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttPahoClientFactory());
    	
    	messageHandler.setAsync(true);
    	messageHandler.setDefaultTopic("#");
    	
    	return messageHandler;
    }
    
}













































