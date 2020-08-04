package Server;

import Server.Model.Customer;
import Server.Service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomerServiceTest {

    @Test
    public void getCorrectConditionToUpdateOrFindEntity_GetCorrectConditionToSqlRequest_PartOfSqlRequestWithCondition() {

        // Arrange
        CustomerService customerService = new CustomerService();
        Customer customer = new Customer();
        String passport = "12344566";

        customer.setPassport(passport);
        String exceptedSqlConditionPart = " WHERE passport = '12344566'";

        // Act
        String result = customerService.getCorrectConditionToUpdateOrFindEntity(customer);

        // Arrange
        Assertions.assertEquals(exceptedSqlConditionPart, result);
    }
}
