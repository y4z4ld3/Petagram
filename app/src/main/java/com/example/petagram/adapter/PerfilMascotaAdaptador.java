package com.example.petagram.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petagram.pojo.Mascota;
import com.example.petagram.R;
import com.example.petagram.restApi.ConstantesRestApi;
import com.example.petagram.restApi.IEndpoinsApi;
import com.example.petagram.restApi.adapter.RestApiAdapter;
import com.example.petagram.restApi.model.LikeUsuarioRespose;
import com.example.petagram.restApi.model.UsuarioResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilMascotaAdaptador extends RecyclerView.Adapter<PerfilMascotaAdaptador.PerfilViewHolder>{

    ArrayList<Mascota> mascotas_coleccion;
    //ArrayList<MascotaPerfil> mascotas_coleccion;
    Activity activity;
    private static final String TAG = "PerfilMascotaAdaptador";
    /**/
    public PerfilMascotaAdaptador(ArrayList<Mascota> lista_coleccion, Activity activity){
        this.mascotas_coleccion = lista_coleccion;
        this.activity = activity;
    }


    @NonNull
    @Override
    public PerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_perfil_mascota,parent,false);
        return new PerfilMascotaAdaptador.PerfilViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PerfilViewHolder perfilViewHolder, int position) {
        final Mascota mascota_hold = mascotas_coleccion.get(position);
        //perfilViewHolder.imgFotoColeccionMascota.setImageResource(mascota_hold.getFoto());
        String UrlFoto = mascota_hold.getUrl_foto_media();
        if (UrlFoto == null){
            Log.i("Picasso: ", "Foto Vacia o Nula " + mascota_hold.toString());
        }
        else{
            Log.i("PicassoURL: ", UrlFoto);
        }

        Picasso.with(activity)
                .load(mascota_hold.getUrl_foto_media())
                .placeholder(R.drawable.dogface_100527)
                .into(perfilViewHolder.imgFotoColeccionMascota);
        perfilViewHolder.tvVotosColeccion.setText(String.valueOf(mascota_hold.getVotos()));
        perfilViewHolder.imgHuesoAmarilloColeccion.setImageResource(R.drawable.hueso_amarillo);
        //Log.i("UserName: ", mascota_hold.getNombre_usuario());
        //perfilViewHolder.tvNombrePerfilMascota.setText(mascota_hold.getNombre_usuario());

        perfilViewHolder.imgFotoColeccionMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnviarLike(mascota_hold.getId_apiRest());
            }
        });

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
                Toast.makeText(activity,"Diste Like",Toast.LENGTH_LONG).show();
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
                Toast.makeText(activity,"Noficaste Like",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LikeUsuarioRespose> call, Throwable t) {
                //Toast.makeText(this,"Error Notificación",Toast.LENGTH_SHORT).show();
                Log.e("LikeUsuarioRespose",t.getStackTrace().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mascotas_coleccion.size();
    }

    public static class PerfilViewHolder extends RecyclerView.ViewHolder{

        private AppCompatImageView imgFotoColeccionMascota;
        private AppCompatTextView tvVotosColeccion;
        private AppCompatImageView imgHuesoAmarilloColeccion;

        private TextView tvNombrePerfilMascota;

        public PerfilViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFotoColeccionMascota      = (AppCompatImageView)itemView.findViewById(R.id.imgFotoColeccionMascota);
            tvVotosColeccion             = (AppCompatTextView)itemView.findViewById(R.id.tvVotosColeccion);
            imgHuesoAmarilloColeccion    = (AppCompatImageView)itemView.findViewById(R.id.imgHuesoAmarilloColeccion);
            tvNombrePerfilMascota        = (TextView)itemView.findViewById(R.id.tvNombrePerfilMascota);

        }
    }
}
