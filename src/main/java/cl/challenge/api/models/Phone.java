package cl.challenge.api.models;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Phone {
    @Id
    @GeneratedValue
    private UUID id;
    
    private String number;
    private String citycode;
    private String contrycode;
    
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getCitycode() {
        return citycode;
    }
    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }
    public String getContrycode() {
        return contrycode;
    }
    public void setContrycode(String contrycode) {
        this.contrycode = contrycode;
    }
    
    @Override
    public String toString() {
        return "Phone [id=" + id + ", number=" + number + ", citycode=" + citycode + ", contrycode=" + contrycode + "]";
    }


}
