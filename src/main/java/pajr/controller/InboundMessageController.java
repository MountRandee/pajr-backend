package pajr.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
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
        debugTwilioResponse(request);
        UserRequest userRequest = createUserRequestFromSMS(request);
        String entity = getEntityFromWit(userRequest.getMessage());
        Disaster disaster = disasterRepo.findByName(entity);
        userRequest.setDisaster(disaster);
        userRequestRepo.save(userRequest);
        return createFeedback(disaster.getAdvice());
    }
    
    private UserRequest createUserRequestFromSMS(HttpServletRequest request) {
        UserRequest userRequest = new UserRequest(
                request.getParameter("Body"), 
                request.getParameter("FromCity"), 
                new Timestamp(System.currentTimeMillis()),
                Status.queued);
        userRequest = userRequestRepo.save(userRequest);
        return userRequest;
    }
    
    private String getEntityFromWit(String message) throws URISyntaxException, JsonProcessingException, IOException {
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
        
        String value = "UNKNOWN";
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
    
    private String createFeedback(String feedback) throws TwiMLException {
        return feedback;
/*        Message messageToSend = new Message.Builder()
                .body(new Body(feedback))
                .build();
        MessagingResponse twiml = new MessagingResponse.Builder()
                .message(messageToSend)
                .build();
        
        return messageToSend.getBody().toString();*/
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
