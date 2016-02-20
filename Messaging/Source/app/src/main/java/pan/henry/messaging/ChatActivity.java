package pan.henry.messaging;

import pan.henry.messaging.response.GetMessage;
import pan.henry.messaging.response.PostMessage;
import pan.henry.messaging.response.ResultList;
import pan.henry.messaging.SecureRandomString;

import java.util.List;
import java.util.Iterator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ChatActivity extends AppCompatActivity {
    //initialize
    EditText user_message;
    ListView message;
    Button refresh_button;
    Button post_button;
    String user_id;
    String nickname;
    List<ResultList> result_list;
    String result;
    Location loc;
    float lat;
    float lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user_message = (EditText) findViewById(R.id.editText2);
        message = (ListView) findViewById(R.id.listView);
        refresh_button = (Button) findViewById(R.id.refreshButton);
        post_button = (Button) findViewById(R.id.postButton);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = settings.getString("user_id", null);
        nickname = settings.getString("user_nickname", null);
        loc = LocationData.getLocationData().getLocation();
        lat = (float) loc.getLatitude();
        lng = (float) loc.getLongitude();
        refresh();
    }

    /*
    Method shared by the two buttons. Both will close the keyboard when clicked,
    and will do their respective actions.
     */
    public void onClick(View v) {
        hideKeyboard();
        switch(v.getId()){
            case R.id.refreshButton:
                refresh();
                break;
            case R.id.postButton:
                post();
                break;
            default:
                break;
        }
    }

    /*
    Refresh the chat. Any new messages will appear if they exist.
     */
    public void refresh() {
        Retrofit retrofit = getRetrofit();
        GetService getservice = retrofit.create(GetService.class);

        Call<GetMessage> call = getservice.getMessage(result_list,result,lat,lng,user_id);
        call.enqueue(new Callback<GetMessage>() {
            @Override
            public void onResponse(Response<GetMessage> response) {
                if (response.code() == 200)
                    showMessages(response.body().resultList);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    /*
    Post your message and clears your text box.
    Also updates the chat.
    */
    public void post() {
        String post = user_message.getText().toString();
        if (!post.isEmpty()){ //post only if post is not empty.
            Retrofit retrofit = getRetrofit();
            PostService postservice = retrofit.create(PostService.class);

            SecureRandomString srs = new SecureRandomString(); //give a random message id
            String message_id = srs.nextString();

            Call<PostMessage> call = postservice.postMessage(lat,lng,user_id,nickname,post,message_id);
            call.enqueue(new Callback<PostMessage>() {
                @Override
                public void onResponse(Response<PostMessage> response) {
                    if (response.code() == 200) {
                        user_message.setText(""); //reset the text box
                        refresh();
                    }
                }

                @Override
                public void onFailure(Throwable t) {}
            });
        }
    }

    /*
    Show the messages in the chat.
     */
    public void showMessages(List<ResultList> messages) {
        Iterator<ResultList> messagesCount = messages.iterator();
        String[] messageArray = new String[20]; //amount of messages stored

        int n = 20; //index
        while (messagesCount.hasNext()) {
            ResultList L = messagesCount.next();
            if(L.userId.equals(user_id)){ //distinguish yourself from others
                messageArray[n - 1] = L.nickname + " (you): " + L.message;
            } else {
                messageArray[n - 1] = L.nickname + ": " + L.message;
            }
            n--;
        }
        Display adapter = new Display(ChatActivity.this, messageArray);
        message = (ListView) findViewById(R.id.listView);
        message.setAdapter(adapter);
    }

    /*
    Hides the keyboard.
    */
    public void hideKeyboard() {
        if(getCurrentFocus()!= null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /*
    Retrofit call.
    */
    public Retrofit getRetrofit(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/default/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        return retrofit;
    }

    public interface PostService {
        @POST ("post_message/")
        Call<PostMessage> postMessage (@Query("lat") float lat,
                                       @Query("lng") float lng,
                                       @Query("user_id") String user_id,
                                       @Query("nickname") String nickname,
                                       @Query("message") String message,
                                       @Query("message_id") String message_id
                                       );
    }

    public interface GetService {
        @GET ("get_messages/")
        Call<GetMessage> getMessage (@Query ("result_list") List<ResultList> resultList,
                                     @Query ("result") String result,
                                     @Query ("lat") float lat,
                                     @Query ("lng") float lng,
                                     @Query ("user_id") String user_id);
    }

}
