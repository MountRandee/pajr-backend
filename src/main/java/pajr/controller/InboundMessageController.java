package pajr.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;

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
    
    @RequestMapping(value="/receive-sms", method=RequestMethod.POST)
    public String receiveSMS(HttpServletRequest request) throws TwiMLException, IOException, URISyntaxException {
        debugTwilioResponse(request);
        UserRequest userRequest = createUserRequestFromSMS(request);
        // String entity = getEntityFromWit(userRequest.getMessage());
        String entity = "fire";
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
    
    private String getEntityFromWit(String message) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String witApiUrl = "";
        URIBuilder uriBuilder = new URIBuilder(witApiUrl);
        ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.build().toString(), String.class);
        return response.getBody().toString();
    }
    
    private String createFeedback(String feedback) throws TwiMLException {
        Message messageToSend = new Message.Builder()
                .body(new Body(feedback))
                .build();
        MessagingResponse twiml = new MessagingResponse.Builder()
                .message(messageToSend)
                .build();
        return twiml.toXml();
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
