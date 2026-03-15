package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.data.repository.ShopRepository;
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
public final class ShopViewModel_Factory implements Factory<ShopViewModel> {
  private final Provider<ShopRepository> shopRepoProvider;

  public ShopViewModel_Factory(Provider<ShopRepository> shopRepoProvider) {
    this.shopRepoProvider = shopRepoProvider;
  }

  @Override
  public ShopViewModel get() {
    return newInstance(shopRepoProvider.get());
  }

  public static ShopViewModel_Factory create(Provider<ShopRepository> shopRepoProvider) {
    return new ShopViewModel_Factory(shopRepoProvider);
  }

  public static ShopViewModel newInstance(ShopRepository shopRepo) {
    return new ShopViewModel(shopRepo);
  }
}
