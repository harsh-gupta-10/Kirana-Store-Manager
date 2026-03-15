package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.data.repository.CustomerRepository;
import com.kiranstore.manager.data.repository.DebtRepository;
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
public final class UdhaarViewModel_Factory implements Factory<UdhaarViewModel> {
  private final Provider<DebtRepository> debtRepoProvider;

  private final Provider<CustomerRepository> customerRepoProvider;

  public UdhaarViewModel_Factory(Provider<DebtRepository> debtRepoProvider,
      Provider<CustomerRepository> customerRepoProvider) {
    this.debtRepoProvider = debtRepoProvider;
    this.customerRepoProvider = customerRepoProvider;
  }

  @Override
  public UdhaarViewModel get() {
    return newInstance(debtRepoProvider.get(), customerRepoProvider.get());
  }

  public static UdhaarViewModel_Factory create(Provider<DebtRepository> debtRepoProvider,
      Provider<CustomerRepository> customerRepoProvider) {
    return new UdhaarViewModel_Factory(debtRepoProvider, customerRepoProvider);
  }

  public static UdhaarViewModel newInstance(DebtRepository debtRepo,
      CustomerRepository customerRepo) {
    return new UdhaarViewModel(debtRepo, customerRepo);
  }
}
