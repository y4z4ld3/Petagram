package com.example.petagram.presentador;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petagram.R;
import com.example.petagram.db.InteractorPerfilMascota;
import com.example.petagram.pojo.Mascota;
import com.example.petagram.restApi.ConstantesRestApi;
import com.example.petagram.restApi.IEndpoinsApi;
import com.example.petagram.restApi.adapter.RestApiAdapter;
import com.example.petagram.restApi.model.MascotaImgUrlResponse;
import com.example.petagram.restApi.model.MascotaResponse;
import com.example.petagram.vista_fragment.IPerfilMascotaFragmentView;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilMascotaFragmentPresenter implements IPerfilMascotaFragmentPresenter{

    private IPerfilMascotaFragmentView iPerfilMascotaFragmentView;
    private Context context;
    private InteractorPerfilMascota interactorPerfilMascota;
    private ArrayList<Mascota> coleccion;


    public PerfilMascotaFragmentPresenter(IPerfilMascotaFragmentView iPerfilMascotaFragmentView, Context context) {
        this.iPerfilMascotaFragmentView = iPerfilMascotaFragmentView;
        this.context = context;
        //obtenerPerfilMascota();
        obtenerDatosPerfil();
    }

    @Override
    public void obtenerPerfilMascota() {
        interactorPerfilMascota = new InteractorPerfilMascota(context);
        coleccion = interactorPerfilMascota.obtenerDatos();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Log.e("sleep",e.toString());
        }
        mostrarPerfilMascota();
    }

    @Override
    public void obtenerDatosPerfil() {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Gson gsonMediaData = restApiAdapter.construyeGsonDeserealizadorListadoMedia();
        IEndpoinsApi iEndpoinsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonMediaData);
        String user_id = ConstantesRestApi.gUserId;
        String token = ConstantesRestApi.gToken;
        String fields = ConstantesRestApi.FIEDLS;
        Call<MascotaResponse> mascotaResponseCall = iEndpoinsApi.getListaMedia(user_id,token,fields);
        //Log.i("mascotaResponseCall", mascotaResponseCall.request().toString());
        mascotaResponseCall.enqueue(new Callback<MascotaResponse>() {
            @Override
            public void onResponse(Call<MascotaResponse> call, Response<MascotaResponse> response) {
                //Log.i("URL", ConstantesRestApi.URL_GET_INFORMATION_USER);

                if (response.body()!= null){
                    if (!response.isSuccessful()){
                        Log.i("Respose: ", String.valueOf(response.code()));
                    }

                    MascotaResponse mascotaResponse = response.body();
                    //Log.i("MascotaResponse: ", mascotaResponse.toString());
                    coleccion = mascotaResponse.getMascotaPerfil();


                    if (coleccion != null){
                         obtenerImagenDeURL(coleccion);
                         mostrarPerfilMascota();
                    }
                    else{
                        Log.i("coleccion", "Array coleccion esta NULO");
                    }
                }
                else{
                    Log.i("response.body", "response.body esta NULO");
                }


            }

            @Override
            public void onFailure(Call<MascotaResponse> call, Throwable t) {
                Toast.makeText(context,"Algo Ocurrió Datos Perfil",Toast.LENGTH_LONG).show();
                Log.e("Error en el RestApi", t.toString());
            }
        });

    }

    @Override
    public void mostrarPerfilMascota() {
        iPerfilMascotaFragmentView.inicializarPerfilMascotaAdaptadorRV(iPerfilMascotaFragmentView.crearPerfilMascotaAdaptador(coleccion));
        iPerfilMascotaFragmentView.generarGridLayout();
    }

    private void obtenerImagenDeURL(final ArrayList<Mascota> coleccion){
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Gson gsonMediaData = restApiAdapter.construyeGsonDesearlizadorImagenMedia();
        IEndpoinsApi iEndpoinsApi = restApiAdapter.establecerConexionRestApiInstagram(gsonMediaData);
        for (int i=0;i<coleccion.size();i++){
            String id_media = coleccion.get(i).getId_apiRest(); // Aca Tomo del Array el id
            String token = ConstantesRestApi.gToken;
            String fields = ConstantesRestApi.FIEDLS_MEDIA;
            Call<MascotaImgUrlResponse>  urlMediaResponse = iEndpoinsApi.getUrlMedia(id_media,token,fields);
            //Log.i("urlMediaResponse", urlMediaResponse.request().toString());
            if (urlMediaResponse==null){
                Log.i("urlMediaResponseNull", urlMediaResponse.request().toString());
            }
            else{
                final int finalI = i;
                urlMediaResponse.enqueue(new Callback<MascotaImgUrlResponse>() {
                    @Override
                    public void onResponse(Call<MascotaImgUrlResponse> call, Response<MascotaImgUrlResponse> response) {
                        //  Log.i("ImagenURL", ConstantesRestApi.URL_GET_INFORMATION_MEDIA_DATA);
                        if (!response.isSuccessful()){
                            Log.i("Respose_img: ", String.valueOf(response.code()));
                        }
                        if (response.body() != null){

                            MascotaImgUrlResponse mascotaImgUrlResponse = response.body();
                            // Log.i("MascotaResponseImg: ", response.body().toString());
                            String url_media = mascotaImgUrlResponse.getMedia_url();
                            //Log.i("url_media de Response:", url_media);
                            coleccion.get(finalI).setUrl_foto_media(url_media);   // Aca a la Array actualizo su URL
                        }
                        else{
                            Log.i("response.body", "response.body de Imagen esta NULO");
                        }
                    }

                    @Override
                    public void onFailure(Call<MascotaImgUrlResponse> call, Throwable t) {
                        Toast.makeText(context,"Algo Ocurrió Imagen Data",Toast.LENGTH_LONG).show();
                        Log.e("Error en el ImgRestApi", t.toString());
                    }
                });
            }
        }
    }

}
