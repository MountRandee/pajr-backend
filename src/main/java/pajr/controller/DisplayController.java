package pajr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/display")
public class DisplayController {
    
    @RequestMapping(value="/all", method=RequestMethod.GET)
    public String getPrioritizedEmergencyRequests(@RequestParam String requestId) {
        return requestId;
        
    }
    
    @RequestMapping(value="/find", method=RequestMethod.GET)
    public String getEmergencyRequest(@RequestParam String requestId) {
        return requestId;
        
    }
    
    @RequestMapping(value="/complete", method=RequestMethod.GET)
    public String completeEmergencyRequest(@RequestParam String requestId) {
        return requestId;
        
    }
    
}
