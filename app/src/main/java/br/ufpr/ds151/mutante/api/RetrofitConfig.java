package br.ufpr.ds151.mutante.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.11:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public MutanteService getMutanteService() {
        return this.retrofit.create(MutanteService.class);
    }
}
