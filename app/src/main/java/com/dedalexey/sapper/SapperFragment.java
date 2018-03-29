package com.dedalexey.sapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SapperFragment extends Fragment {

    private static final String TAG= "SapperFragment";

    public static final String ARG_WIDTH = "arg_width";
    public static final String ARG_HEIGHT = "arg_height";
    public static final String ARG_BOMBS = "arg_bombs";

    public static final int  REQUEST_ACTION = 0;
    private static final int REQUEST_CUSTOM = 1;

    public static final String DIALOG_END = "dialogEnd";
    private Field mField;

    RecyclerView mRecyclerView;
    FieldAdapter mAdapter;
    GridLayoutManager mLayoutManager;

    private Vibrator vibrator;
    private long vibrTime = 500L;

    CallBacks listener;

    interface CallBacks{
        void onEndDialogResult(Intent data);
        void updateFragment(Intent data);
    }

    public SapperFragment() {
    }

    public static SapperFragment newInstance(int width, int height, int bombs) {

        Bundle args = new Bundle();
        args.putInt(ARG_WIDTH,width);
        args.putInt(ARG_HEIGHT,height);
        args.putInt(ARG_BOMBS,bombs);
        SapperFragment fragment = new SapperFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater  inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_main, menu);
       // updateSubtitle();
    }

    private void updateSubtitle() {
        String subtitle = getString(R.string.subtitle,mField.getWidth(),
                mField.getHeight(), mField.getNumBombs());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_mode_easy) {
            Intent data = getData(9,9,10);
            listener.updateFragment(data);
            return true;
        }
        if (id == R.id.action_mode_normal) {
            Intent data = getData(16,16,40);
            listener.updateFragment(data);
            return true;
        }

        if (id == R.id.action_mode_hard) {
            Intent data = getData(16,30,100);
            listener.updateFragment(data);
            return true;
        }
        if( id ==R.id.action_mode_custom){
            FragmentManager manager = getFragmentManager();
            CustomDialogFragment dialog = CustomDialogFragment.
                    newInstance(mField.getWidth(),mField.getHeight(),mField.getNumBombs());
            Log.i(TAG,mField.getWidth() + " " + mField.getHeight() + " " + mField.getNumBombs());
            dialog.setTargetFragment(SapperFragment.this,REQUEST_CUSTOM);
            dialog.setCancelable(false);
            dialog.show(manager,"Dialog custom");
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent getData(int width, int height, int bombs) {
        Intent data = new Intent();
        data.putExtra(ARG_WIDTH,width);
        data.putExtra(ARG_HEIGHT,height);
        data.putExtra(ARG_BOMBS,bombs);
        return data;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (CallBacks) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        int width = args.getInt(ARG_WIDTH,0);
        int height = args.getInt(ARG_HEIGHT,0);
        int numBombs = args.getInt(ARG_BOMBS,0);
        mField = new Field(width,height,numBombs);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        updateSubtitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mLayoutManager = new GridLayoutManager(getActivity(),mField.getWidth());
        mField.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FieldAdapter(mField.getCellList());
        mRecyclerView.setAdapter(mAdapter);
        mField.setAdapter(mAdapter);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(true);
        Log.i("------------", "fragment onCreateView");
        return view;
    }

    class CellHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        Cell mCell;
        Button mCellButton;

        public CellHolder(View itemView) {
            super(itemView);
            mCellButton = itemView.findViewById(R.id.field_item);
            int width = mField.getWidth();
            int k = 16*30;
            int size = k/width;
            mCellButton.setLayoutParams(new FrameLayout.LayoutParams(size, size));
            mCellButton.setTextSize((float) (size/3));
            mCellButton.setOnClickListener(this);
            mCellButton.setOnLongClickListener(this);
        }
        void bindCell(Cell cell){
            mCell = cell;
            updateButton();
        }

        public void updateButton() {
            String cellText =String.valueOf(mCell.getText());

            if(cellText.equals("0")){
                cellText = "";
                mCellButton.setText(cellText);
                mCellButton.setBackgroundColor(getResources().getColor(R.color.colorEmpty));
                return;

            } else {
                mCellButton.setBackground(getResources().getDrawable(R.drawable.field_cell));
            }


            mCellButton.setText(cellText);

            switch(mCell.getCountBombs()){
                case 1:
                    mCellButton.setTextColor(getResources().getColor(R.color.colorOne));
                    break;
                case 2:
                    mCellButton.setTextColor(getResources().getColor(R.color.colorTwo));
                    break;
                case 3:
                    mCellButton.setTextColor(getResources().getColor(R.color.colorThree));
                    break;
                case 4:
                    mCellButton.setTextColor(getResources().getColor(R.color.colorFour));
                    break;
                case 5:
                    mCellButton.setTextColor(getResources().getColor(R.color.colorFive));
                    break;
                case 6:
                    mCellButton.setTextColor(getResources().getColor(R.color.colorSix));
                    break;
                case 7:
                    mCellButton.setTextColor(getResources().getColor(R.color.colorSeven));
                    break;
                case 8:
                    mCellButton.setTextColor(getResources().getColor(R.color.colorEight));
                    break;
            }
            if(mCell.isMarked() || mCell.isBomb()) {
                mCellButton.setTextColor(Color.BLACK);
            }
        }

        @Override
        public void onClick(View view) {
            switch (mField.onCellClick(mCell)){
                case inGame:{
                    updateButton();
                    break;
                }

                case lose:{
                    updateButton();
                    vibrator.vibrate(vibrTime);
                    FragmentManager manager = getFragmentManager();
                    EndDialogFragment dialog = EndDialogFragment.newInstance(false);
                    dialog.setTargetFragment(SapperFragment.this,REQUEST_ACTION);
                    dialog.setCancelable(false);
                    dialog.show(manager,DIALOG_END);
                    break;
                }
                case win:{
                    updateButton();
                    FragmentManager manager = getFragmentManager();
                    EndDialogFragment dialog = EndDialogFragment.newInstance(true);
                    dialog.setTargetFragment(SapperFragment.this,REQUEST_ACTION);
                    dialog.setCancelable(false);
                    dialog.show(manager,DIALOG_END);
                    break;
                }
            }

        }

        @Override
        public boolean onLongClick(View view) {
            mField.onCellLongClick(mCell);
            updateButton();
            return true;
        }
    }

    class FieldAdapter extends RecyclerView.Adapter<CellHolder>{
        private List<Cell> mCellList;

        public FieldAdapter(List<Cell> cellList){
            mCellList = cellList;
        }
        @Override
        public CellHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.field_item,parent,false);
            return new CellHolder(view);
        }

        @Override
        public void onBindViewHolder(CellHolder holder, int position) {
            Cell cell = mCellList.get(position);
            mLayoutManager.findViewByPosition(position);
            holder.bindCell(cell);
        }

        @Override
        public int getItemCount() {
            return mCellList.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_ACTION){
            listener.onEndDialogResult(data);
        }

        if (requestCode == REQUEST_CUSTOM){
            listener.updateFragment(data);
        }
    }
}
