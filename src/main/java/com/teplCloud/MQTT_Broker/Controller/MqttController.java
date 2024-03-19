package com.teplCloud.MQTT_Broker.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.teplCloud.MQTT_Broker.MqttGateway;

@RestController
public class MqttController {
	@Autowired
	MqttGateway mqttGateway;
	
	@PostMapping("/sendMessage")
	public ResponseEntity<?> publish(@RequestBody String mqttMessage){
		
		try {
			JsonObject convertObject = new Gson().fromJson(mqttMessage, JsonObject.class);
			mqttGateway.sendToMqtt(convertObject.get("message").toString(), convertObject.get("topic").toString());
			return ResponseEntity.ok("Sucess");
		}catch(Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.ok("Fail");
		}
		
		
	}
	
}
