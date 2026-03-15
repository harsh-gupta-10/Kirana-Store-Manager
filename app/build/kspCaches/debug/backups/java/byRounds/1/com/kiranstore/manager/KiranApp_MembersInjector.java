package com.kiranstore.manager;

import com.kiranstore.manager.data.database.KiranDatabase;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class KiranApp_MembersInjector implements MembersInjector<KiranApp> {
  private final Provider<KiranDatabase> databaseProvider;

  public KiranApp_MembersInjector(Provider<KiranDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  public static MembersInjector<KiranApp> create(Provider<KiranDatabase> databaseProvider) {
    return new KiranApp_MembersInjector(databaseProvider);
  }

  @Override
  public void injectMembers(KiranApp instance) {
    injectDatabase(instance, databaseProvider.get());
  }

  @InjectedFieldSignature("com.kiranstore.manager.KiranApp.database")
  public static void injectDatabase(KiranApp instance, KiranDatabase database) {
    instance.database = database;
  }
}
