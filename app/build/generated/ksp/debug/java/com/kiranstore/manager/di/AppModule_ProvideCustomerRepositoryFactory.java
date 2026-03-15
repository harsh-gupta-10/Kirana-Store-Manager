package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.dao.CustomerDao;
import com.kiranstore.manager.data.repository.CustomerRepository;
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
public final class AppModule_ProvideCustomerRepositoryFactory implements Factory<CustomerRepository> {
  private final Provider<CustomerDao> daoProvider;

  public AppModule_ProvideCustomerRepositoryFactory(Provider<CustomerDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public CustomerRepository get() {
    return provideCustomerRepository(daoProvider.get());
  }

  public static AppModule_ProvideCustomerRepositoryFactory create(
      Provider<CustomerDao> daoProvider) {
    return new AppModule_ProvideCustomerRepositoryFactory(daoProvider);
  }

  public static CustomerRepository provideCustomerRepository(CustomerDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCustomerRepository(dao));
  }
}
