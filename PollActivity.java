package com.gap22.community.apartment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gap22.community.apartment.Database.Poll;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.HashMap;

public class PollActivity extends AppCompatActivity {
    private EditText Question,Option1,Option2,Option3;
    private Button create;
    private DatabaseReference mDatabase;
    private FirebaseAuth fireauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        Question=(EditText)findViewById(R.id.Question);
        Option1=(EditText)findViewById(R.id.option1);
        Option2=(EditText)findViewById(R.id.option2);
        Option3=(EditText)findViewById(R.id.option3);
        create =(Button)findViewById(R.id.create);
        mDatabase = FirebaseDatabase.getInstance().getReference("poll");
        fireauth = FirebaseAuth.getInstance();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Poll p = new Poll();
             //   p.setOption1(Option1.getText().toString());

                HashMap option = new HashMap();
                option.put("text",Option1.getText().toString());
                option.put("count",0);
                HashMap option2 = new HashMap();
                option2.put("text",Option2.getText().toString());
                option2.put("count",0);
                HashMap option3 = new HashMap();
                option3.put("text",Option3.getText().toString());
                option3.put("count",0);
p.setOption1(option);
                p.setOption2(option2);
                p.setOption3(option3);
                p.setQuestion(Question.getText().toString());
                p.setStatus("Active");
                if(validate(p)) {
                    createPoll(p);
                }
            }
        });
    }

    public boolean validate(Poll p)
    {

        if(TextUtils.isEmpty(p.getQuestion()))
        {
            Toast.makeText(PollActivity.this,"Please Enter Question",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(p.getOption1().get("text").toString()))
        {
            Toast.makeText(PollActivity.this,"Please Enter Option1",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(p.getOption2().get("text").toString()))
        {
            Toast.makeText(PollActivity.this,"Please Enter Option2",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(p.getOption3().get("text").toString()))
        {
            Toast.makeText(PollActivity.this,"Please Enter Option3",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void createPoll(Poll p)
    {
        String userId = mDatabase.push().getKey();

        mDatabase.child(userId).setValue(p);



        Toast.makeText(PollActivity.this, "Poll Created", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ViewPolls.class));
        finish();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.adminmenu, menu);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection

        switch (item.getItemId()) {
            case R.id.Polls:
                startActivity(new Intent(this, ViewPolls.class));
                finish();
                return true;
            case R.id.Community:
                startActivity(new Intent(this, ViewCommunityUser.class));
                finish();
                return true;
            case R.id.Posts:
                startActivity(new Intent(this, ViewPostActivity.class));
                finish();
                return true;
            case R.id.CreateCommunity:
                startActivity(new Intent(this, CreateCommunityActivity.class));
                finish();
                return true;
            case R.id.ViewCommunity:
                startActivity(new Intent(this, ViewCommunity.class));
                finish();
                return true;
            case R.id.CreatePosts:
                startActivity(new Intent(this, PostActivity.class));
                finish();
                return true;
            case R.id.CreatePolls:
                startActivity(new Intent(this, PollActivity.class));
                finish();
                return true;
            case R.id.Signout:
                fireauth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.ViewResidents:
                startActivity(new Intent(this, ViewResidents.class));
                finish();
                return true;

            case R.id.Residents:
                Intent intent = new AppInviteInvitation.IntentBuilder("Communify App")
                        .setMessage("please install the app")
                        //.setDeepLink(Uri.parse("https://play.google.com/store/apps/details?id=com.vazhagavalamudhan.vethathiri&hl=en"))
                        //.setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))

                        .setEmailSubject("App Invite")
                        .setEmailHtmlContent("<html><body>https://play.google.com/store/apps/details?id=com.vazhagavalamudhan.vethathiri&hl=en<br>Install</body></html>")
                        .build();
                startActivityForResult(intent, 59);


            default:
                return super.onOptionsItemSelected(item);
        }


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: handle the case where the data already exists

        mDatabase = null;
    }
}
