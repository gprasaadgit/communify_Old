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

import com.gap22.community.apartment.Database.Posts;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostActivity extends AppCompatActivity {

    private DatabaseReference mposts,mauthor;
    private FirebaseAuth fireauth;
    private EditText Title,Posts;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        fireauth = FirebaseAuth.getInstance();

        mposts = FirebaseDatabase.getInstance().getReference("post");
        mauthor=FirebaseDatabase.getInstance().getReference("author");
        Title =(EditText) findViewById(R.id.Title);
        Posts =(EditText) findViewById(R.id.Body);
        create =(Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Posts p = new Posts();

                p.setAuthor(fireauth.getCurrentUser().getUid());
                p.setBody(Posts.getText().toString());
                p.setTitle(Title.getText().toString());
                p.setType("Announcement");

                if(validate(p)) {
                    createPosts(p);
                }
            }
        });

    }
    public boolean validate(Posts p)
    {

        if(TextUtils.isEmpty(p.getTitle()))
        {
            Toast.makeText(PostActivity.this,"Please Enter Title",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(p.getBody()))
        {
            Toast.makeText(PostActivity.this,"Please Enter Content",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    public void createPosts(Posts p)
    {

        String key = mposts.push().getKey();

        mposts.child(key).setValue(p);



        mauthor.child(fireauth.getCurrentUser().getUid()).child("posts").child(key).setValue("true");
        Toast.makeText(PostActivity.this, "Post Created", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ViewPostActivity.class));
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
            case R.id.ViewResidents:
                startActivity(new Intent(this, ViewResidents.class));
                finish();
                return true;

            case R.id.Signout:
fireauth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.Residents:
                Intent intent = new AppInviteInvitation.IntentBuilder("Communify App")
                        .setMessage("please install the app")
                        .setDeepLink(Uri.parse("https://play.google.com/store/apps/details?id=com.vazhagavalamudhan.vethathiri&hl=en"))
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
        fireauth = null;
        mposts = null;
        mauthor = null;
    }
}
