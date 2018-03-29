package com.dedalexey.sapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by Alexey on 22.10.2017.
 */

public class CustomDialogFragment extends DialogFragment {

    private static final String TAG = "CustomDialogFragment";

    int mWidth;
    int mHeight;
    int mBombs;

    SeekBar mWidthSeekBar;
    TextView mWidthValueTextView;
    int mMinWidth=3;
    int mMaxWidth=16-mMinWidth;


    SeekBar mHeightSeekBar;
    TextView mHeightValueTextView;
    int mMinHeight=3;
    int mMaxHeight=50-mMinHeight;

    SeekBar mBombsSeekBar;
    TextView mBombsValueTextView;
    int mMinBombs=1;
    int mMaxBombs;



    private void update() {
        mWidth = mWidthSeekBar.getProgress() + mMinWidth;
        mWidthValueTextView.setText(String.valueOf(mWidth));

        mHeight = mHeightSeekBar.getProgress() + mMinHeight;
        mHeightValueTextView.setText(String.valueOf(mHeight));

        mMaxBombs = mHeight*mWidth - 1;

        mBombs = mBombsSeekBar.getProgress() + mMinBombs;
        mBombsSeekBar.setMax(mMaxBombs-mMinBombs);
        if(mBombs>mMaxBombs){
            mBombsSeekBar.setProgress(0);
            mBombs = mMinBombs;
        }
        mBombsValueTextView.setText(String.valueOf(mBombs));

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        mWidth = args.getInt(SapperFragment.ARG_WIDTH,0);
        mHeight = args.getInt(SapperFragment.ARG_HEIGHT,0);
        mBombs = args.getInt(SapperFragment.ARG_BOMBS,0);
        Log.i(TAG, mWidth + " " + mHeight + " " +  mBombs);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog,null);
        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        mWidthSeekBar = view.findViewById(R.id.width_seekBar);
        mWidthValueTextView = view.findViewById(R.id.width_value);


        mHeightSeekBar = view.findViewById(R.id.height_seekBar);
        mHeightValueTextView = view.findViewById(R.id.height_value);

        mBombsSeekBar = view.findViewById(R.id.bombs_seekBar);
        mBombsValueTextView = view.findViewById(R.id.bombs_value);


        mWidthSeekBar.setMax(mMaxWidth);
        mWidthSeekBar.setProgress(mWidth-mMinWidth);


        mHeightSeekBar.setMax(mMaxHeight);
        mHeightSeekBar.setProgress(mHeight-mMinHeight);


        mHeightSeekBar.setMax(mWidth*mHeight - mMinBombs);
        mBombsSeekBar.setProgress(mBombs-mMinBombs);

        mWidthSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        mHeightSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        mBombsSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        update();
//        Log.i("UPDATE",
//                "mSeekBar.getProgress()= "+ mSeekBar.getProgress()+
//                        "\nmSeekBar1.getProgress()= "+
 //                       mSeekBar1.getProgress());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_CANCELED);
                       // Toast.makeText(getActivity(),R.string.cancel,Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    private void sendResult(int resultCode){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(SapperFragment.ARG_WIDTH,mWidth);
        intent.putExtra(SapperFragment.ARG_HEIGHT,mHeight);
        intent.putExtra(SapperFragment.ARG_BOMBS,mBombs);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    public static CustomDialogFragment newInstance(int currentWidth, int currentHeaght, int currentBombs) {
        
        Bundle args = new Bundle();
        args.putInt(SapperFragment.ARG_WIDTH,currentWidth);
        args.putInt(SapperFragment.ARG_HEIGHT,currentHeaght);
        args.putInt(SapperFragment.ARG_BOMBS,currentBombs);
        CustomDialogFragment fragment = new CustomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
