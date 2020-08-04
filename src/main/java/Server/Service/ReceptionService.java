package Server.Service;

import Server.Model.IEntity;
import Server.Model.Reception;

/**
 * Represent reception service to connection with database.
 */
public class ReceptionService extends BaseService {

    static ReceptionService singleInstance;

    public static BaseService getService() {

        if (singleInstance == null) {
            singleInstance = new ReceptionService();
        }

        return singleInstance;
    }

    /**
     * Get correct attribute
     *
     * @param receptionist
     * @return
     */
    @Override
    public String getCorrectConditionToUpdateOrFindEntity(IEntity receptionist) {
        return " WHERE passport = '" + ((Reception) receptionist).getPassport() + '\'';
    }
}