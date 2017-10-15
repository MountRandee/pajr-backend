package pajr.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.twiml.TwiMLException;

import pajr.configuration.PajrProperties;
import pajr.model.Disaster;
import pajr.model.Status;
import pajr.model.UserRequest;
import pajr.repo.DisasterRepo;
import pajr.repo.UserRequestRepo;

@RestController
public class InboundMessageController {
    
    @Autowired
    UserRequestRepo userRequestRepo;
    
    @Autowired
    DisasterRepo disasterRepo;
    
    @Autowired
    PajrProperties pajrProperties;
    
    @RequestMapping(value="/receive-sms", method=RequestMethod.POST)
    public String receiveSMS(HttpServletRequest request) throws TwiMLException, IOException, URISyntaxException {
        if (pajrProperties.isDebugTwilioResponse()) {
            debugTwilioResponse(request);
        }
        UserRequest userRequest = createUserRequestFromSMS(request);
        String entity = getEntityFromWit(userRequest.getMessage());
        Disaster disaster = disasterRepo.findByName(entity);
        userRequest.setDisaster(disaster);
        userRequestRepo.save(userRequest);
        return disaster.getAdvice();
    }
    
    private UserRequest createUserRequestFromSMS(HttpServletRequest request) {
        String body = request.getParameter("Body");
        String city = request.getParameter("FromCity");
        
        if (body == null) {
            body = "";
        }
        
        if (city == null) {
            city = "";
        }
        
        UserRequest userRequest = new UserRequest(
                body, 
                city, 
                new Timestamp(System.currentTimeMillis()),
                disasterRepo.findOne(1),
                Status.queued);
        userRequest = userRequestRepo.save(userRequest);
        return userRequest;
    }
    
    private String getEntityFromWit(String message) throws URISyntaxException, JsonProcessingException, IOException {
        String value = "UNKNOWN";
        if (message == null || message.trim().isEmpty()) {
            return value;
        }
        String accessToken = pajrProperties.getWitAuth();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        
        RestTemplate restTemplate = new RestTemplate();
        String witApiUrl = "https://api.wit.ai/message?v=20171014&q=".concat(URLEncoder.encode(message, "UTF-8"));
        URIBuilder uriBuilder = new URIBuilder(witApiUrl);
        
        ResponseEntity<String> response = restTemplate.postForEntity(uriBuilder.build().toString(), entity, String.class);
        String responseString = response.getBody().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(responseString);
        
        JsonNode entities = jsonResponse.get("entities");
        if (entities.has("Issue")) {
            JsonNode issues = entities.findValue("Issue");
            Float highestPercentage = null;
            
            for (JsonNode node : issues) {
                Float percentage = node.get("confidence").floatValue();
                if (highestPercentage == null) {
                    highestPercentage = percentage;
                    value = node.get("value").asText();
                } else if (percentage > highestPercentage) {
                    highestPercentage = percentage;
                    value = node.get("value").asText();
                }
            }
        }
        
        return value;
    }
    
    private void debugTwilioResponse(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            System.out.println("Parameter Name - " + paramName + ", Value - " + request.getParameter(paramName));
        }
        System.out.println(request.getContentType());
    }

}
