package com.kiranstore.manager.services.auth;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SupabaseAuthService_Factory implements Factory<SupabaseAuthService> {
  @Override
  public SupabaseAuthService get() {
    return newInstance();
  }

  public static SupabaseAuthService_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SupabaseAuthService newInstance() {
    return new SupabaseAuthService();
  }

  private static final class InstanceHolder {
    private static final SupabaseAuthService_Factory INSTANCE = new SupabaseAuthService_Factory();
  }
}
