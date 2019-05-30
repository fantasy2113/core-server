package de.josmer.coreserver.base.interfaces.repositories;

import de.josmer.coreserver.base.entities.CustomerAddress;

public interface ICustomerAddressRepository extends IRepository<CustomerAddress>, IRepositoryFromForeignKey<CustomerAddress> {

}
