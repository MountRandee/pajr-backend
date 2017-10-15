package pajr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pajr.model.Status;
import pajr.model.UserRequest;
import pajr.repo.UserRequestRepo;

@RestController
@RequestMapping(value="/userRequest")
public class UserRequestController {
    
    @Autowired
    private UserRequestRepo userRequestRepo;
    
    @RequestMapping(value="", method=RequestMethod.GET)
    public @ResponseBody List<UserRequest> getUserRequests(@RequestParam Status status) {
    		return userRequestRepo.findByStatus(status);
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public UserRequest getUserRequest(@PathVariable Integer id) {
        UserRequest userRequest = userRequestRepo.findOne(id);
        return userRequest;
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public @ResponseBody UserRequest putUserRequest(
    			@PathVariable Integer id,
    		    @RequestParam Status status
    		) {
        UserRequest userRequest = userRequestRepo.findOne(id);
        userRequest.setStatus(status);
        userRequestRepo.save(userRequest);
        return userRequest;
    }    
}
