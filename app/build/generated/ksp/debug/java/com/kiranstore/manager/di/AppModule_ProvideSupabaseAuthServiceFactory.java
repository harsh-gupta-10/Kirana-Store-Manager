package com.kiranstore.manager.di;

import com.kiranstore.manager.services.auth.SupabaseAuthService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AppModule_ProvideSupabaseAuthServiceFactory implements Factory<SupabaseAuthService> {
  @Override
  public SupabaseAuthService get() {
    return provideSupabaseAuthService();
  }

  public static AppModule_ProvideSupabaseAuthServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SupabaseAuthService provideSupabaseAuthService() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSupabaseAuthService());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideSupabaseAuthServiceFactory INSTANCE = new AppModule_ProvideSupabaseAuthServiceFactory();
  }
}
