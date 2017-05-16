package com.gap22.community.apartment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.gap22.community.apartment.Database.Community;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewCommunity extends AppCompatActivity {
    EditText name,buildername,seceretary,city,state,address,address2,pincode,taxid;
    Button Update;
    private DatabaseReference mDatabase;
    private FirebaseAuth fireauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_community);
        fireauth = FirebaseAuth.getInstance();

        name = (EditText) findViewById(R.id.Name);
        buildername = (EditText) findViewById(R.id.builder);
        seceretary = (EditText) findViewById(R.id.sec);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        address = (EditText) findViewById(R.id.Address1);
        address2 = (EditText) findViewById(R.id.Address2);
        pincode = (EditText) findViewById(R.id.pincode);
        taxid = (EditText) findViewById(R.id.tax);
      Update = (Button) findViewById(R.id.update);
       Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
click();
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference("community");
        //Community cmt = callQuery();


        mDatabase.child(fireauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // TODO: handle the case where the data already exists
                    {
                    Community    cmt = dataSnapshot.getValue(Community.class);

                        name.setText(cmt.getName());
                        buildername.setText(cmt.getBuilder());
                        seceretary.setText(cmt.getSeceretary());
                        city.setText(cmt.getCity());
                        state.setText(cmt.getState());
                        address.setText(cmt.getAddress1());
                        address2.setText(cmt.getAddress2());
                        pincode.setText(""+cmt.getPinCode());
                        taxid.setText(cmt.getTaxID());

                    }
                }
                else {
                    // TODO: handle the case where the data does not yet exist
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewCommunity.this);


                    // set title
                    alertDialogBuilder.setTitle("Community Not Created");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click OK to exit!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, close
                                    // current activity

                                    startActivity(new Intent(ViewCommunity.this, CreateCommunityActivity.class));
                                    finish();

                                }
                            });


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       /* if (cmt != null) else {

        }*/


    }
public void click()
{
    Community c = new Community();
    c.setAddress1(address.getText().toString());
    c.setAddress2(address2.getText().toString());
    c.setBuilder(buildername.getText().toString());
    c.setCity(city.getText().toString());
    c.setState(state.getText().toString());
    System.out.println("pincode"+pincode.getText().toString());
    int a = Integer.parseInt(pincode.getText().toString());
    c.setPinCode(a);
    c.setName(name.getText().toString());
    c.setSeceretary(seceretary.getText().toString());
    c.setStatus(1);
    c.setTaxID(taxid.getText().toString());
    if(validate(c)) {
        Update(c);
    }

}
    public boolean validate(Community c)
    {

        if(TextUtils.isEmpty(c.getName()))
        {
            Toast.makeText(ViewCommunity.this,"Please Enter Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(c.getBuilder()))
        {
            Toast.makeText(ViewCommunity.this,"Please Enter BuilderName",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(c.getSeceretary()))
        {
            Toast.makeText(ViewCommunity.this,"Please Enter Seceretary",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(c.getAddress1()))
        {
            Toast.makeText(ViewCommunity.this,"Please Enter Address1",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(c.getAddress2()))
        {
            Toast.makeText(ViewCommunity.this,"Please Enter Address2",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(c.getCity()))
        {
            Toast.makeText(ViewCommunity.this,"Please Enter City",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(c.getState()))
        {
            Toast.makeText(ViewCommunity.this,"Please Enter State",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(c.getTaxID()))
        {
            Toast.makeText(ViewCommunity.this,"Please Enter TaxId",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void Update(Community c)
    {
        // String userId = mDatabase.push().getKey();

        mDatabase.child(fireauth.getCurrentUser().getUid()).setValue(c);
        Toast.makeText(ViewCommunity.this,"Community Details Updated",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ViewPostActivity.class));
        finish();

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

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.adminmenu, menu);

        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: handle the case where the data already exists
        fireauth = null;
        mDatabase = null;
    }

}
