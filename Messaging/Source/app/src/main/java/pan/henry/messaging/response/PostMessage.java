package pan.henry.messaging.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PostMessage {

    @SerializedName("lat")
    @Expose
    private float latitude;
    @SerializedName("lng")
    @Expose
    private float longitude;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("message_id")
    @Expose
    private String message_id;
    @SerializedName("result")
    @Expose
    private String result;

}