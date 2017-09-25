package com.vakilapp.lawbooks.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vakilapp.lawbooks.R;
import com.vakilapp.lawbooks.interfaces.ChapterClickListener;
import com.vakilapp.lawbooks.models.Chapter;

import java.util.ArrayList;

/**
 * Created by rajatkhanna on 24/09/17.
 */

public class ChaptersListAdapter extends RecyclerView.Adapter<ChaptersListAdapter.BookItemHolder>{

    private ArrayList<Chapter> chaptersObjs;
    private Activity act;
    private ChapterClickListener clicker;

    public ChaptersListAdapter(Activity act, ArrayList<Chapter> chaptersObjs, ChapterClickListener clicker)
    {
        this.chaptersObjs = chaptersObjs;
        this.act = act;
        this.clicker = clicker;
    }

    public void setChaptersData(ArrayList<Chapter> chaptersObjs)
    {
        this.chaptersObjs = chaptersObjs;
        notifyDataSetChanged();
    }

    @Override
    public BookItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(act).inflate(R.layout.chapter_list_item, parent,false);
        return new BookItemHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(BookItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(chaptersObjs == null)
            return 0;
        return chaptersObjs.size();
    }

    public class BookItemHolder extends RecyclerView.ViewHolder
    {

        private TextView initialTextView;
        private LinearLayout rootLay;
        private TextView nameTextView;

        private BookItemHolder(View itemView) {
            super(itemView);
            initialTextView = (TextView) itemView.findViewById(R.id.chapter_first_letter);
            nameTextView = (TextView) itemView.findViewById(R.id.chapter_name);
            rootLay = (LinearLayout) itemView.findViewById(R.id.rootLay);
        }

        void bind(final int bookPos)
        {
            Chapter chap = chaptersObjs.get(bookPos);
            String title = chap.getChapterName();
            initialTextView.setText(title.charAt(0)+"");
            if(TextUtils.isDigitsOnly(title.charAt(0)+""))
                nameTextView.setText(title.substring(3));
            else
                nameTextView.setText(title);

            rootLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicker.onChapterClicked(bookPos,chaptersObjs,view);
                }
            });
//            noOfChapters.setText(chap.getContent().substring(0,10));

//            int downloadButtonTitle = R.string.download;
//            switch ((int)mo.getDownloaded())
//            {
//                case 0 : downloadButtonTitle = R.string.download;
//                    break;
//                case 1 : downloadButtonTitle = R.string.open;
//                    break;
//                case 2 : downloadButtonTitle = R.string.update;
//                    break;
//                default: downloadButtonTitle = R.string.download;
//                    break;
//            }
//            downloadButton.setText(downloadButtonTitle);
//            if(mo.getDownloaded() == 2)
//                downloadButton.setText(R.string.update);
//            downloadButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    clicker.onBookDownAsked(bookPos,view);
//                }
//            });
        }
    }}
