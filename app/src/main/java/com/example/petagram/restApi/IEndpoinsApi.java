package com.example.petagram.restApi;

import com.example.petagram.restApi.model.LikeUsuarioRespose;
import com.example.petagram.restApi.model.MascotaImgUrlResponse;
import com.example.petagram.restApi.model.MascotaResponse;
import com.example.petagram.restApi.model.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IEndpoinsApi {
    @GET(ConstantesRestApi.URL_GET_INFORMATION_USER)
    Call<MascotaResponse> getListaMedia(@Path(value = "user",encoded = true) String user,
                                        @Query(value = "access_token") String token,
                                        @Query(value = "fields") String fields);

    // @GET("{url_media}")
    @GET(ConstantesRestApi.URL_GET_INFORMATION_MEDIA_DATA)
    Call<MascotaImgUrlResponse>  getUrlMedia(@Path(value = "id_media",encoded = true) String id_media,
                                             @Query(value = "access_token") String token,
                                             @Query(value = "fields") String fields);
    // Call<MascotaResponse>  getUrlMedia(@Path(value = "url_media",encoded = false) String url_media);
    // Call<MascotaImgUrlResponse>  getUrlMedia(@Path(value = "id_media",encoded = true) String id_media);

    @FormUrlEncoded
    @POST(ConstantesRestApi.KEY_POST_ID_TOKEN)
    Call<UsuarioResponse> registrarTokenID(@Field("id_dispositivo") String id_dispositivo,
                                           @Field("id_usuario_instagram") String id_usuario_instagram);
    @FormUrlEncoded
    @POST(ConstantesRestApi.KEY_POST_LIKE_FOTO)
    Call<LikeUsuarioRespose> registrarLikeFoto(@Field("id_dispositivo") String id_dispositivo,
                                               @Field("id_usuario_instagram") String id_usuario_instagram,
                                               @Field("id_foto") String id_foto);

    @GET(ConstantesRestApi.KEY_POST_NOTIFICAR_LIKE_FOTO)
    Call<LikeUsuarioRespose>  notificarLikeFoto(@Path(value = "id",encoded = true) String id);

}
