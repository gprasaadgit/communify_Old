package com.gap22.community.apartment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.gap22.community.apartment.Database.Community;
import com.gap22.community.apartment.Database.Poll;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.gap22.community.apartment.R.id.city;
import static com.gap22.community.apartment.R.id.pincode;
import static com.gap22.community.apartment.R.id.state;

public class ViewPolls extends AppCompatActivity {
    ListView lview;

    ArrayList<Poll> poll;
    private FirebaseAuth fireauth;

    private DatabaseReference mDatabase,mpollresults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_polls);

        lview = (ListView) findViewById(R.id.listView2);
        fireauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("poll");
        mpollresults=FirebaseDatabase.getInstance().getReference("pollResults");

        // Firebase ref = new Firebase("https://<yourapp>.firebaseio.com");
        final FirebaseListAdapter adapter = new FirebaseListAdapter(this, Poll.class, R.layout.listitem_poll, mDatabase) {
            @Override
            protected void populateView(View v, Object model, int position) {

                Poll p = (Poll) model;

                ((TextView) v.findViewById(R.id.textView1)).setText(p.getQuestion());
                if (p.getStatus().equalsIgnoreCase("Active")) {
                    ((TextView) v.findViewById(R.id.textView2)).setText("OPEN");
                }
                else
                {
                    ((TextView) v.findViewById(R.id.textView2)).setText("CLOSED");
                }

                ((TextView) v.findViewById(R.id.textView3)).setText(p.getOption1().get("text").toString()+"-"+p.getOption1().get("count"));
                ((TextView) v.findViewById(R.id.textView4)).setText(p.getOption2().get("text").toString()+"-"+p.getOption2().get("count"));
                ((TextView) v.findViewById(R.id.textView5)).setText(p.getOption3().get("text").toString()+"-"+p.getOption3().get("count"));
            }
        };
        lview.setAdapter(adapter);
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!(fireauth.getCurrentUser().getEmail().equals("testadmin@gmail.com")))
                { final Poll p = (Poll) adapter.getItem(position);
                    final String pollid = adapter.getRef(position).getKey();

                    mpollresults.child(adapter.getRef(position).getKey()).child(fireauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                // TODO: handle the case where the data already exists
                                {

                                    Toast.makeText(ViewPolls.this, "Poll Response Already Submitted", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {

                                Bundle bundle = new Bundle();
                                bundle.putString("Question", p.getQuestion());
                                bundle.putString("Option1", p.getOption1().get("text").toString());
                                bundle.putInt("Count1", Integer.parseInt(p.getOption1().get("count").toString()));
                                bundle.putString("Option2", p.getOption2().get("text").toString());
                                bundle.putInt("Count2", Integer.parseInt(p.getOption2().get("count").toString()));
                                bundle.putString("Option3", p.getOption3().get("text").toString());
                                bundle.putInt("Count3", Integer.parseInt(p.getOption3().get("count").toString()));
                                bundle.putString("PollId",pollid );
                                Intent i = new Intent(ViewPolls.this, PollResultActivity.class);
                                i.putExtras(bundle);

//Fire that second activity
                                startActivity(i);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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
