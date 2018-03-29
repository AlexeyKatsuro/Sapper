package com.dedalexey.sapper;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.dedalexey.sapper.SapperFragment.*;

/**
 * Created by Alexey on 07.10.2017.
 */

public class Field {

    public enum Status {win, lose, inGame}

    public static final int RADIUS =1;
    private int mWidth;
    private int mHeight;
    private int mNumBombs;
    private int mSize;
    private int countShownCell;
    private List<Cell> mCellList;
    private GridLayoutManager mLayoutManager;
    private SapperFragment.FieldAdapter mAdapter;

    public void setAdapter(SapperFragment.FieldAdapter adapter) {
        mAdapter = adapter;
    }

    public Field(int width, int height, int numBombs) {

        mWidth = width;
        mHeight = height;
        mNumBombs = numBombs;
        mSize = width*height;
        fillField();
    }

    private void fillField(){
        mCellList = new ArrayList<>();
        for(int i = 0 ; i<mSize; i++){
            mCellList.add(new Cell(i));
        }

        for(int i=0 ,randPosistion; i<mNumBombs;i++){
            randPosistion = (int) (Math.random()*size());
            Cell cell = mCellList.get(randPosistion);
            if(cell.isBomb()){
                i--;
                continue;
            }
            cell.setBomb(true);

            increaseCountBombsAround(cell);
        }

    }

    public Status onCellClick(final Cell cell){
         return open(cell);
    }
    public void onCellLongClick(Cell cell) {
        if(!cell.isShown()) {
            cell.setMarked(!cell.isMarked());
        }

    }
    private Status open(Cell cell) {
        if(cell== null)
            return null;

        if(cell.isMarked()){
            return Status.inGame;
        }
        if(cell.isShown()){
//            if(cell.getCountBombs()!= 0 &&
//                    (cell.getCountBombs()== getCountMarksAround(cell))){
//                openAround(cell);
//            }
            return Status.inGame;
        }

        cell.setShown(true);
        if(cell.isBomb()){
            return Status.lose;
        }
        countShownCell++;
        Log.i("TAAAAAAG","All: "+ mWidth*mHeight + "\nshown: " + countShownCell);
        if(cell.getText().equals("0")){
            openAround(cell);
        }
        if(mWidth*mHeight - countShownCell == mNumBombs){
            return Status.win;
        }
        return Status.inGame;
    }
    private void increaseCountBombsAround(Cell cell){
        int posistion = cell.getPosition();
        int row = posistion/mWidth;
        int colum =  posistion-row*mWidth;
        for(int y = row-RADIUS; y<=row+RADIUS;y++) {
            for (int x = colum - RADIUS; x <= colum + RADIUS; x++) {
                int pos = x + y * mWidth;
                if(x>=0 && y>=0 && x<mWidth && y<mHeight && pos>=0 && pos<mSize) {
                    mCellList.get(pos).increaseCountBombs();
                }
            }
        }
    }

    private int getCountMarksAround(Cell cell){
        int count = 0;
        int posistion = cell.getPosition();
        int row = posistion/mWidth;
        int colum =  posistion-row*mWidth;
        for(int y = row-RADIUS; y<=row+RADIUS;y++) {
            for (int x = colum - RADIUS; x <= colum + RADIUS; x++) {
                int pos = x + y * mWidth;
                if(x>=0 && y>=0 && x<mWidth && y<mHeight && pos>=0 && pos<mSize) {
                    if(mCellList.get(pos).isMarked()){
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private void openAround(Cell cell) {
        int position = cell.getPosition();
        int row = position/mWidth;
        int column =  position-row*mWidth;
        for(int y = row-RADIUS; y<=row+RADIUS;y++) {
            for (int x = column - RADIUS; x <= column + RADIUS; x++) {
                int pos = x + y * mWidth;
                if(x>=0 && y>=0 && x<mWidth && y<mHeight && pos>=0 && pos<mSize) {
                        if(!mCellList.get(pos).isShown()) {
                            View view = mLayoutManager.findViewByPosition(pos);
                            if(view!=null) {
                                Button button = (Button) view.findViewById(R.id.field_item);
                                button.callOnClick();
                            }
                            //open(mCellList.get(pos));
                        }
                }
            }
        }

    }


    public GridLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void setLayoutManager(GridLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getNumBombs() {
        return mNumBombs;
    }

    public List<Cell> getCellList() {
        return mCellList;
    }

    public Cell getCell(int position){
        return mCellList.get(position);
    }

    public int size(){
        return mSize;
    }

    public int getSize() {
        return mSize;
    }


}
