package pajr.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Disaster {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String name;
    private Integer priority;
    private String advice;
    
    protected Disaster() {}
    
    public Disaster(String name, Integer priority, String advice) {
        this.name = name;
        this.priority = priority;
        this.advice = advice;
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
        
    public String getAdvice() {
        return advice;
    }
    
    public void setAdvice(String advice) {
        this.advice = advice;
    }    
    
    @Override
    public String toString() {
        return String.format(
                "Disaster[id=%d, name='%s', priority='%s', advice='%s']",
                id, name, priority, advice);
    }
}
