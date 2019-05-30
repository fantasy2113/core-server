package de.josmer.coreserver.repositories;

import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.entities.Customer;
import de.josmer.coreserver.base.enums.EntityTypes;
import de.josmer.coreserver.base.interfaces.connector.Columns;
import de.josmer.coreserver.base.interfaces.connector.ConnectorResponse;
import de.josmer.coreserver.base.interfaces.connector.IConnector;
import de.josmer.coreserver.base.interfaces.repositories.ICustomerRepository;

import javax.inject.Inject;

public final class CustomerRepository extends Repository<Customer> implements ICustomerRepository {

    @Inject
    public CustomerRepository(final IConnector connector) {
        super(connector, EntityTypes.CUSTOMER, Customer.class);
    }

    @Override
    public final ConnectorResponse save(final Customer entity) {
        final String statement = INSERT_INTO + getTableName()
                + OPEN
                + Columns.COMMERCIAL + COL
                + Columns.COMPANY_NAME + COL
                + Columns.TITLE + COL
                + Columns.SALUTATION + COL
                + Columns.FIRST_NAME + COL
                + Columns.LAST_NAME + COL
                + Columns.STREET_NAME + COL
                + Columns.STREET_NR + COL
                + Columns.ZIP_CODE + COL
                + Columns.PLACE + COL
                + Columns.TELEPHONE + COL
                + Columns.EMAIL + COL
                + Columns.FAX + COL
                + Columns.CREATED + COL
                + Columns.MODIFIED
                + CLOSE
                + VALUES
                + OPEN
                + APO + parseBoolean(entity.isCommercial()) + APO + COL
                + APO + entity.getCompanyName() + APO + COL
                + APO + entity.getPerson().getTitle() + APO + COL
                + APO + entity.getPerson().getSalutation() + APO + COL
                + APO + entity.getPerson().getFirstName() + APO + COL
                + APO + entity.getPerson().getLastName() + APO + COL
                + APO + entity.getAddress().getStreetName() + APO + COL
                + APO + entity.getAddress().getStreetNr() + APO + COL
                + APO + entity.getAddress().getZipCode() + APO + COL
                + APO + entity.getAddress().getPlace() + APO + COL
                + APO + entity.getAddress().getTelephone() + APO + COL
                + APO + entity.getAddress().getEmail() + APO + COL
                + APO + entity.getAddress().getFax() + APO + COL
                + APO + Toolbox.dateTimeNow() + APO + COL
                + APO + Toolbox.dateTimeNow() + APO
                + CLOSE + END;

        return execute(statement);
    }

    @Override
    public final ConnectorResponse update(final Customer entity) {
        final String statement = UPDATE + getTableName() + SET
                + Columns.COMMERCIAL + EQ + APO + parseBoolean(entity.isCommercial()) + APO + COL
                + Columns.COMPANY_NAME + EQ + APO + entity.getCompanyName() + APO + COL
                + Columns.TITLE + EQ + APO + entity.getPerson().getTitle() + APO + COL
                + Columns.SALUTATION + EQ + APO + entity.getPerson().getSalutation() + APO + COL
                + Columns.FIRST_NAME + EQ + APO + entity.getPerson().getFirstName() + APO + COL
                + Columns.LAST_NAME + EQ + APO + entity.getPerson().getLastName() + APO + COL
                + Columns.STREET_NAME + EQ + APO + entity.getAddress().getStreetName() + APO + COL
                + Columns.STREET_NR + EQ + APO + entity.getAddress().getStreetNr() + APO + COL
                + Columns.ZIP_CODE + EQ + APO + entity.getAddress().getZipCode() + APO + COL
                + Columns.PLACE + EQ + APO + entity.getAddress().getPlace() + APO + COL
                + Columns.TELEPHONE + EQ + APO + entity.getAddress().getTelephone() + APO + COL
                + Columns.EMAIL + EQ + APO + entity.getAddress().getEmail() + APO + COL
                + Columns.FAX + EQ + APO + entity.getAddress().getFax() + APO + COL
                + Columns.MODIFIED + EQ + APO + Toolbox.dateTimeNow() + APO
                + getEqId(entity.getId());

        return execute(statement);
    }
}
