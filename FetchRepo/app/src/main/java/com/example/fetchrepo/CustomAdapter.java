package com.example.fetchrepo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.InputStream;

public class CustomAdapter extends ArrayAdapter {

    private final Context context;
    protected Repository[] repos;

    public CustomAdapter(Context context, int resId) {
        super(context, resId);
        this.context = context;
    }

    public void setData(Repository[] repos) {
        this.repos = repos;

        for (int i=0; i<repos.length; i++) {
            add(null);
        }
    }

    @androidx.annotation.NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Activity.LAYOUT_INFLATER_SERVICE);

            // if we are not responsible for adding the view to the parent,
            // then attachToRoot should be 'false' (which is in our case)
            view = inflater.inflate(R.layout.row, parent, false);
        }

        // set avatar display
        ImageView avatar = view.findViewById(R.id.avatar);
        Thread bkgdThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    InputStream input = new java.net.URL(repos[pos].getAvatar()).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    avatar.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bkgdThread.start();

        // set the text for display name
        TextView name = view.findViewById(R.id.name);
        name.setText("Name: " + repos[pos].getDisplayName());

        // set the text for type of repo
        TextView type = view.findViewById(R.id.type);
        type.setText("User type: " + repos[pos].getType());

        // set the text for date of creation
        TextView date = view.findViewById(R.id.date);
        date.setText("Date of creation: " + repos[pos].getDateOfCreation());

        return view;
    }
}
