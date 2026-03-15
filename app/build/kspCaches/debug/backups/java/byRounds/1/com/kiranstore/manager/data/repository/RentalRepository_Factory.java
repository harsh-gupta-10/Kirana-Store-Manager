package com.kiranstore.manager.data.repository;

import com.kiranstore.manager.data.database.dao.RentalDao;
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
public final class RentalRepository_Factory implements Factory<RentalRepository> {
  private final Provider<RentalDao> rentalDaoProvider;

  public RentalRepository_Factory(Provider<RentalDao> rentalDaoProvider) {
    this.rentalDaoProvider = rentalDaoProvider;
  }

  @Override
  public RentalRepository get() {
    return newInstance(rentalDaoProvider.get());
  }

  public static RentalRepository_Factory create(Provider<RentalDao> rentalDaoProvider) {
    return new RentalRepository_Factory(rentalDaoProvider);
  }

  public static RentalRepository newInstance(RentalDao rentalDao) {
    return new RentalRepository(rentalDao);
  }
}
