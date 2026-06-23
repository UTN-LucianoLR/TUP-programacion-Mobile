package com.app.partidos.di

import android.content.Context
import androidx.room.Room
import com.app.partidos.BuildConfig
import com.app.partidos.data.local.AppDatabase
import com.app.partidos.data.local.dao.CompraDao
import com.app.partidos.data.local.dao.PartidoDao
import com.app.partidos.data.local.dao.UsuarioDao
import com.app.partidos.data.remote.PartidosApi
import com.app.partidos.data.repository.AuthRepositoryImpl
import com.app.partidos.data.repository.PartidosRepositoryImpl
import com.app.partidos.domain.repository.AuthRepository
import com.app.partidos.domain.repository.PartidosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.app.partidos.data.local.preferences.AuthPreferences
import com.app.partidos.data.remote.interceptor.AuthInterceptor
import com.app.partidos.data.remote.AuthApiService

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- ROOM ---
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "partidos_db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    fun provideUsuarioDao(db: AppDatabase): UsuarioDao = db.usuarioDao()

    @Provides
    fun providePartidoDao(db: AppDatabase): PartidoDao = db.partidoDao()

    @Provides
    fun provideCompraDao(db: AppDatabase): CompraDao = db.compraDao()

    // --- RETROFIT ---
    @Provides
    @Singleton
    fun provideAuthPreferences(@ApplicationContext context: Context): AuthPreferences {
        return AuthPreferences(context)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(authPreferences: AuthPreferences): AuthInterceptor {
        return AuthInterceptor {
            runBlocking {
                authPreferences.tokenFlow.firstOrNull()
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun providePartidosApi(client: OkHttpClient): PartidosApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PartidosApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(client: OkHttpClient): AuthApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    // --- REPOSITORIES ---
    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApiService,
        authPreferences: AuthPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, authPreferences)
    }

    @Provides
    @Singleton
    fun providePartidosRepository(
        api: PartidosApi,
        partidoDao: PartidoDao,
        compraDao: CompraDao
    ): PartidosRepository {
        return PartidosRepositoryImpl(api, partidoDao, compraDao)
    }
}

