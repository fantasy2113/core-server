package com.orbis.coreserver.base.interfaces.repositories;

import com.orbis.coreserver.base.entities.CustomerAddress;

public interface ICustomerAddressRepository extends IRepository<CustomerAddress>, IRepositoryFromForeignKey<CustomerAddress> {

}
