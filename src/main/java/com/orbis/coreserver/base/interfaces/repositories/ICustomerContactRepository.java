package com.orbis.coreserver.base.interfaces.repositories;

import com.orbis.coreserver.base.entities.CustomerContact;

public interface ICustomerContactRepository extends IRepository<CustomerContact>, IRepositoryFromForeignKey<CustomerContact> {

}
