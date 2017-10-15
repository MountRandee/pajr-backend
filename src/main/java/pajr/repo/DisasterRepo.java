package pajr.repo;

import org.springframework.data.repository.CrudRepository;

import pajr.model.Disaster;

public interface DisasterRepo extends CrudRepository<Disaster, Integer> {
    
    Disaster findByName(String name);

}

