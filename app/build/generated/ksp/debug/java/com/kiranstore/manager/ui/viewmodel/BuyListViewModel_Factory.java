package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.data.repository.BuyListRepository;
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
public final class BuyListViewModel_Factory implements Factory<BuyListViewModel> {
  private final Provider<BuyListRepository> repoProvider;

  public BuyListViewModel_Factory(Provider<BuyListRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public BuyListViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static BuyListViewModel_Factory create(Provider<BuyListRepository> repoProvider) {
    return new BuyListViewModel_Factory(repoProvider);
  }

  public static BuyListViewModel newInstance(BuyListRepository repo) {
    return new BuyListViewModel(repo);
  }
}
