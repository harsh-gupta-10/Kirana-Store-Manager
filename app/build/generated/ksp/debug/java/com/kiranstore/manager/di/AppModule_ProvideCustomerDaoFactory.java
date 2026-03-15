package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.KiranDatabase;
import com.kiranstore.manager.data.database.dao.CustomerDao;
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
public final class AppModule_ProvideCustomerDaoFactory implements Factory<CustomerDao> {
  private final Provider<KiranDatabase> dbProvider;

  public AppModule_ProvideCustomerDaoFactory(Provider<KiranDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CustomerDao get() {
    return provideCustomerDao(dbProvider.get());
  }

  public static AppModule_ProvideCustomerDaoFactory create(Provider<KiranDatabase> dbProvider) {
    return new AppModule_ProvideCustomerDaoFactory(dbProvider);
  }

  public static CustomerDao provideCustomerDao(KiranDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCustomerDao(db));
  }
}
