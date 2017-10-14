package pajr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import pajr.model.UserRequest;
import pajr.repo.UserRequestRepo;

@RestController
@RequestMapping(value="/display")
public class DisplayController {
    
    @Autowired
    private UserRequestRepo userRequestDAO;
    
    @RequestMapping(value="", method=RequestMethod.GET)
    public List<UserRequest> getPrioritizedEmergencyRequests() {
        List<UserRequest> userRequests = Lists.newArrayList(userRequestDAO.findAll());
        return userRequests;
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public UserRequest getEmergencyRequest(@PathVariable Integer id) {
        UserRequest userRequest = userRequestDAO.findOne(id);
        return userRequest;
    }
    
/*    @RequestMapping(value="", method=RequestMethod.GET)
    public UserRequest completeEmergencyRequest(@RequestParam String status) {
        UserRequest userRequest = userRequestDAO.findByStatus(status);
        return userRequest;
    }*/
    
}
