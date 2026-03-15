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
public final class UdhaarDetailViewModel_Factory implements Factory<UdhaarDetailViewModel> {
  private final Provider<DebtRepository> debtRepoProvider;

  private final Provider<CustomerRepository> customerRepoProvider;

  public UdhaarDetailViewModel_Factory(Provider<DebtRepository> debtRepoProvider,
      Provider<CustomerRepository> customerRepoProvider) {
    this.debtRepoProvider = debtRepoProvider;
    this.customerRepoProvider = customerRepoProvider;
  }

  @Override
  public UdhaarDetailViewModel get() {
    return newInstance(debtRepoProvider.get(), customerRepoProvider.get());
  }

  public static UdhaarDetailViewModel_Factory create(Provider<DebtRepository> debtRepoProvider,
      Provider<CustomerRepository> customerRepoProvider) {
    return new UdhaarDetailViewModel_Factory(debtRepoProvider, customerRepoProvider);
  }

  public static UdhaarDetailViewModel newInstance(DebtRepository debtRepo,
      CustomerRepository customerRepo) {
    return new UdhaarDetailViewModel(debtRepo, customerRepo);
  }
}
