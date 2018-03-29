package com.dedalexey.sapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SapperActivity extends AppCompatActivity
        implements SapperFragment.CallBacks{

    public static final int ACTION_REPEAT = 0;
    public static final int ACTION_EXIT = 1;

    private int mWidth;
    private int mHeight;
    private int mBombs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        mWidth = 3;
        mHeight = 3;
        mBombs = 2;
        FragmentManager fragmentManager = getSupportFragmentManager();


        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


    }

    private Fragment createFragment() {
        return SapperFragment.newInstance(mWidth,mHeight,mBombs);
    }


    private void replaceFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, createFragment())
                    .commit();
    }

    @Override
    public void onEndDialogResult(Intent data) {
        int action = data.getIntExtra(EndDialogFragment.EXTRA_ACTION,-1);
        //Toast.makeText(this,action+ "",Toast.LENGTH_SHORT).show();
        switch (action){
            case ACTION_REPEAT: {
                replaceFragment();
                break;
            }
            case ACTION_EXIT: {
                finish();
                break;
            }
        }
    }

    @Override
    public void updateFragment(Intent data) {
        Bundle extras = data.getExtras();
        mWidth = extras.getInt(SapperFragment.ARG_WIDTH);
        mHeight = extras.getInt(SapperFragment.ARG_HEIGHT);
        mBombs = extras.getInt(SapperFragment.ARG_BOMBS);
        replaceFragment();
    }
}
