package com.dedalexey.sapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by Alexey on 22.10.2017.
 */

public class EndDialogFragment extends DialogFragment {
    public static final String AGR_IS_WIN= "isWin";
    public static final String EXTRA_ACTION = "com.dedalexey.sapper.action";

    boolean isWin;
    TextView mStatusTextView;
    Button mRepeatButton;
    Button mExitButton;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        isWin = getArguments().getBoolean(AGR_IS_WIN);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.end_dialog,null);
        mStatusTextView = view.findViewById(R.id.status_text_view);

        if(isWin){
            mStatusTextView.setText(getString(R.string.win));
        }else {
            mStatusTextView.setText(getString(R.string.lose));
        }

        mRepeatButton = view.findViewById(R.id.repeat_button);
        mRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult(Activity.RESULT_OK,SapperActivity.ACTION_REPEAT);
                dismiss();
            }
        });
        mExitButton = view.findViewById(R.id.exit_button);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult(Activity.RESULT_OK,SapperActivity.ACTION_EXIT);
                dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setCancelable(false)
                .create();
    }

    private void sendResult(int resultCode, int action){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ACTION,action);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
    public static EndDialogFragment newInstance(boolean isWin) {
        Bundle args = new Bundle();
        args.putBoolean(AGR_IS_WIN,isWin);
        EndDialogFragment fragment = new EndDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
