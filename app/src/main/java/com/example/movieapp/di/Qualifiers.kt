package com.example.movieapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReqresApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbApi