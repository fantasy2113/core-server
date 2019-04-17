package com.orbis.coreserver.repositories;

import com.orbis.coreserver.base.Toolbox;
import com.orbis.coreserver.base.entities.CustomerAddress;
import com.orbis.coreserver.base.enums.EntityTypes;
import com.orbis.coreserver.base.interfaces.connector.Columns;
import com.orbis.coreserver.base.interfaces.connector.ConnectorResponse;
import com.orbis.coreserver.base.interfaces.connector.IConnector;
import com.orbis.coreserver.base.interfaces.connector.Query;
import com.orbis.coreserver.base.interfaces.repositories.ICustomerAddressRepository;

import javax.inject.Inject;
import java.util.List;

public final class CustomerAddressRepository extends Repository<CustomerAddress> implements ICustomerAddressRepository {

    @Inject
    public CustomerAddressRepository(final IConnector connector) {
        super(connector, EntityTypes.CUSTOMER_ADDRESS, CustomerAddress.class);
    }

    @Override
    public final ConnectorResponse save(final CustomerAddress entity) {
        final String statement = INSERT_INTO + getTableName()
                + OPEN
                + Columns.CUSTOMER_ID + COL
                + Columns.TYPE + COL
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
                + APO + entity.getCustomerId() + APO + COL
                + APO + entity.getType() + APO + COL
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

        return getConnector().update(new Query<>(getEntityTyp(), getClassTyp(), statement));
    }

    @Override
    public final ConnectorResponse update(final CustomerAddress entity) {
        final String statement = UPDATE + getTableName() + SET
                + Columns.CUSTOMER_ID + EQ + APO + entity.getCustomerId() + APO + COL
                + Columns.TYPE + EQ + APO + entity.getType() + APO + COL
                + Columns.STREET_NAME + EQ + APO + entity.getAddress().getStreetName() + APO + COL
                + Columns.STREET_NR + EQ + APO + entity.getAddress().getStreetNr() + APO + COL
                + Columns.ZIP_CODE + EQ + APO + entity.getAddress().getZipCode() + APO + COL
                + Columns.PLACE + EQ + APO + entity.getAddress().getPlace() + APO + COL
                + Columns.TELEPHONE + EQ + APO + entity.getAddress().getTelephone() + APO + COL
                + Columns.EMAIL + EQ + APO + entity.getAddress().getEmail() + APO + COL
                + Columns.FAX + EQ + APO + entity.getAddress().getFax() + APO + COL
                + Columns.MODIFIED + EQ + APO + Toolbox.dateTimeNow() + APO
                + getEqId(entity.getId());

        return getConnector().update(new Query<>(getEntityTyp(), getClassTyp(), statement));
    }

    @Override
    public final List<CustomerAddress> getAll(final int foreignKey) {
        return all(foreignKey, Columns.CUSTOMER_ID);
    }
}
