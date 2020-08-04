package Server;

import Server.Model.Reception;
import Server.Service.ReceptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReceptionServiceTest {
    @Test
    public void getCorrectConditionToUpdateOrFindEntity_GetCorrectConditionToSqlRequest_PartOfSqlRequestWithCondition() {

        // Arrange
        ReceptionService receptionService = new ReceptionService();
        Reception reception = new Reception();
        String passport = "12344566";

        reception.setPassport(passport);
        String exceptedSqlConditionPart = " WHERE passport = '12344566'";

        // Act
        String result = receptionService.getCorrectConditionToUpdateOrFindEntity(reception);

        // Arrange
        Assertions.assertEquals(exceptedSqlConditionPart, result);
    }
}
