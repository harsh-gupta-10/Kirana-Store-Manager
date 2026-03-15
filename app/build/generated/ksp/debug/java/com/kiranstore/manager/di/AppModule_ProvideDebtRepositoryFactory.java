package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.dao.DebtDao;
import com.kiranstore.manager.data.repository.DebtRepository;
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
public final class AppModule_ProvideDebtRepositoryFactory implements Factory<DebtRepository> {
  private final Provider<DebtDao> daoProvider;

  public AppModule_ProvideDebtRepositoryFactory(Provider<DebtDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public DebtRepository get() {
    return provideDebtRepository(daoProvider.get());
  }

  public static AppModule_ProvideDebtRepositoryFactory create(Provider<DebtDao> daoProvider) {
    return new AppModule_ProvideDebtRepositoryFactory(daoProvider);
  }

  public static DebtRepository provideDebtRepository(DebtDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDebtRepository(dao));
  }
}
