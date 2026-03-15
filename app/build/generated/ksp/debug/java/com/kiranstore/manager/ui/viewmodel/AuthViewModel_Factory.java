package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.services.auth.SupabaseAuthService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<SupabaseAuthService> authServiceProvider;

  public AuthViewModel_Factory(Provider<SupabaseAuthService> authServiceProvider) {
    this.authServiceProvider = authServiceProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authServiceProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<SupabaseAuthService> authServiceProvider) {
    return new AuthViewModel_Factory(authServiceProvider);
  }

  public static AuthViewModel newInstance(SupabaseAuthService authService) {
    return new AuthViewModel(authService);
  }
}
