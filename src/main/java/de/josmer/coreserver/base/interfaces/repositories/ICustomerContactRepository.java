package de.josmer.coreserver.base.interfaces.repositories;

import de.josmer.coreserver.base.entities.CustomerContact;

public interface ICustomerContactRepository extends IRepository<CustomerContact>, IRepositoryFromForeignKey<CustomerContact> {

}
