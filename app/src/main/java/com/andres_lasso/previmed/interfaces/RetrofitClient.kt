package com.andres_lasso.previmed.interfaces

import android.util.Log
import com.andres_lasso.previmed.network.PagosApi
import com.andres_lasso.previmed.network.PagosApiService
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // 🌐 URL base del backend Previmed
    private const val BASE_URL = "https://previmedbackend-q73n.onrender.com/"

    init {
        Log.d("RetrofitClient", "Inicializando Retrofit con URL: $BASE_URL")
    }

    // 🧾 Interceptor de logs detallado (solo en modo debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        Log.d("RetrofitClient", "HttpLoggingInterceptor configurado en modo BODY")
    }

    // ⚙️ Cliente HTTP con timeouts y logs
    private val okHttpClient: OkHttpClient by lazy {
        Log.d("RetrofitClient", "Creando OkHttpClient...")
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("Retrofit-REQ", "➡️ ${request.method} ${request.url}")
                val response = chain.proceed(request)
                Log.d("Retrofit-RESP", "⬅️ Código: ${response.code}")
                response
            }
            .addInterceptor(loggingInterceptor) // logs del body
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    // 🧠 Configuración de Gson
    private val gson = GsonBuilder()
        .setLenient()
        .serializeNulls()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    // 🚀 Instancia principal de Retrofit
    private val retrofit: Retrofit by lazy {
        Log.d("RetrofitClient", "Creando instancia Retrofit...")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ----------------------------------------------------
    // APIS CENTRALIZADAS — cada una con log al inicializar
    // ----------------------------------------------------
    val loginApi: LoginApi by lazy {
        Log.d("RetrofitClient", "Creando LoginApi")
        retrofit.create(LoginApi::class.java)
    }

    val planesApi: PlanesApi by lazy {
        Log.d("RetrofitClient", "Creando PlanesApi")
        retrofit.create(PlanesApi::class.java)
    }

    val pacienteApi: PacienteApi by lazy {
        Log.d("RetrofitClient", "Creando PacienteApi")
        retrofit.create(PacienteApi::class.java)
    }

    val epsApi: EpsApi by lazy {
        Log.d("RetrofitClient", "Creando EpsApi")
        retrofit.create(EpsApi::class.java)
    }

    val membresiaApi: MembresiaApi by lazy {
        Log.d("RetrofitClient", "Creando MembresiaApi")
        retrofit.create(MembresiaApi::class.java)
    }

    val visitasApi: VisitaService by lazy {
        Log.d("RetrofitClient", "Creando VisitaService")
        retrofit.create(VisitaService::class.java)
    }

    val pagoApi: PagoApi by lazy {
        Log.d("RetrofitClient", "Creando PagoApi")
        retrofit.create(PagoApi::class.java)
    }

    val formaPagoApi: FormaPagoApi by lazy {
        Log.d("RetrofitClient", "Creando FormaPagoApi")
        retrofit.create(FormaPagoApi::class.java)
    }

    val registerApi: RegisterApi by lazy {
        Log.d("RetrofitClient", "Creando RegisterApi")
        retrofit.create(RegisterApi::class.java)
    }

    val medicoApi: MedicoApi by lazy {
        Log.d("RetrofitClient", "Creando MedicoApi")
        retrofit.create(MedicoApi::class.java)
    }

    val pagosApi: PagosApiService by lazy {
        Log.d("RetrofitClient", "Creando PagosApiService")
        retrofit.create(PagosApiService::class.java)
    }

    val beneficiarioApi: BeneficiarioService by lazy {
        Log.d("RetrofitClient", "Creando BeneficiarioService")
        retrofit.create(BeneficiarioService::class.java)
    }
    val usuarioApi: UsuarioApi by lazy {
        Log.d("RetrofitClient", "Creando UsuarioApi")
        retrofit.create(UsuarioApi::class.java)
    }

}
