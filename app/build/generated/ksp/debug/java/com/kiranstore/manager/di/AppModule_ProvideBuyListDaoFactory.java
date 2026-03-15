package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.KiranDatabase;
import com.kiranstore.manager.data.database.dao.BuyListDao;
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
public final class AppModule_ProvideBuyListDaoFactory implements Factory<BuyListDao> {
  private final Provider<KiranDatabase> dbProvider;

  public AppModule_ProvideBuyListDaoFactory(Provider<KiranDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public BuyListDao get() {
    return provideBuyListDao(dbProvider.get());
  }

  public static AppModule_ProvideBuyListDaoFactory create(Provider<KiranDatabase> dbProvider) {
    return new AppModule_ProvideBuyListDaoFactory(dbProvider);
  }

  public static BuyListDao provideBuyListDao(KiranDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideBuyListDao(db));
  }
}
