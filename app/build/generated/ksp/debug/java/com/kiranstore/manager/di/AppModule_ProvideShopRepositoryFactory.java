package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.dao.ShopDao;
import com.kiranstore.manager.data.repository.ShopRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideShopRepositoryFactory implements Factory<ShopRepository> {
  private final Provider<ShopDao> daoProvider;

  public AppModule_ProvideShopRepositoryFactory(Provider<ShopDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ShopRepository get() {
    return provideShopRepository(daoProvider.get());
  }

  public static AppModule_ProvideShopRepositoryFactory create(Provider<ShopDao> daoProvider) {
    return new AppModule_ProvideShopRepositoryFactory(daoProvider);
  }

  public static ShopRepository provideShopRepository(ShopDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideShopRepository(dao));
  }
}
