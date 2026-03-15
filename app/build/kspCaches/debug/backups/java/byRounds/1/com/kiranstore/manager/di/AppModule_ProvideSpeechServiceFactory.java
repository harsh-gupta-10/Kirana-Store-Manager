package com.kiranstore.manager.di;

import android.content.Context;
import com.kiranstore.manager.services.speech.SpeechRecognitionService;
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
public final class AppModule_ProvideSpeechServiceFactory implements Factory<SpeechRecognitionService> {
  private final Provider<Context> ctxProvider;

  public AppModule_ProvideSpeechServiceFactory(Provider<Context> ctxProvider) {
    this.ctxProvider = ctxProvider;
  }

  @Override
  public SpeechRecognitionService get() {
    return provideSpeechService(ctxProvider.get());
  }

  public static AppModule_ProvideSpeechServiceFactory create(Provider<Context> ctxProvider) {
    return new AppModule_ProvideSpeechServiceFactory(ctxProvider);
  }

  public static SpeechRecognitionService provideSpeechService(Context ctx) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSpeechService(ctx));
  }
}
