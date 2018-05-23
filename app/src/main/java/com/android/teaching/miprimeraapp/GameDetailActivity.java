package com.android.teaching.miprimeraapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.teaching.miprimeraapp.fragments.GameDetailFragment;
import com.android.teaching.miprimeraapp.model.GameModel;
import com.android.teaching.miprimeraapp.presenters.GameDetailPresenter;
import com.android.teaching.miprimeraapp.view.GameDetailView;

public class GameDetailActivity extends AppCompatActivity
    implements GameDetailView {

    private GameDetailPresenter presenter;
    private int currentPosition;
    private MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //PARA AÑADIR LA FLECHA DE BACK A LA TOOLBAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new GameDetailPresenter();

        currentPosition = getIntent().getIntExtra("position", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting(this);

        ViewPager myViewPager = findViewById(R.id.view_pager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myPagerAdapter);
        getSupportActionBar().setTitle(myPagerAdapter.getPageTitle(currentPosition));
        //PARA MOVER A LA PAGINA X
        myViewPager.setCurrentItem(currentPosition);
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(myPagerAdapter.getPageTitle(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onGameLoaded(GameModel game) {

    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position){
            int gameId = presenter.getGames().get(position).getId();
            return GameDetailFragment.newInstance(gameId);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return presenter.getGames().get(position).getName();
        }

        @Override
        public int getCount() {
            return presenter.getGames().size();
        }
    }
}























