package com.gap22.community.apartment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.key;

public class PollResultActivity extends AppCompatActivity {

    private TextView Question;
    private RadioButton Option1,Option2,Option3;
    private Button Submit;
    private RadioGroup result;
    private String option1,option2,option3;
    private String resultvalue;
    private String pollId;
    private DatabaseReference mpolls,mpollresults;
    private FirebaseAuth fireauth;
    int finalcount,option1count,option2count,option3count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_result);
        fireauth = FirebaseAuth.getInstance();
        mpolls = FirebaseDatabase.getInstance().getReference("poll");
        mpollresults=FirebaseDatabase.getInstance().getReference("pollResults");
        Question =(TextView)findViewById(R.id.Question);
        Option1 =(RadioButton)findViewById(R.id.radioOption1);
        Option2 =(RadioButton)findViewById(R.id.radioOption2);
        Option3 =(RadioButton)findViewById(R.id.radioOption3);
        result =(RadioGroup) findViewById(R.id.radioResult);
        Submit =(Button)findViewById(R.id.submit);
        Bundle bundle = getIntent().getExtras();

        option1 = bundle.getString("Option1");
        option2 = bundle.getString("Option2");
        option3 = bundle.getString("Option3");
        option1count = bundle.getInt("Count1");
        option2count = bundle.getInt("Count2");
        option3count = bundle.getInt("Count3");

        pollId = bundle.getString("PollId");
        Question.setText(bundle.getString("Question"));
        Option1.setText(option1);
        Option1.setTextColor(Color.WHITE);
        Option2.setText(option2);
        Option2.setTextColor(Color.WHITE);
        Option3.setText(option3);
        Option3.setTextColor(Color.WHITE);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int selectedId = result.getCheckedRadioButtonId();

                // find the radiobutton by returned id
               if(Option1== (RadioButton) findViewById(selectedId))
               {
                   resultvalue = "option1";
                   finalcount = 1+option1count;
               }

               else if(Option2== (RadioButton) findViewById(selectedId))
                {
                    resultvalue = "option2";
                    finalcount = 1+option2count;
                }
                else
                {
                    resultvalue = "option3";
                    finalcount = 1+option3count;
                }
                mpollresults.child(pollId).child(fireauth.getCurrentUser().getUid()).setValue(resultvalue);
                mpolls.child(pollId).child(resultvalue).child("count").setValue(finalcount);
                Toast.makeText(PollResultActivity.this, "Poll Response Submitted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PollResultActivity.this, ViewPolls.class));
                finish();

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
}
