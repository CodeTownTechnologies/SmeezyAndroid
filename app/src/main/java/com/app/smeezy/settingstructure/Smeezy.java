package com.app.smeezy.settingstructure;

import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import com.app.smeezy.BuildConfig;
import com.app.smeezy.utills.PreferenceUtils;
import com.app.smeezy.utills.StaticData;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@ReportsCrashes(formKey = "", // will not be used
        mailTo = "itsgauravjain.gj@gmail.com",
        mode = ReportingInteractionMode.SILENT
)

/**
 * Created by kipl146 on 8/29/2016.
 */

public class Smeezy extends MultiDexApplication {

    Context mContext;
    ApiService mApiService;
    private static Smeezy mInstance;
    private String LOGTAG = Smeezy.class.getName();


    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
        ACRA.init(this);

        mInstance = this;
        mContext = this;

        Thread t = new Thread(new Runnable() {
            public void run() {

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        String GCMRegId = PreferenceUtils.getDeviceGcmId(getAppContext());
                        while (TextUtils.isEmpty(GCMRegId)) {
                            try {
                                GCMRegId = FirebaseInstanceId.getInstance().getToken();
                            } catch (Exception e) {
                                if (BuildConfig.DEBUG)
                                    Log.e(LOGTAG, e.toString());
                            }
                            PreferenceUtils.setDeviceGcmId(getAppContext(), GCMRegId);
                            if (BuildConfig.DEBUG)
                                Log.d(LOGTAG, "GCMRegId --> " + GCMRegId);
                        }
                    }
                });
                t.start();
            }
        });
        t.start();


        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        httpBuilder.connectTimeout(60, TimeUnit.SECONDS);
        httpBuilder.readTimeout(10, TimeUnit.MINUTES);
        httpBuilder.writeTimeout(10, TimeUnit.MINUTES);
        httpBuilder.retryOnConnectionFailure(true);
        httpBuilder.addInterceptor(new CustomInterceptor(""));


        //OkHttpClient okHttpClient = enableTls12OnPreLollipop(httpBuilder).build();

        OkHttpClient okHttpClient = httpBuilder.build();

        //init retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(StaticData.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mApiService = retrofit.create(ApiService.class);

    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {

                SSLContext sc = SSLContext.getInstance("TLS");

                TrustManagerManipulator trustManager = new TrustManagerManipulator();

                sc.init(null, new TrustManager[]{trustManager}, null);
                client.sslSocketFactory(new SSLSocketFactoryCompat(trustManager), trustManager);

               /* ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);*/
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    public Context getAppContext() {
        return mContext;
    }

    public ApiService getApiService() {
        return mApiService;
    }

    public static Smeezy getInstance() {
        return mInstance;
    }

}
