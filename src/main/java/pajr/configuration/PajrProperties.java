package pajr.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:pajr.properties")
public class PajrProperties {
    
    @Value("${witAuth}")
    private String witAuth;

    public String getWitAuth() {
        return witAuth;
    }

    public void setWitAuth(String witAuth) {
        this.witAuth = witAuth;
    }
    
}
