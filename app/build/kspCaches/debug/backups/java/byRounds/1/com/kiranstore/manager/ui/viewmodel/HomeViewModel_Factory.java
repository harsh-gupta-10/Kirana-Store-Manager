package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.data.repository.BuyListRepository;
import com.kiranstore.manager.data.repository.CustomerRepository;
import com.kiranstore.manager.data.repository.DebtRepository;
import com.kiranstore.manager.data.repository.RentalRepository;
import com.kiranstore.manager.data.repository.ShopRepository;
import com.kiranstore.manager.data.repository.TaskRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<ShopRepository> shopRepoProvider;

  private final Provider<DebtRepository> debtRepoProvider;

  private final Provider<RentalRepository> rentalRepoProvider;

  private final Provider<TaskRepository> taskRepoProvider;

  private final Provider<BuyListRepository> buyListRepoProvider;

  private final Provider<CustomerRepository> customerRepoProvider;

  public HomeViewModel_Factory(Provider<ShopRepository> shopRepoProvider,
      Provider<DebtRepository> debtRepoProvider, Provider<RentalRepository> rentalRepoProvider,
      Provider<TaskRepository> taskRepoProvider, Provider<BuyListRepository> buyListRepoProvider,
      Provider<CustomerRepository> customerRepoProvider) {
    this.shopRepoProvider = shopRepoProvider;
    this.debtRepoProvider = debtRepoProvider;
    this.rentalRepoProvider = rentalRepoProvider;
    this.taskRepoProvider = taskRepoProvider;
    this.buyListRepoProvider = buyListRepoProvider;
    this.customerRepoProvider = customerRepoProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(shopRepoProvider.get(), debtRepoProvider.get(), rentalRepoProvider.get(), taskRepoProvider.get(), buyListRepoProvider.get(), customerRepoProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<ShopRepository> shopRepoProvider,
      Provider<DebtRepository> debtRepoProvider, Provider<RentalRepository> rentalRepoProvider,
      Provider<TaskRepository> taskRepoProvider, Provider<BuyListRepository> buyListRepoProvider,
      Provider<CustomerRepository> customerRepoProvider) {
    return new HomeViewModel_Factory(shopRepoProvider, debtRepoProvider, rentalRepoProvider, taskRepoProvider, buyListRepoProvider, customerRepoProvider);
  }

  public static HomeViewModel newInstance(ShopRepository shopRepo, DebtRepository debtRepo,
      RentalRepository rentalRepo, TaskRepository taskRepo, BuyListRepository buyListRepo,
      CustomerRepository customerRepo) {
    return new HomeViewModel(shopRepo, debtRepo, rentalRepo, taskRepo, buyListRepo, customerRepo);
  }
}
