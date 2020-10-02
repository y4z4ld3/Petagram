package com.example.petagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout srlMiIndicadorRefresh;
    ListView lvMiLista;
    Adapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        agregarFAB();

        srlMiIndicadorRefresh = (SwipeRefreshLayout) findViewById(R.id.srlMiIndicadorRefresh);
        lvMiLista  = (ListView) findViewById(R.id.lvMiLista);
        String[] familia = getResources().getStringArray(R.array.strArrFamilia);
        lvMiLista.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,familia));
        srlMiIndicadorRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refrescarLista();
            }
        });
    }

    public void refrescarLista() {
        String[] familia = getResources().getStringArray(R.array.strArrFamilia);
        lvMiLista.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,familia));
        srlMiIndicadorRefresh.setRefreshing(false);
    }

    public void agregarFAB() {
        FloatingActionButton miFAB = (FloatingActionButton) findViewById(R.id.fabMiFAB);
        miFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getBaseContext(),getResources().getString(R.string.mensaje),Toast.LENGTH_SHORT).show();
                Snackbar.make(view,getResources().getString(R.string.mensaje),Snackbar.LENGTH_SHORT)
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
}