package com.example.petagram;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.petagram.restApi.ConstantesRestApi;
import com.example.petagram.restApi.IEndpoinsApi;
import com.example.petagram.restApi.adapter.RestApiAdapter;
import com.example.petagram.restApi.model.UsuarioResponse;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //  super.onMessageReceived(remoteMessage);
        try {
            Intent i  = new Intent(this,MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);

            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this,"CHANNEL_ID")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Notificación")
                    .setContentText(remoteMessage.getFrom())
                    .setSound(sonido)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0,notificacion.build());

        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(this,"Error Notificación",Toast.LENGTH_SHORT).show();
            Log.e(TAG,e.getStackTrace().toString());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        // super.onNewToken(s);
        Log.i("onNewToken",token);
        enviarTokenRegistro(token);
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
        // 9B:99:13:BE:B2:DC:F9:70:D2:A1:5D:A0:14:47:A3:17:F5:14:F4:AE
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
