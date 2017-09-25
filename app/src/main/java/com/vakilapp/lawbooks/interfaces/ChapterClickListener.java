package com.vakilapp.lawbooks.interfaces;

import android.view.View;

import com.vakilapp.lawbooks.models.Chapter;

import java.util.ArrayList;

/**
 * Created by rajatkhanna on 02/08/17.
 */

public interface ChapterClickListener {
    public void onChapterClicked(int chaperPosBook, ArrayList<Chapter> chaptersObjs, View view);
}
