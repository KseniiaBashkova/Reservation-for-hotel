package Server.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import java.sql.Date;

@JsonTypeInfo( use = JsonTypeInfo.Id.NONE )
public class Customer implements Serializable, IEntity {

    private Integer id;
    private String first_name;
    private String second_name;
    private String passport;
    private Date day_of_birth;
    private String phone_number;
    private String email;
    @JsonIgnore
    private Integer total;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setTotal(Integer total) {
        this.total = total;
    }
    
    public Integer getTotal() {
        return total;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Date getDay_of_birth() {
        return day_of_birth;
    }

    public void setDay_of_birth(Date day_of_birth) {
        this.day_of_birth = day_of_birth;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
