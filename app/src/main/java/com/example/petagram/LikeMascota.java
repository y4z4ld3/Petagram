package com.example.petagram;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.petagram.restApi.ConstantesRestApi;
import com.example.petagram.restApi.IEndpoinsApi;
import com.example.petagram.restApi.adapter.RestApiAdapter;
import com.example.petagram.restApi.model.LikeUsuarioRespose;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeMascota extends BroadcastReceiver {
    private static final String TAG = "LikeMascotaBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        String ACCION_LIKE_MASCOTA = "LIKE_MASCOTA";
        String accion = intent.getAction();

        if (ACCION_LIKE_MASCOTA.equals(accion)){
            EnviarLike(ConstantesRestApi.gId_Foto);
            Toast.makeText(context,"DISTE Like",Toast.LENGTH_LONG).show();
        }
     }

    private void EnviarLike(String id_apiRest) {
        Log.d(TAG,id_apiRest);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        IEndpoinsApi iEndpoinsApi = restApiAdapter.establecerConexionRestMiApi();
        Call<LikeUsuarioRespose> usuarioResponseCall = iEndpoinsApi.registrarLikeFoto(ConstantesRestApi.gId_Dispositivo, ConstantesRestApi.gUserId,id_apiRest);
        usuarioResponseCall.enqueue(new Callback<LikeUsuarioRespose>() {
            @Override
            public void onResponse(Call<LikeUsuarioRespose> call, Response<LikeUsuarioRespose> response) {
                LikeUsuarioRespose likeUsuarioRespose = response.body();
                Log.d("ID_FIREBASE",likeUsuarioRespose.getId());
                Log.d("Id_dispositivo",likeUsuarioRespose.getId_dispositivo());
                Log.d("Id_usuario_instagram",likeUsuarioRespose.getId_usuario_instagram());
                //Toast.makeText(activity,"Diste Like",Toast.LENGTH_LONG).show();
                NotificarLike(likeUsuarioRespose.getId());
            }

            @Override
            public void onFailure(Call<LikeUsuarioRespose> call, Throwable t) {
                //Toast.makeText(this,"Error Notificación",Toast.LENGTH_SHORT).show();
                Log.e("EnviarLike",t.getStackTrace().toString());
            }
        });
    }

    private void NotificarLike(String id) {
        Log.d(TAG,id);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        IEndpoinsApi iEndpoinsApi = restApiAdapter.establecerConexionRestMiApi();
        Call<LikeUsuarioRespose> usuarioResponseCall = iEndpoinsApi.notificarLikeFoto(id);
        usuarioResponseCall.enqueue(new Callback<LikeUsuarioRespose>() {
            @Override
            public void onResponse(Call<LikeUsuarioRespose> call, Response<LikeUsuarioRespose> response) {
                LikeUsuarioRespose likeUsuarioRespose = response.body();
                Log.d("ID_FIREBASE",likeUsuarioRespose.getId());
                Log.d("Id_dispositivo",likeUsuarioRespose.getId_dispositivo());
                Log.d("Id_usuario_instagram",likeUsuarioRespose.getId_usuario_instagram());
                //Toast.makeText(activity,"Noficaste Like",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LikeUsuarioRespose> call, Throwable t) {
                //Toast.makeText(this,"Error Notificación",Toast.LENGTH_SHORT).show();
                Log.e("LikeUsuarioRespose",t.getStackTrace().toString());
            }
        });
    }
}
