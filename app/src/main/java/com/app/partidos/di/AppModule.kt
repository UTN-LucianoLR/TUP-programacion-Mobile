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
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID
import javax.inject.Singleton

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
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(MockApiInterceptor(context))
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

    // --- REPOSITORIES ---
    @Provides
    @Singleton
    fun provideAuthRepository(usuarioDao: UsuarioDao): AuthRepository {
        return AuthRepositoryImpl(usuarioDao)
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

class MockApiInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (path.contains("matches")) {
            val json = context.assets.open("json/matches.json").bufferedReader().use { it.readText() }
            return Response.Builder()
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        if (path.contains("teams")) {
            val json = context.assets.open("json/teams.json").bufferedReader().use { it.readText() }
            return Response.Builder()
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        if (path.contains("stadiums")) {
            val json = context.assets.open("json/stadiums.json").bufferedReader().use { it.readText() }
            return Response.Builder()
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        if (path.contains("tournaments")) {
            val json = context.assets.open("json/tournament.json").bufferedReader().use { it.readText() }
            return Response.Builder()
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        if (path.contains("payments")) {
            val successJson = """
                {
                    "success": true,
                    "transaction_id": "${UUID.randomUUID()}",
                    "message": "Pago procesado exitosamente (Mock)",
                    "total_amount": 100.0,
                    "currency": "USD",
                    "purchase_date": "2026-06-05T00:00:00Z"
                }
            """.trimIndent()
            return Response.Builder()
                .code(200)
                .message("OK")
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body(successJson.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
        }

        return chain.proceed(request)
    }
}
