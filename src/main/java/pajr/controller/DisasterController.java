package pajr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pajr.model.Disaster;
import pajr.repo.DisasterRepo;

@RestController
@RequestMapping(path="/disaster")
public class DisasterController {
    
    @Autowired
    private DisasterRepo disasterRepository;
    
    @RequestMapping(value="", method=RequestMethod.GET)
    public @ResponseBody Iterable<Disaster> getDisaster() {
        return disasterRepository.findAll();
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public @ResponseBody Disaster getDisaster(@PathVariable("id") Integer id) {
        return disasterRepository.findOne(id);
    }
    
    @RequestMapping(value="", method=RequestMethod.POST)
    public @ResponseBody Disaster addDisaster(
            @RequestParam String name, 
            @RequestParam Integer priority,
            @RequestParam String advice) {
        Disaster disaster = new Disaster(name, priority, advice);
        disasterRepository.save(disaster);
        return disaster;
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public @ResponseBody Disaster putDisaster(
            @PathVariable("id") Integer id,
            @RequestParam String name, 
            @RequestParam Integer priority, 
            @RequestParam String advice) {
        Disaster disaster = disasterRepository.findOne(id);
        disaster.setName(name);
        disaster.setPriority(priority);
        disaster.setAdvice(advice);
        disasterRepository.save(disaster);
        return disaster;
    }
    
    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDisaster(@PathVariable("id") Integer id) {
        disasterRepository.delete(id);
    }
}
