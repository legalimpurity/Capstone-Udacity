package com.vakilapp.lawbooks.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vakilapp.lawbooks.R;
import com.vakilapp.lawbooks.interfaces.BookClickListener;
import com.vakilapp.lawbooks.models.Book;

import java.util.ArrayList;

/**
 * Created by rajatkhanna on 24/09/17.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookItemHolder>{

    private ArrayList<Book> booksObjs;
    private Activity act;
    private BookClickListener clicker;

    public BooksAdapter(Activity act, ArrayList<Book> booksObjs, BookClickListener clicker)
    {
        this.booksObjs = booksObjs;
        this.act = act;
        this.clicker = clicker;
    }

    public void setBooksData(ArrayList<Book> booksObjs)
    {
        this.booksObjs = booksObjs;
        notifyDataSetChanged();
    }

    @Override
    public BookItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(act).inflate(R.layout.book_list_item, parent,false);
        return new BookItemHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(BookItemHolder holder, int position) {
        Book mo = booksObjs.get(position);
        holder.bind(mo);
    }

    @Override
    public int getItemCount() {
        if(booksObjs == null)
            return 0;
        return booksObjs.size();
    }

    public class BookItemHolder extends RecyclerView.ViewHolder
    {

        private TextView initialTextView;
        private TextView noOfChapters;
        private TextView nameTextView;
        private Button downloadButton;

        private BookItemHolder(View itemView) {
            super(itemView);
            initialTextView = (TextView) itemView.findViewById(R.id.book_first_letter);
            nameTextView = (TextView) itemView.findViewById(R.id.book_name);
            downloadButton = (Button) itemView.findViewById(R.id.download_button);
            noOfChapters = (TextView) itemView.findViewById(R.id.book_chapters);
        }

        void bind(final Book mo)
        {
            String title = mo.getName();
            initialTextView.setText(title.charAt(0)+"");
            nameTextView.setText(title);
            noOfChapters.setText(mo.getNumberOfChapters()+" "+act.getResources().getString(R.string.chapters));

            int downloadButtonTitle = R.string.download;
            switch ((int)mo.getDownloaded())
            {
                case 0 : downloadButtonTitle = R.string.download;
                    break;
                case 1 : downloadButtonTitle = R.string.open;
                    break;
                case 2 : downloadButtonTitle = R.string.update;
                    break;
                default: downloadButtonTitle = R.string.download;
                    break;
            }
            downloadButton.setText(downloadButtonTitle);
            if(mo.getDownloaded() == 2)
                downloadButton.setText(R.string.update);
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicker.onBookDownAsked(mo,view);
                }
            });
        }
    }}
