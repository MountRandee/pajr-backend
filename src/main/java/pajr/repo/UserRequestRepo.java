package pajr.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pajr.model.UserRequest;
import pajr.model.Status;

public interface UserRequestRepo  extends CrudRepository<UserRequest, Integer> {
    
	List<UserRequest> findByStatus(Status status);
}
