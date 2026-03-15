package com.kiranstore.manager.services.speech;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SpeechRecognitionService_Factory implements Factory<SpeechRecognitionService> {
  private final Provider<Context> contextProvider;

  public SpeechRecognitionService_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SpeechRecognitionService get() {
    return newInstance(contextProvider.get());
  }

  public static SpeechRecognitionService_Factory create(Provider<Context> contextProvider) {
    return new SpeechRecognitionService_Factory(contextProvider);
  }

  public static SpeechRecognitionService newInstance(Context context) {
    return new SpeechRecognitionService(context);
  }
}
