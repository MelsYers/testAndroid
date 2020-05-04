package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class afterLogin extends AppCompatActivity {

    TextView userView;
    FirebaseUser user;

    DatabaseReference myRef;
    String uid;

    ScrollView scroller ;
    Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        scroller = (ScrollView) findViewById(R.id.scrollview);

        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Teachers");


        userView = (TextView) findViewById(R.id.userView);


        if (user != null) {

            uid = user.getUid();

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                        if(keyNode.getKey().equals(uid)){
                            teacher = keyNode.getValue(Teacher.class);
                            userView.setText(teacher.getUsername());

                            scrollArticles();
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }



    }

    public void scrollArticles(){
        scroller.removeAllViews();// Kill Scrollview's child from last time this ran (and I hope its children?)
        LinearLayout scrollvert = new LinearLayout(this); // Make a new linear layout
        scrollvert.setOrientation(LinearLayout.VERTICAL);// Make it a vertical layout
        scroller.addView(scrollvert); // add this layout to Scrollview (scroller)
        TextView[] tv=new TextView[teacher.getArticles().size()*2];

        int counter = 0;

        for (int i = 0; i < teacher.getArticles().size(); i++) {     //use this line to remove exception of indexoutofbound
            tv[counter] = new TextView(this);
            tv[counter].setText(teacher.getArticles().get(i).getTitle());
            //tv[i].setLayoutParams(new ViewGroup.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv[counter].setTextSize(26);
            scrollvert.addView(tv[counter]);
            counter++;
            tv[counter] = new TextView(this);
            tv[counter].setText(teacher.getArticles().get(i).getArticle());
            //tv[i].setLayoutParams(new ViewGroup.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv[counter].setTextSize(13);
            scrollvert.addView(tv[counter]);  //make this change. Adding to linear layout which is only one child of scrollview.
        }
    }

    public void addArticle(View view){
        Intent intent = new Intent(this, Adding_article.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
