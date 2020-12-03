package com.example.petagram.restApi.adapter;

import com.example.petagram.restApi.ConstantesRestApi;
import com.example.petagram.restApi.IEndpoinsApi;
import com.example.petagram.restApi.deserializador.MascotaDeserializador;
import com.example.petagram.restApi.deserializador.MascotaImgDataDeserializador;
import com.example.petagram.restApi.model.MascotaImgUrlResponse;
import com.example.petagram.restApi.model.MascotaResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiAdapter {

    public IEndpoinsApi establecerConexionRestApiInstagram(Gson gson){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.ROOT_URL_IG)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(IEndpoinsApi.class);
    }

    public Gson construyeGsonDeserealizadorListadoMedia(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(MascotaResponse.class, new MascotaDeserializador());
        return gsonBuilder.create();
    }

    public Gson construyeGsonDesearlizadorImagenMedia(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(MascotaImgUrlResponse.class,new MascotaImgDataDeserializador());
        return gsonBuilder.create();
    }

    public IEndpoinsApi establecerConexionRestMiApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.ROOT_URL_MI_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(IEndpoinsApi.class);
    }


}
