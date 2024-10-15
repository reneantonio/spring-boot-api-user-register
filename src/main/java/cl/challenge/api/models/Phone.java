package cl.challenge.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String number;
    private String citycode;
    private String contrycode;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
