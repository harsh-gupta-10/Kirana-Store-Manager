package com.kiranstore.manager.data.repository;

import com.kiranstore.manager.data.database.dao.BuyListDao;
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
public final class BuyListRepository_Factory implements Factory<BuyListRepository> {
  private final Provider<BuyListDao> buyListDaoProvider;

  public BuyListRepository_Factory(Provider<BuyListDao> buyListDaoProvider) {
    this.buyListDaoProvider = buyListDaoProvider;
  }

  @Override
  public BuyListRepository get() {
    return newInstance(buyListDaoProvider.get());
  }

  public static BuyListRepository_Factory create(Provider<BuyListDao> buyListDaoProvider) {
    return new BuyListRepository_Factory(buyListDaoProvider);
  }

  public static BuyListRepository newInstance(BuyListDao buyListDao) {
    return new BuyListRepository(buyListDao);
  }
}
