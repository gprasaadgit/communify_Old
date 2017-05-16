package com.gap22.community.apartment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.gap22.community.apartment.Database.Member;
import com.gap22.community.apartment.Database.Posts;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.R.attr.id;

public class ViewResidents extends AppCompatActivity {


    ListView lview;


    private DatabaseReference mDatabase;
    private FirebaseAuth fireauth;
    FirebaseListAdapter adapter;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_residents);
        lview = (ListView) findViewById(R.id.listView2);

        mDatabase = FirebaseDatabase.getInstance().getReference("member");
        fireauth = FirebaseAuth.getInstance();

        // Firebase ref = new Firebase("https://<yourapp>.firebaseio.com");
          adapter = new FirebaseListAdapter(this, Member.class, R.layout.listitem_residents, mDatabase) {
            @Override
            protected void populateView(View v, Object model, int position) {
                Member m = (Member) model;
                ((TextView) v.findViewById(R.id.unit)).setText(m.getUnit());
                ((TextView) v.findViewById(R.id.Name)).setText(m.getFirstname());
                ((TextView) v.findViewById(R.id.status)).setText("Active");
String obj = this.getRef(position).getKey();

                StorageReference storageRef = storage.getReferenceFromUrl("gs://communify-4b71c.appspot.com/"+obj+".jpg");

// ImageView in your Activity
                ImageView imageView = (ImageView)v.findViewById(R.id.img);

// Load the image using Glide
                Glide.with(ViewResidents.this )
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .into(imageView);

            }
        };
        lview.setAdapter(adapter);

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        if (fireauth.getCurrentUser().getEmail().equals("testadmin@gmail.com")) {
            inflater.inflate(R.menu.adminmenu, menu);
        } else {
            inflater.inflate(R.menu.menu, menu);
        }
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
}
