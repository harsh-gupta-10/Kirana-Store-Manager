package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.data.repository.CustomerRepository;
import com.kiranstore.manager.data.repository.RentalRepository;
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
public final class RentalViewModel_Factory implements Factory<RentalViewModel> {
  private final Provider<RentalRepository> rentalRepoProvider;

  private final Provider<CustomerRepository> customerRepoProvider;

  public RentalViewModel_Factory(Provider<RentalRepository> rentalRepoProvider,
      Provider<CustomerRepository> customerRepoProvider) {
    this.rentalRepoProvider = rentalRepoProvider;
    this.customerRepoProvider = customerRepoProvider;
  }

  @Override
  public RentalViewModel get() {
    return newInstance(rentalRepoProvider.get(), customerRepoProvider.get());
  }

  public static RentalViewModel_Factory create(Provider<RentalRepository> rentalRepoProvider,
      Provider<CustomerRepository> customerRepoProvider) {
    return new RentalViewModel_Factory(rentalRepoProvider, customerRepoProvider);
  }

  public static RentalViewModel newInstance(RentalRepository rentalRepo,
      CustomerRepository customerRepo) {
    return new RentalViewModel(rentalRepo, customerRepo);
  }
}
