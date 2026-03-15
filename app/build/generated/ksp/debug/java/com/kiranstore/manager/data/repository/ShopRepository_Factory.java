package com.kiranstore.manager.data.repository;

import com.kiranstore.manager.data.database.dao.ShopDao;
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
public final class ShopRepository_Factory implements Factory<ShopRepository> {
  private final Provider<ShopDao> shopDaoProvider;

  public ShopRepository_Factory(Provider<ShopDao> shopDaoProvider) {
    this.shopDaoProvider = shopDaoProvider;
  }

  @Override
  public ShopRepository get() {
    return newInstance(shopDaoProvider.get());
  }

  public static ShopRepository_Factory create(Provider<ShopDao> shopDaoProvider) {
    return new ShopRepository_Factory(shopDaoProvider);
  }

  public static ShopRepository newInstance(ShopDao shopDao) {
    return new ShopRepository(shopDao);
  }
}
