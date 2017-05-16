package com.gap22.community.apartment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.gap22.community.apartment.Database.Community;
import com.gap22.community.apartment.Database.Member;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PostResponse extends AppCompatActivity {

    private TextView post,title;
    ListView lview;


    private DatabaseReference mDatabase,mpost,mmember;
    private FirebaseAuth fireauth;
    private String pollId;
    private String postQuestion;
    private Button Response;
    private int responsecount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_response);
        lview = (ListView) findViewById(R.id.listView2);
        post =(TextView) findViewById(R.id.Post);
        title =(TextView) findViewById(R.id.Title);

        fireauth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        pollId = bundle.getString("PostId");
        postQuestion = bundle.getString("Post");
        title.setText( bundle.getString("Title"));
        responsecount = bundle.getInt("ResponseCount");
        post.setText(postQuestion);
        Response =(Button) findViewById(R.id.respond);
        mDatabase = FirebaseDatabase.getInstance().getReference("postResponse").child(pollId);
        mmember = FirebaseDatabase.getInstance().getReference("member");
        mpost = FirebaseDatabase.getInstance().getReference("post").child(pollId);
       final FirebaseListAdapter adapter = new FirebaseListAdapter(this, Object.class, android.R.layout.simple_list_item_1, mDatabase)
       {

           @Override
           protected void populateView(View v, Object model, int position) {
           String key = this.getRef(position).getKey();

               HashMap<String,String> h = (HashMap)model;



               final TextView t = (TextView) v.findViewById(android.R.id.text1);
               t.setTextColor(Color.WHITE);

            t.setText(((HashMap) model).get("text").toString()+"-"+((HashMap) model).get("name").toString());



           }
       };
        lview.setAdapter(adapter);


Response.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostResponse.this);
        alert.setTitle("Post Response"); //Set Alert dialog title here
        alert.setMessage("Enter Your Response Here"); //Message here

        // Set an EditText view to get user input
        final EditText input = new EditText(PostResponse.this);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //You will get as string input data in this variable.
                // here we convert the input to a string and show in a toast.
                final HashMap Output = new HashMap();
               final String srt = input.getEditableText().toString();

                mmember.child(fireauth.getCurrentUser().getUid()).addListenerForSingleValueEvent( new ValueEventListener()
                {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Member m = dataSnapshot.getValue(Member.class);

                            Output.put("text",srt);
                            Output.put("name",m.getFirstname());
                        }

                        else
                        {
                            Output.put("text",srt);
                            Output.put("name","Admin");
                        }

                        mDatabase.child(fireauth.getCurrentUser().getUid()).setValue(Output);
                        responsecount = responsecount +1;
                        mpost.child("responses").setValue(responsecount);
                        Toast.makeText(PostResponse.this,"Response Submitted Succesfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PostResponse.this, ViewPostActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            } // End of onClick(DialogInterface dialog, int whichButton)
        }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
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
}
