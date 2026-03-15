package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.data.repository.BuyListRepository;
import com.kiranstore.manager.data.repository.CustomerRepository;
import com.kiranstore.manager.data.repository.DebtRepository;
import com.kiranstore.manager.data.repository.TaskRepository;
import com.kiranstore.manager.services.ai.GeminiService;
import com.kiranstore.manager.services.speech.SpeechRecognitionService;
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
public final class VoiceViewModel_Factory implements Factory<VoiceViewModel> {
  private final Provider<SpeechRecognitionService> speechServiceProvider;

  private final Provider<GeminiService> geminiServiceProvider;

  private final Provider<DebtRepository> debtRepoProvider;

  private final Provider<CustomerRepository> customerRepoProvider;

  private final Provider<BuyListRepository> buyListRepoProvider;

  private final Provider<TaskRepository> taskRepoProvider;

  public VoiceViewModel_Factory(Provider<SpeechRecognitionService> speechServiceProvider,
      Provider<GeminiService> geminiServiceProvider, Provider<DebtRepository> debtRepoProvider,
      Provider<CustomerRepository> customerRepoProvider,
      Provider<BuyListRepository> buyListRepoProvider, Provider<TaskRepository> taskRepoProvider) {
    this.speechServiceProvider = speechServiceProvider;
    this.geminiServiceProvider = geminiServiceProvider;
    this.debtRepoProvider = debtRepoProvider;
    this.customerRepoProvider = customerRepoProvider;
    this.buyListRepoProvider = buyListRepoProvider;
    this.taskRepoProvider = taskRepoProvider;
  }

  @Override
  public VoiceViewModel get() {
    return newInstance(speechServiceProvider.get(), geminiServiceProvider.get(), debtRepoProvider.get(), customerRepoProvider.get(), buyListRepoProvider.get(), taskRepoProvider.get());
  }

  public static VoiceViewModel_Factory create(
      Provider<SpeechRecognitionService> speechServiceProvider,
      Provider<GeminiService> geminiServiceProvider, Provider<DebtRepository> debtRepoProvider,
      Provider<CustomerRepository> customerRepoProvider,
      Provider<BuyListRepository> buyListRepoProvider, Provider<TaskRepository> taskRepoProvider) {
    return new VoiceViewModel_Factory(speechServiceProvider, geminiServiceProvider, debtRepoProvider, customerRepoProvider, buyListRepoProvider, taskRepoProvider);
  }

  public static VoiceViewModel newInstance(SpeechRecognitionService speechService,
      GeminiService geminiService, DebtRepository debtRepo, CustomerRepository customerRepo,
      BuyListRepository buyListRepo, TaskRepository taskRepo) {
    return new VoiceViewModel(speechService, geminiService, debtRepo, customerRepo, buyListRepo, taskRepo);
  }
}
