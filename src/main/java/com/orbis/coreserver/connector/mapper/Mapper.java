package com.orbis.coreserver.connector.mapper;

import com.orbis.coreserver.base.Toolbox;
import com.orbis.coreserver.base.entities.*;
import com.orbis.coreserver.base.entities.beans.AddressBean;
import com.orbis.coreserver.base.entities.beans.PersonBean;
import com.orbis.coreserver.base.enums.EntityTypes;
import com.orbis.coreserver.base.interfaces.connector.Columns;
import com.orbis.coreserver.base.interfaces.connector.IMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Mapper implements IMapper {

    @Override
    public final <E extends AbstractEntity> E mapTo(final EntityTypes entityTyp, final Class<E> classTyp, final ResultSet rs) throws SQLException {
        try {
            switch (entityTyp) {
                case USER:
                    return classTyp.cast(mapUser(rs));
                case USER_RIGHT:
                    return classTyp.cast(mapService(rs));
                case CUSTOMER_ADDRESS:
                    return classTyp.cast(mapAddress(rs));
                case CUSTOMER:
                    return classTyp.cast(mapCustomer(rs));
                case CUSTOMER_CONTACT:
                    return classTyp.cast(mapContact(rs));
                default:
                    throw new SQLException("mapping failed");
            }
        } catch (Exception ex) {
            throw new SQLException(ex.getMessage());
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User entity = new User();
        entity.setIsActive(rs.getBoolean(Columns.ACTIVE));
        mapToAbstractEntity(entity, rs);
        entity.setUserEmail(rs.getString(Columns.EMAIL));
        entity.setPw("");
        entity.setLastLogin(Toolbox.parseDateTime(rs.getTimestamp(Columns.LAST_LOGIN)));
        return entity;
    }

    private UserRight mapService(ResultSet rs) throws SQLException {
        UserRight entity = new UserRight();
        mapToAbstractEntity(entity, rs);
        entity.setUserId(rs.getInt(Columns.USER_ID));
        entity.setService(rs.getString(Columns.SERVICE));
        entity.setServiceRight(rs.getString(Columns.SERVICE_RIGHT));
        return entity;
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer entity = new Customer();
        entity.setAddress(addressBean(rs));
        entity.setPerson(personBean(rs));
        mapToAbstractEntity(entity, rs);
        entity.setCommercial(rs.getBoolean(Columns.COMMERCIAL));
        entity.setCompanyName(rs.getString(Columns.COMPANY_NAME));
        return entity;
    }

    private CustomerContact mapContact(ResultSet rs) throws SQLException {
        CustomerContact entity = new CustomerContact();
        mapToAbstractEntity(entity, rs);
        entity.setAddress(addressBean(rs));
        entity.setPerson(personBean(rs));
        entity.setCustomerId(rs.getInt(Columns.CUSTOMER_ID));
        return entity;
    }

    private CustomerAddress mapAddress(ResultSet rs) throws SQLException {
        CustomerAddress entity = new CustomerAddress();
        mapToAbstractEntity(entity, rs);
        entity.setAddress(addressBean(rs));
        entity.setCustomerId(rs.getInt(Columns.CUSTOMER_ID));
        entity.setType(rs.getString(Columns.TYPE));
        return entity;
    }

    private void mapToAbstractEntity(AbstractEntity entity, ResultSet rs) throws SQLException {
        entity.setId(rs.getInt(Columns.ID));
        entity.setCreated(Toolbox.parseDateTime(rs.getTimestamp(Columns.CREATED)));
        entity.setModified(Toolbox.parseDateTime(rs.getTimestamp(Columns.MODIFIED)));
    }

    private AddressBean addressBean(ResultSet rs) throws SQLException {
        AddressBean bean = new AddressBean();
        bean.setStreetName(rs.getString(Columns.STREET_NAME));
        bean.setStreetNr(rs.getString(Columns.STREET_NR));
        bean.setZipCode(rs.getString(Columns.ZIP_CODE));
        bean.setPlace(rs.getString(Columns.PLACE));
        bean.setTelephone(rs.getString(Columns.TELEPHONE));
        bean.setEmail(rs.getString(Columns.EMAIL));
        bean.setFax(rs.getString(Columns.FAX));
        return bean;
    }

    private PersonBean personBean(ResultSet rs) throws SQLException {
        PersonBean bean = new PersonBean();
        bean.setTitle(rs.getString(Columns.TITLE));
        bean.setSalutation(rs.getString(Columns.SALUTATION));
        bean.setFirstName(rs.getString(Columns.FIRST_NAME));
        bean.setLastName(rs.getString(Columns.LAST_NAME));
        return bean;
    }
}
