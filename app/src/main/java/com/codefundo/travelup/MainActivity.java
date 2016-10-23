package com.codefundo.travelup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Button fbbutton,getfirendsbutton, btn_nextActivity;
    private ImageView fbimage,friend_fbimage;
    private  Bitmap bitmap_friend_image;
    private TextView greet;
    String str_id;
    public static final String PREFS_NAME = "MyApp_Settings";
    SharedPreferences settings;

    String friend_id,friend_name;
    private AccessToken mAccessToken;

    // Creating Facebook CallbackManager Value
    public static CallbackManager callbackmanager;

    public static boolean isUserLoggedIn= false;

    private final String TAG_CANCEL="Cancelled login.";
    private  final String TAG_ERROR="Error logging in.";

    private ArrayList<String> friendlist_toget=new ArrayList<>();


    //test map
    Button btn_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Initialize SDK before setContentView(Layout ID)
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        // Initialize layout button
        fbbutton = (Button) findViewById(R.id.login_button);
        getfirendsbutton = (Button) findViewById(R.id.btn_getfriends);
        btn_nextActivity=(Button) findViewById(R.id.next_activity);

        //intialize image
        fbimage = (ImageView) findViewById(R.id.imageView);
        friend_fbimage=(ImageView) findViewById(R.id.imageView1);


        //say hello to mr..?
        greet = (TextView) findViewById(R.id.greetings);

        //shared prefs
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        btn_map=(Button) findViewById(
                R.id.map
        );

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(MainActivity.this, SelectLocationActivity.class);
                startActivity(i1);
            }
        });
        fbbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Call private method
                if(!isUserLoggedIn) {
                    onFblogin();
                }
            }
        });

        getfirendsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_id!=null) {
                    requestFriends();
                }
                else
                {

                    Toast.makeText(getApplicationContext(),"Wait for login..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(str_id!=null) {
                    requestFriends();


                    requestFriends();
                    Intent i = new Intent(MainActivity.this, DisplayLocationReviewsActivity.class);
                    friendlist_toget.add(friend_name);

                    i.putExtra("USER-FILTERED-FRIEND-LIST", friendlist_toget);
                    i.putExtra("USER-SELECTED-LOCATION", "Manipal");
                    //Convert to byte array
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap_friend_image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    i.putExtra("bitmap", byteArray);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(MainActivity.this, DisplayLocationReviewsActivity.class);
                    friendlist_toget.add("Yash");

                    i.putExtra("USER-FILTERED-FRIEND-LIST", friendlist_toget);
                    i.putExtra("USER-SELECTED-LOCATION", "Manipal");
                    //Convert to byte array
                    bitmap_friend_image=BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap_friend_image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    i.putExtra("bitmap", byteArray);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),"Wait for login..", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    private void requestFriends() {


/* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+str_id+"/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        String jsonresult = String.valueOf(response.getJSONObject());
                        System.out.println("JSON Result" + jsonresult);
                        try {
                            JSONArray data=  response.getJSONObject().getJSONArray("data");

                            Log.i("ID ", ""+data.getJSONObject(0).get("id").toString());
                            Log.i("Name ", ""+data.getJSONObject(0).get("name").toString());
                            friend_id=data.getJSONObject(0).get("id").toString();
                            friend_name=data.getJSONObject(0).get("name").toString();

                            new DownloadfbImage(friend_fbimage).execute("https://graph.facebook.com/" + friend_id + "/picture?type=large");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    // Private method to handle Facebook login and callback
    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile","user_friends"));



        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mAccessToken=loginResult.getAccessToken();
                        System.out.println("Success");
                        GraphRequest request =    GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                               /* String str_email = json.getString("email");*/
                                                str_id = json.getString("id");
                                                String str_firstname = json.getString("name");


                                                System.out.println("JSON-->"+ " "+str_firstname);

                                                //greet the user
                                                greet.setText("Welcome, "+ str_firstname);
                                               // Log.i("Perms ", ": "+AccessToken.getCurrentAccessToken().getPermissions());
                                                //start downloading image
                                                new DownloadfbImage(fbimage).execute("https://graph.facebook.com/" + str_id + "/picture?type=large");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,friends");
                        request.setParameters(parameters);
                        request.executeAsync();

                        // Writing data to SharedPreferences
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("uid", str_id);
                        editor.commit();


                       /* *//* make the API call *//*
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/{user-id}/friends",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
             //handle the result
                                        String jsonresult1 = String.valueOf(response.getJSONArray());
                                        System.out.println("mJSON Result" + jsonresult1);
                                    }
                                }
                        ).executeAsync();*/
                      /*  GraphRequest.newMyFriendsRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray objects, GraphResponse response) {
                                String jsonresult1 = String.valueOf(objects);
                                System.out.println("mJSON Result" + jsonresult1);
                            }
                        }).executeAsync();*/
                        isUserLoggedIn=true;
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG_CANCEL, "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG_ERROR, error.toString());
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    private class DownloadfbImage extends AsyncTask<String,Void,Bitmap>
    {
        ImageView image;

        public DownloadfbImage(ImageView fbimage) {

            this.image=fbimage;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Toast.makeText(getApplicationContext(),"image received",Toast.LENGTH_SHORT).show();
            image.setImageBitmap(bitmap);
            bitmap_friend_image=bitmap;

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap returnbitmap=null;
            try {
                URL rurl= new URL(url);
                returnbitmap = BitmapFactory.decodeStream(rurl.openConnection().getInputStream());


            } catch (Exception e) {
                e.printStackTrace();
            }


            return returnbitmap;
        }
    }
}
