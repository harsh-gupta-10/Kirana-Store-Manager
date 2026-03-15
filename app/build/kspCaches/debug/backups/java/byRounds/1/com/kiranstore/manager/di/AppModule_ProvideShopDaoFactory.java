package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.KiranDatabase;
import com.kiranstore.manager.data.database.dao.ShopDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideShopDaoFactory implements Factory<ShopDao> {
  private final Provider<KiranDatabase> dbProvider;

  public AppModule_ProvideShopDaoFactory(Provider<KiranDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ShopDao get() {
    return provideShopDao(dbProvider.get());
  }

  public static AppModule_ProvideShopDaoFactory create(Provider<KiranDatabase> dbProvider) {
    return new AppModule_ProvideShopDaoFactory(dbProvider);
  }

  public static ShopDao provideShopDao(KiranDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideShopDao(db));
  }
}
