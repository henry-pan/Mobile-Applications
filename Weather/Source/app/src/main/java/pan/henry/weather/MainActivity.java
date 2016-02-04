package pan.henry.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import pan.henry.weather.response.Result;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

/*
Can't see all of the text? Try scrolling down!
You can do this by touching the screen and moving your finger.
 */

public class MainActivity extends AppCompatActivity {
    String weatherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /*
    onClick is called when the button is pressed.
    It will use retrofit to get weather data.
     */
    public void onClick (View v){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://luca-teaching.appspot.com/weather/")
                .addConverterFactory(GsonConverterFactory.create()) //parse Gson string
                .client(httpClient) //add logging
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        Call<Result> queryResponseCall = service.weather();

        queryResponseCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response) {
                if(response.code() == 500) { //500 Server Error
                    TextView displayText = (TextView) findViewById(R.id.display);
                    displayText.setText("500 Server Error - Try again");
                    displayText.setVisibility(View.VISIBLE);
                } else if(response.body().response.result.equals("error")) { //Application Error
                    TextView displayText = (TextView) findViewById(R.id.display);
                    displayText.setText("Application Error - Try again");
                    displayText.setVisibility(View.VISIBLE);
                } else { //Normal
                    weatherText = "City: " + response.body().response.conditions.observationLocation.city + "\n" +
                            "Elevation: " + response.body().response.conditions.observationLocation.elevation + "\n" +
                            "Temperature: " + String.format("%.2f F / %.2f C",
                            response.body().response.conditions.tempF,
                            response.body().response.conditions.tempC)+ "\n" +
                            "Relative Humidity : " + response.body().response.conditions.relativeHumidity + "\n" +
                            "Wind Average: " + String.format("%.2f ", response.body().response.conditions.windMph) + "\n" +
                            "Wind Gust: " + response.body().response.conditions.windGustMph + "\n" +
                            "Pressure: " + response.body().response.conditions.pressureMb + " mb\n" +
                            "Dewpoint: " + response.body().response.conditions.dewpointF + " F / " +
                            response.body().response.conditions.dewpointC + " C\n" +
                            "Weather: " + response.body().response.conditions.weather;
                    TextView displayText = (TextView) findViewById(R.id.display);
                    displayText.setText(weatherText);
                    displayText.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Throwable t) {//other Error
                TextView displayText = (TextView) findViewById(R.id.display);
                displayText.setText("Error");
            }
        });
    }

    public interface WeatherService {
        @GET ("default/get_weather")
        Call<Result> weather ();
    }
}
