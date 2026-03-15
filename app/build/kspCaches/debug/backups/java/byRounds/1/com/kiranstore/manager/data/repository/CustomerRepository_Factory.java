package com.kiranstore.manager.data.repository;

import com.kiranstore.manager.data.database.dao.CustomerDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class CustomerRepository_Factory implements Factory<CustomerRepository> {
  private final Provider<CustomerDao> customerDaoProvider;

  public CustomerRepository_Factory(Provider<CustomerDao> customerDaoProvider) {
    this.customerDaoProvider = customerDaoProvider;
  }

  @Override
  public CustomerRepository get() {
    return newInstance(customerDaoProvider.get());
  }

  public static CustomerRepository_Factory create(Provider<CustomerDao> customerDaoProvider) {
    return new CustomerRepository_Factory(customerDaoProvider);
  }

  public static CustomerRepository newInstance(CustomerDao customerDao) {
    return new CustomerRepository(customerDao);
  }
}
