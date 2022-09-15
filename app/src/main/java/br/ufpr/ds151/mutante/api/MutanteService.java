package br.ufpr.ds151.mutante.api;

import java.util.List;

import br.ufpr.ds151.mutante.model.LoginDTO;
import br.ufpr.ds151.mutante.model.MutanteDTO;
import br.ufpr.ds151.mutante.model.UsuarioDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;

public interface MutanteService {

    @POST("login")
    Call<UsuarioDTO> login(@Body LoginDTO loginDTO);

    @GET("mutantes")
    Call<List<MutanteDTO>> getAllMutantes();

    @GET("mutantes/{id}")
    Call<MutanteDTO> getMutanteById(@Path("id") Long id);

    @GET("mutantes/count")
    Call<Integer> getCount();

    @GET("habilidades/top")
    Call<List<String>> getTopHabilities();

    @GET("mutantes/{hab}/habilidades")
    Call<List<MutanteDTO>> getMutantesByHabilidade(@Path("hab") String hab);

    @POST("mutantes")
    Call<MutanteDTO> postMutante(@Body MutanteDTO mutanteDTO);

    @PUT("mutantes/{id}")
    Call<MutanteDTO> putMutante(@Path("id") Long id, @Body MutanteDTO mutanteDTO);

    @DELETE("mutantes/{id}")
    Call<MutanteDTO> deleteMutante(@Path("id") Long id);

    @GET("usuarios/{id}")
    Call<UsuarioDTO> getUsuario(@Path("id") Long id);

}
