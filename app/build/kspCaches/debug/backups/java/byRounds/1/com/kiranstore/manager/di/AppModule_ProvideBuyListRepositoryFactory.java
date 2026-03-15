package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.dao.BuyListDao;
import com.kiranstore.manager.data.repository.BuyListRepository;
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
public final class AppModule_ProvideBuyListRepositoryFactory implements Factory<BuyListRepository> {
  private final Provider<BuyListDao> daoProvider;

  public AppModule_ProvideBuyListRepositoryFactory(Provider<BuyListDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public BuyListRepository get() {
    return provideBuyListRepository(daoProvider.get());
  }

  public static AppModule_ProvideBuyListRepositoryFactory create(Provider<BuyListDao> daoProvider) {
    return new AppModule_ProvideBuyListRepositoryFactory(daoProvider);
  }

  public static BuyListRepository provideBuyListRepository(BuyListDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideBuyListRepository(dao));
  }
}
