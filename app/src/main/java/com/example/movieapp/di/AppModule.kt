package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import com.example.movieapp.BuildConfig
import com.example.movieapp.api.ApiService
import com.example.movieapp.api.TmdbApiService
import com.example.movieapp.db.AppDatabase
import com.example.movieapp.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    // --- Provider for ReqRes API ---
    @Singleton
    @Provides
    @ReqresApi // Custom qualifier
    fun provideReqresRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://reqres.in/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(@ReqresApi retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    // --- Provider for TMDB API ---
    @Singleton
    @Provides
    @TmdbApi // Custom qualifier
    fun provideTmdbRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideTmdbApiService(@TmdbApi retrofit: Retrofit): TmdbApiService =
        retrofit.create(TmdbApiService::class.java)


    // --- Providers for Database ---
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "movie_app_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()
}