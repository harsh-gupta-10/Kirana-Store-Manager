package com.kiranstore.manager.di

import android.content.Context
import com.kiranstore.manager.data.database.KiranDatabase
import com.kiranstore.manager.data.database.dao.*
import com.kiranstore.manager.data.repository.*
import com.kiranstore.manager.services.ai.GeminiService
import com.kiranstore.manager.services.auth.SupabaseAuthService
import com.kiranstore.manager.services.contacts.ContactsService
import com.kiranstore.manager.services.speech.SpeechRecognitionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): KiranDatabase =
        KiranDatabase.create(context)

    @Provides fun provideShopDao(db: KiranDatabase): ShopDao = db.shopDao()
    @Provides fun provideCustomerDao(db: KiranDatabase): CustomerDao = db.customerDao()
    @Provides fun provideDebtDao(db: KiranDatabase): DebtDao = db.debtDao()
    @Provides fun provideRentalDao(db: KiranDatabase): RentalDao = db.rentalDao()
    @Provides fun provideBuyListDao(db: KiranDatabase): BuyListDao = db.buyListDao()
    @Provides fun provideTaskDao(db: KiranDatabase): TaskDao = db.taskDao()

    @Provides @Singleton
    fun provideShopRepository(dao: ShopDao): ShopRepository = ShopRepository(dao)
    @Provides @Singleton
    fun provideCustomerRepository(dao: CustomerDao): CustomerRepository = CustomerRepository(dao)
    @Provides @Singleton
    fun provideDebtRepository(dao: DebtDao): DebtRepository = DebtRepository(dao)
    @Provides @Singleton
    fun provideRentalRepository(dao: RentalDao): RentalRepository = RentalRepository(dao)
    @Provides @Singleton
    fun provideBuyListRepository(dao: BuyListDao): BuyListRepository = BuyListRepository(dao)
    @Provides @Singleton
    fun provideTaskRepository(dao: TaskDao): TaskRepository = TaskRepository(dao)

    @Provides @Singleton
    fun provideGeminiService(): GeminiService = GeminiService()

    @Provides @Singleton
    fun provideSpeechService(@ApplicationContext ctx: Context): SpeechRecognitionService =
        SpeechRecognitionService(ctx)

    @Provides @Singleton
    fun provideContactsService(@ApplicationContext ctx: Context): ContactsService =
        ContactsService(ctx)
}
