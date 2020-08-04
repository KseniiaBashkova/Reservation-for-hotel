package Server.Model;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;

@JsonTypeInfo( use = JsonTypeInfo.Id.NONE )
public class Room implements Serializable, IEntity {

    private Integer id;
    private Integer room_number;
    private Float price;
    private String description;
    private Boolean vip;
    private Integer capacity;

    public Integer getRoom_number() {
        return room_number;
    }

    public void setRoom_number(Integer roomNumber) {
        this.room_number = roomNumber;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
