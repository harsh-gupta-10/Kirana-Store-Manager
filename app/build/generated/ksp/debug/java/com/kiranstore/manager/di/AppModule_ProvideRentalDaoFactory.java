package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.KiranDatabase;
import com.kiranstore.manager.data.database.dao.RentalDao;
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
public final class AppModule_ProvideRentalDaoFactory implements Factory<RentalDao> {
  private final Provider<KiranDatabase> dbProvider;

  public AppModule_ProvideRentalDaoFactory(Provider<KiranDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RentalDao get() {
    return provideRentalDao(dbProvider.get());
  }

  public static AppModule_ProvideRentalDaoFactory create(Provider<KiranDatabase> dbProvider) {
    return new AppModule_ProvideRentalDaoFactory(dbProvider);
  }

  public static RentalDao provideRentalDao(KiranDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRentalDao(db));
  }
}
