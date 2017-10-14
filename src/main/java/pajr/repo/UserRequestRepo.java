package pajr.repo;

import org.springframework.data.repository.CrudRepository;

import pajr.model.UserRequest;
import pajr.model.Status;

public interface UserRequestRepo  extends CrudRepository<UserRequest, Integer> {
    
    UserRequest findByStatus(Status status);
}
