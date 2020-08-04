package Server.Model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;
import java.sql.Date;

@JsonTypeInfo( use = JsonTypeInfo.Id.NONE )
public class Reservation implements Serializable, IEntity {

    private Integer id;
    private Date check_in;
    private Date check_out;
    private Integer customer_id;
    private Integer room_id;
    private String roomnumber;
    private String customerpassport;
    
    public String getRoomnumber() {
        return roomnumber;
    }
    
    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }
    
    public String getCustomerpassport() {
        return customerpassport;
    }
    
    public void setCustomerpassport(String customerpassport) {
        this.customerpassport = customerpassport;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Date check_in) {
        this.check_in = check_in;
    }

    public Date getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Date check_out) {
        this.check_out = check_out;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Integer room_id) {
        this.room_id = room_id;
    }
}
