package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.data.repository.CustomerRepository;
import com.kiranstore.manager.services.contacts.ContactsService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class CustomerViewModel_Factory implements Factory<CustomerViewModel> {
  private final Provider<CustomerRepository> customerRepoProvider;

  private final Provider<ContactsService> contactsServiceProvider;

  public CustomerViewModel_Factory(Provider<CustomerRepository> customerRepoProvider,
      Provider<ContactsService> contactsServiceProvider) {
    this.customerRepoProvider = customerRepoProvider;
    this.contactsServiceProvider = contactsServiceProvider;
  }

  @Override
  public CustomerViewModel get() {
    return newInstance(customerRepoProvider.get(), contactsServiceProvider.get());
  }

  public static CustomerViewModel_Factory create(Provider<CustomerRepository> customerRepoProvider,
      Provider<ContactsService> contactsServiceProvider) {
    return new CustomerViewModel_Factory(customerRepoProvider, contactsServiceProvider);
  }

  public static CustomerViewModel newInstance(CustomerRepository customerRepo,
      ContactsService contactsService) {
    return new CustomerViewModel(customerRepo, contactsService);
  }
}
