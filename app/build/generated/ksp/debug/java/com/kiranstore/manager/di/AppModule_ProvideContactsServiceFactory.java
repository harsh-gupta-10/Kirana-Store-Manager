package com.kiranstore.manager.di;

import android.content.Context;
import com.kiranstore.manager.services.contacts.ContactsService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideContactsServiceFactory implements Factory<ContactsService> {
  private final Provider<Context> ctxProvider;

  public AppModule_ProvideContactsServiceFactory(Provider<Context> ctxProvider) {
    this.ctxProvider = ctxProvider;
  }

  @Override
  public ContactsService get() {
    return provideContactsService(ctxProvider.get());
  }

  public static AppModule_ProvideContactsServiceFactory create(Provider<Context> ctxProvider) {
    return new AppModule_ProvideContactsServiceFactory(ctxProvider);
  }

  public static ContactsService provideContactsService(Context ctx) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideContactsService(ctx));
  }
}
