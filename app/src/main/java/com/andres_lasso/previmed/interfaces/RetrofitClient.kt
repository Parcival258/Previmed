package com.andres_lasso.previmed.interfaces


import android.util.Log
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
    private const val BASE_URL = "http://72.61.8.11:3333/"

    init {
        Log.d("RetrofitClient", "Inicializando Retrofit con URL: $BASE_URL")
    }

    // 🧾 Interceptor de logs detallado
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
            .addInterceptor(loggingInterceptor)
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
    val retrofit: Retrofit by lazy {
        Log.d("RetrofitClient", "Creando instancia Retrofit...")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // -------- APIS --------
    val loginApi: LoginApi by lazy { retrofit.create(LoginApi::class.java) }
    val planesApi: PlanesApi by lazy { retrofit.create(PlanesApi::class.java) }
    val pacienteApi: PacienteApi by lazy { retrofit.create(PacienteApi::class.java) }
    val epsApi: EpsApi by lazy { retrofit.create(EpsApi::class.java) }
    val membresiaApi: MembresiaApi by lazy { retrofit.create(MembresiaApi::class.java) }
    val visitasApi: VisitaService by lazy { retrofit.create(VisitaService::class.java) }
    val pagoApi: PagoApi by lazy { retrofit.create(PagoApi::class.java) }
    val formaPagoApi: FormaPagoApi by lazy { retrofit.create(FormaPagoApi::class.java) }
    val registerApi: RegisterApi by lazy { retrofit.create(RegisterApi::class.java) }
    val medicoApi: MedicoApi by lazy { retrofit.create(MedicoApi::class.java) }
    val pagosApi: PagosApiService by lazy { retrofit.create(PagosApiService::class.java) }
    val beneficiarioApi: BeneficiarioService by lazy { retrofit.create(BeneficiarioService::class.java) }
    val usuarioApi: UsuarioApi by lazy { retrofit.create(UsuarioApi::class.java) }
}
