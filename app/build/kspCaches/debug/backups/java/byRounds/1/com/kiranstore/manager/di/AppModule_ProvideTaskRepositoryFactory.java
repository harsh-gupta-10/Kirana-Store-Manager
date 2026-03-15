package com.kiranstore.manager.di;

import com.kiranstore.manager.data.database.dao.TaskDao;
import com.kiranstore.manager.data.repository.TaskRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AppModule_ProvideTaskRepositoryFactory implements Factory<TaskRepository> {
  private final Provider<TaskDao> daoProvider;

  public AppModule_ProvideTaskRepositoryFactory(Provider<TaskDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public TaskRepository get() {
    return provideTaskRepository(daoProvider.get());
  }

  public static AppModule_ProvideTaskRepositoryFactory create(Provider<TaskDao> daoProvider) {
    return new AppModule_ProvideTaskRepositoryFactory(daoProvider);
  }

  public static TaskRepository provideTaskRepository(TaskDao dao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTaskRepository(dao));
  }
}
