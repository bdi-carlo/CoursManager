package com.coursmanager.app.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.PostCardManager;

import java.util.Random;

public class ShufflePostCardActivity extends AppCompatActivity {

    private TextView tPostCard;
    private Cursor c;
    private boolean recto;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FolderActivity.setAppTheme(this);
        setContentView(R.layout.activity_shuffle_post_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        PostCardManager postCardManager = new PostCardManager(this);
        postCardManager.open();
        this.c = postCardManager.getAllPostCardLesson(intent.getLongExtra("idLesson", 0));
        this.tPostCard = findViewById(R.id.tPostCard);
        this.max = c.getCount();

        //Move to good position of the cursor
        if(savedInstanceState == null) {
            c.moveToPosition(getRandomPosition());
            recto = true;
        }else {
            c.moveToPosition(savedInstanceState.getInt("position", 0));
            recto = savedInstanceState.getBoolean("recto", true);
        }

        //Put the good side of the post-card
        if(recto)
            tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
        else{
            tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_verso));
            tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_VERSO_POSTCARD)));
        }

        FloatingActionButton fabShuffle = findViewById(R.id.fabNext);
        fabShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.moveToPosition(getRandomPosition());
                tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
            }
        });

        FloatingActionButton fabReplay = findViewById(R.id.fabReplay);
        fabReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!recto) {
                    tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                    tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
                    recto = true;
                }
            }
        });

        tPostCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recto) {
                    tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_verso));
                    tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_VERSO_POSTCARD)));
                    recto = false;
                }else{
                    tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                    tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
                    recto = true;
                }
            }
        });
    }

    private int getRandomPosition(){
        return new Random().nextInt(max);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("position", c.getPosition());
        outState.putBoolean("recto", recto);
    }

}
