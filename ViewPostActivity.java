package com.gap22.community.apartment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.gap22.community.apartment.Database.Poll;
import com.gap22.community.apartment.Database.Posts;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.os.Build.VERSION_CODES.M;
import static com.gap22.community.apartment.R.id.Post;


public class ViewPostActivity extends AppCompatActivity {


    ListView lview;


    private DatabaseReference mDatabase;
    private FirebaseAuth fireauth;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        lview = (ListView) findViewById(R.id.listView2);
        mDatabase = FirebaseDatabase.getInstance().getReference("post");
        fireauth = FirebaseAuth.getInstance();

        // Firebase ref = new Firebase("https://<yourapp>.firebaseio.com");
       final FirebaseListAdapter adapter = new FirebaseListAdapter(this, Posts.class, R.layout.listitem_row, mDatabase) {
            @Override
            protected void populateView(View v, Object model, int position) {
                Posts p = (Posts) model;
                ((TextView) v.findViewById(R.id.textView1)).setText(p.getTitle());
                ((TextView) v.findViewById(R.id.textView2)).setText(p.getBody());
                ((TextView) v.findViewById(R.id.textView3)).setText("Responses:"+p.getResponses());
            }
        };
        lview.setAdapter(adapter);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                {
                     Posts p = (Posts) adapter.getItem(position);
                     String postId= adapter.getRef(position).getKey();
                    Bundle bundle = new Bundle();
                 bundle.putString("Post",p.getBody());
                    bundle.putString("Title",p.getTitle());
                    bundle.putString("PostId",postId );
                    bundle.putInt("ResponseCount",p.getResponses());
                    Intent i = new Intent(ViewPostActivity.this, PostResponse.class);
                    i.putExtras(bundle);

//Fire that second activity
                    startActivity(i);

                }

            }
        });
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: handle the case where the data already exists
        fireauth = null;
        mDatabase = null;
    }

}

