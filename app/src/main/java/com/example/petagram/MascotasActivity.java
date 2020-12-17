package com.example.petagram;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.petagram.adapter.PageAdapter;
import com.example.petagram.restApi.ConstantesRestApi;
import com.example.petagram.vista_fragment.MascotasFragment;
import com.example.petagram.vista_fragment.PerfilMascotaFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;


public class MascotasActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final String TAG = "MascotasActivity";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macotas);


        androidx.appcompat.widget.Toolbar miActionBar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.miActionBar);
        if (miActionBar != null){

            setSupportActionBar(miActionBar);
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_huella);
        //getSupportActionBar().setTitle(app_name);

        agregarFAB();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        setUpViewPager();
        enviarNotificacion();
      }

    private ArrayList<Fragment> agregarFragments(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MascotasFragment());
        fragments.add(new PerfilMascotaFragment());
        return fragments;
    }

    private void setUpViewPager(){
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_house);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_galeria);
        tabLayout.setTabIndicatorFullWidth(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(1);
    }

    public void agregarFAB() {
        FloatingActionButton miFAB = (FloatingActionButton)findViewById(R.id.fabMiFAB);
        miFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getBaseContext(),getResources().getString(R.string.mensaje),Toast.LENGTH_SHORT).show();
                Snackbar.make(view,getResources().getString(R.string.texto_accion),Snackbar.LENGTH_SHORT)
                        .setAction(getResources().getString(R.string.texto_accion), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.i("SANAKBAR","Click en SnackBar");
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opciones, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.imgEstrella:
                irAMascotaTop5();
                return true;
            case R.id.mContacto:
                intent = new Intent(this,ContactoActivity.class);
                startActivity(intent);
                return true;
            case R.id.mAcercade:
                intent = new Intent(this,AcercaDeActivity.class);
                startActivity(intent);
                return true;
            case R.id.mConfigurarCuenta:
                intent = new Intent(this,ConfigurarCuentaActivity.class);
                startActivity(intent);
                return true;
            case R.id.mRecibirNotificaciones:
                enviarNotificacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void enviarNotificacion() {
        // Get token
        // [START log_reg_token]
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        ConstantesRestApi.gId_Dispositivo = token;
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        //Toast.makeText(MascotasActivity.this, msg, Toast.LENGTH_SHORT).show();
                        NotificationService notificationService = new NotificationService();
                        notificationService.onNewToken(token);
                    }
                });
        // [END log_reg_token]
    }

    private void lanzarNotificacion() {
        try {
            createNotificationChannel();
            Intent i  = new Intent(this,MascotasActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);

            Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder notificacion = new NotificationCompat.Builder(this,"CHANNEL_ID")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Notificación")
                    .setContentText("Hola Mundo")
                    .setSound(sonido)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0,notificacion.build());

        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(this,"Error Notificación",Toast.LENGTH_SHORT).show();
            Log.e("Notificación",e.getStackTrace().toString());
        }
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

    public void irAMascotaTop5(){
        Bundle extras = new Bundle();
        Intent intent = new Intent(this,MascotasTop5Activity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Explode explode = new Explode();
            explode.setDuration(1234);
            getWindow().setExitTransition(explode);
            startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this,viewPager,"").toBundle());
        }
        else {
            startActivity(intent);
        }

    }
}