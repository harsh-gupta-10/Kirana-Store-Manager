package com.kiranstore.manager.ui.viewmodel;

import com.kiranstore.manager.data.repository.TaskRepository;
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
public final class TaskViewModel_Factory implements Factory<TaskViewModel> {
  private final Provider<TaskRepository> repoProvider;

  public TaskViewModel_Factory(Provider<TaskRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public TaskViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static TaskViewModel_Factory create(Provider<TaskRepository> repoProvider) {
    return new TaskViewModel_Factory(repoProvider);
  }

  public static TaskViewModel newInstance(TaskRepository repo) {
    return new TaskViewModel(repo);
  }
}
