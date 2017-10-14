package pajr.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
//import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;

@RestController
public class InboundMessageController {
    
    @RequestMapping(value="/receive-sms", method=RequestMethod.POST)
    public String receiveSMS(HttpServletRequest request) throws TwiMLException, IOException {
        debugTwilioResponse(request);

        BufferedReader bufferedReader = request.getReader();
//        String message = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        
        Message messageToSend = new Message.Builder()
                .body(new Body("The message you sent was " ))
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
    }

}
