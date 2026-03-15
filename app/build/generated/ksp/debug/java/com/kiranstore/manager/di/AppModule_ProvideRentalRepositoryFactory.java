package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.dao.RentalDao;
import com.kiranstore.manager.data.repository.RentalRepository;
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
public final class AppModule_ProvideRentalRepositoryFactory implements Factory<RentalRepository> {
  private final Provider<RentalDao> daoProvider;

  public AppModule_ProvideRentalRepositoryFactory(Provider<RentalDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public RentalRepository get() {
    return provideRentalRepository(daoProvider.get());
  }

  public static AppModule_ProvideRentalRepositoryFactory create(Provider<RentalDao> daoProvider) {
    return new AppModule_ProvideRentalRepositoryFactory(daoProvider);
  }

  public static RentalRepository provideRentalRepository(RentalDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRentalRepository(dao));
  }
}
