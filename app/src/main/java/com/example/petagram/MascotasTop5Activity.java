package com.example.petagram;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;

import com.example.petagram.adapter.PageAdapter;

import com.example.petagram.vista_fragment.MascotasTop5Fragment;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class MascotasTop5Activity extends  AppCompatActivity  {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas_top5);

        androidx.appcompat.widget.Toolbar miActionBar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.miActionBar);
        if (miActionBar != null){
            setSupportActionBar(miActionBar);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_huella);

        // tabLayout = (TabLayout) findViewById(R.id.tabLayoutTop5);
        viewPager = (ViewPager) findViewById(R.id.viewPagerTop5);
        setUpViewPager();


    }

    private ArrayList<Fragment> agregarFragmentsUnico(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MascotasTop5Fragment());
        return fragments;
    }

    private void setUpViewPager(){
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),agregarFragmentsUnico() ));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.setDuration(1234);
            getWindow().setEnterTransition(slide);
            getWindow().setReturnTransition(new Fade(Fade.OUT));
        }
        /*  tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_galeria);
        tabLayout.setTabIndicatorFullWidth(true);

         */
    }
}