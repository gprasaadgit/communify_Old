package com.gap22.community.apartment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.gap22.community.apartment.Database.Community;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.gap22.community.apartment.R.id.pincode;

public class ViewCommunityUser extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth fireauth;
    ListView lview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_community_user);
        lview = (ListView) findViewById(R.id.listView2);
        mDatabase = FirebaseDatabase.getInstance().getReference("community");
        fireauth = FirebaseAuth.getInstance();

        // Firebase ref = new Firebase("https://<yourapp>.firebaseio.com");
        ListAdapter adapter = new FirebaseListAdapter(this, Community.class, R.layout.listitem_viewcommunity, mDatabase) {
            @Override
            protected void populateView(View v, Object model, int position) {
                Community c =(Community) model;

                ((TextView) v.findViewById(R.id.Name)).setText("Name : "+c.getName());
                ((TextView) v. findViewById(R.id.builder)).setText("Builder : "+c.getBuilder() );
                ((TextView) v. findViewById(R.id.sec)).setText("Seceratary : "+c.getSeceretary() );
                ((TextView) v. findViewById(R.id.city)).setText("City : "+c.getCity() );
                ((TextView) v. findViewById(R.id.state)).setText("State : "+c.getState() );
                ((TextView) v. findViewById(R.id.Address1)).setText("Address : "+c.getAddress1() );

                ((TextView) v.findViewById(pincode)).setText("Pincode :"+c.getPinCode());
                ((TextView) v.findViewById(R.id.tax)).setText("TaxId :"+c.getTaxID());
            }
        };
        lview.setAdapter(adapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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

        mDatabase = null;
    }
}
