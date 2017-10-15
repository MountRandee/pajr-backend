package pajr.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name ="userRequest")
public class UserRequest {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    private String message;
    private String city;
    private Timestamp timestamp;
    
    @ManyToOne
    @JoinColumn(name="disasterId")
    private Disaster disaster;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    public UserRequest() {}
    
    public UserRequest(String message, String city, Timestamp timestamp, Status status) {
        this.message = message;
        this.city = city;
        this.timestamp = timestamp;
        this.status = status;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public Disaster getDisaster() {
    		return disaster;
    }
    public void setDisaster(Disaster disaster) {
    		this.disaster = disaster;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
