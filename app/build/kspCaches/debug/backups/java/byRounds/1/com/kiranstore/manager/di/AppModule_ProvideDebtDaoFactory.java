package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.KiranDatabase;
import com.kiranstore.manager.data.database.dao.DebtDao;
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
public final class AppModule_ProvideDebtDaoFactory implements Factory<DebtDao> {
  private final Provider<KiranDatabase> dbProvider;

  public AppModule_ProvideDebtDaoFactory(Provider<KiranDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public DebtDao get() {
    return provideDebtDao(dbProvider.get());
  }

  public static AppModule_ProvideDebtDaoFactory create(Provider<KiranDatabase> dbProvider) {
    return new AppModule_ProvideDebtDaoFactory(dbProvider);
  }

  public static DebtDao provideDebtDao(KiranDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDebtDao(db));
  }
}
