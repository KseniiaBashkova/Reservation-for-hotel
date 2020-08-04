package Server.Model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use=JsonTypeInfo.Id.CLASS,
        property="_class")
@JsonSubTypes({
        @JsonSubTypes.Type(value=Room.class, name= "Room"),
        @JsonSubTypes.Type(value=Reservation.class, name="Reservation"),
        @JsonSubTypes.Type(value=Customer.class, name="Customer"),
        @JsonSubTypes.Type(value=Reception.class, name="Reception"),
})
public interface IEntity {

    Integer getId();
    void setId(Integer id);
}