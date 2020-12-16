package com.example.petagram;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.NotificationCompat.WearableExtender;

import com.example.petagram.restApi.ConstantesRestApi;
import com.example.petagram.restApi.IEndpoinsApi;
import com.example.petagram.restApi.adapter.RestApiAdapter;
import com.example.petagram.restApi.model.UsuarioResponse;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";
    private static final int notificationId = 001;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //  super.onMessageReceived(remoteMessage);
        try {
            createNotificationChannel();
            //Intent i  = new Intent(this,MascotasActivity.class);
            //Intent i_like_mascota = new Intent();
            Intent i_like_mascota = new Intent(this,MascotasActivity.class);
            i_like_mascota.setAction("LIKE_MASCOTA");
            Intent i_ver_top_5 = new Intent(this,MascotasTop5Activity.class);
            Intent i_ver_usuario = new Intent(this,AcercaDeActivity.class);


            //PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
            //PendingIntent pendingIntentLikeMascota = PendingIntent.getBroadcast(this,0,i_like_mascota,PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntentLikeMascota = PendingIntent.getActivity(this,0,i_like_mascota,PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntentVerTop5 = PendingIntent.getActivity(this,0,i_ver_top_5,PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntentVerUsuario = PendingIntent.getActivity(this,0,i_ver_usuario,PendingIntent.FLAG_UPDATE_CURRENT);

            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            /*
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(
                            R.drawable.ic_full_tap,
                            getString(R.string.texto_accion_like),
                            pendingIntent
                    ).build();
            */
            NotificationCompat.Action acc_dar_like = new NotificationCompat.Action.Builder(
                    R.drawable.ic_full_heart,
                    getString(R.string.texto_accion_like),
                    pendingIntentLikeMascota
            ).build();
            NotificationCompat.Action acc_ver_perfil = new NotificationCompat.Action.Builder(
                    R.drawable.ic_full_pawprint,
                    getString(R.string.texto_accion_ver_top_5),
                    pendingIntentVerTop5
            ).build();
            NotificationCompat.Action acc_ver_usuario = new NotificationCompat.Action.Builder(
                    R.drawable.ic_full_usuario,
                    getString(R.string.texto_accion_ver_usuario),
                    pendingIntentVerUsuario
            ).build();
            

            NotificationCompat.WearableExtender wearableExtender =
                    new NotificationCompat.WearableExtender()
                    .setHintHideIcon(true)
                    .setBackground(BitmapFactory.decodeResource(getResources(),R.drawable.fondo_600x600))
                    .setGravity(Gravity.CENTER_VERTICAL)
                    .setContentIcon(R.mipmap.ic_launcher)
                    .addAction(acc_dar_like)
                    .addAction(acc_ver_perfil)
                    .addAction(acc_ver_usuario)
                    ;

            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this,"CHANNEL_ID")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Notificación")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSound(sonido)
                    .setContentIntent(pendingIntentLikeMascota)
                    //.addAction(R.drawable.ic_full_tap,getString(R.string.texto_accion_like),pendingIntent)
                    //.extend(wearableExtender.addAction(action))
                    .extend(wearableExtender)
                    .setAutoCancel(true);

            // NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

            //notificationManager.notify(0,notificacion.build());
            notificationManagerCompat.notify(notificationId,notificacion.build());

        } catch (Exception e) {
            //e.printStackTrace();
            //Toast.makeText(this,"Error Notificación",Toast.LENGTH_SHORT).show();
            Log.e(TAG,e.getStackTrace().toString());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        // super.onNewToken(s);
        Log.i("onNewToken",token);
        enviarTokenRegistro(token);
        ConstantesRestApi.gId_Dispositivo = token;
    }

    private void enviarTokenRegistro(String id_dispositivo) {
        Log.d(TAG,id_dispositivo);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        IEndpoinsApi iEndpoinsApi = restApiAdapter.establecerConexionRestMiApi();
        Call<UsuarioResponse> usuarioResponseCall = iEndpoinsApi.registrarTokenID(id_dispositivo, ConstantesRestApi.gUserId);
        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();
                Log.d("ID_FIREBASE",usuarioResponse.getId());
                Log.d("Id_dispositivo",usuarioResponse.getId_dispositivo());
                Log.d("Id_usuario_instagram",usuarioResponse.getId_usuario_instagram());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                //Toast.makeText(this,"Error Notificación",Toast.LENGTH_SHORT).show();
                Log.e("FIREBASE",t.getStackTrace().toString());
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
