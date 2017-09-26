package com.vakilapp.lawbooks.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vakilapp.lawbooks.R;
import com.vakilapp.lawbooks.models.Book;
import com.vakilapp.lawbooks.models.Chapter;
import com.vakilapp.lawbooks.widget.ChapterWidgetService;

import java.util.ArrayList;

public class ChapterDetailActivity extends AppCompatActivity {

    public static final String CHAP_OBJS = "CHAP_OBJS";
    public static final String BOOK_OBJ = "BOOK_OBJ";
    public static final String SELECTED_CHAP_OBJ = "SELECTED_CHAP_OBJ";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private Book bk;
    private ArrayList<Chapter> chapters_list = new ArrayList<>();
    private int selectedPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setAdapter();
        processFlow(this);

        final AppCompatActivity act = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChapterWidgetService.startActionUpdateRecipeWidgets(act, chapters_list.get(selectedPos));
                Snackbar.make(view, R.string.widget_check, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void processFlow(AppCompatActivity act)
    {
        if(getIntent() != null && getIntent().getExtras() != null) {
            chapters_list = getIntent().getExtras().getParcelableArrayList(CHAP_OBJS);
            selectedPos = getIntent().getExtras().getInt(SELECTED_CHAP_OBJ);
            bk = getIntent().getExtras().getParcelable(BOOK_OBJ);
            getSupportActionBar().setTitle(bk.getName());
            mSectionsPagerAdapter.notifyDataSetChanged();

            mViewPager.setCurrentItem(selectedPos);
        }
        else
            NavUtils.navigateUpFromSameTask(act);
    }

    private void setAdapter()
    {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public static class ChapterDetailFragment extends Fragment {

        private static final String ARG_SECTION_CHAPTER = "ARG_SECTION_CHAPTER";

        private Chapter chap;

        public ChapterDetailFragment() {
        }

        public static ChapterDetailFragment newInstance(Chapter chap) {
            ChapterDetailFragment fragment = new ChapterDetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(ARG_SECTION_CHAPTER, chap);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_chapter_detail, container, false);

            chap = getArguments().getParcelable(ARG_SECTION_CHAPTER);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(chap.getChapterName());

            TextView textView2 = (TextView) rootView.findViewById(R.id.section_label2);
            textView2.setText(chap.getContent());
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ChapterDetailFragment.newInstance(chapters_list.get(position));
        }

        @Override
        public int getCount() {
            return chapters_list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return chapters_list.get(position).getChapterName();
        }
    }
}
