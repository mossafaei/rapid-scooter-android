package ir.MostafaSafaeipour.rapid;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class ScooterIDJson{
    String ScooterID;
}

class StartEndJson{
    String ScooterID;
    String PhoneNumber;
}

class StatusJson{
    String Status;
}

class ScooterDetailListObject{
    String ScooterID;
    int Available;
    double Longitude;
    double Latitude;
    int Battery;
    int Lock;
    int Velocity;
}
class ScooterDetailObject{
    String Status;
    ArrayList<ScooterDetailListObject> List;
}

interface RapidAPIEvents{
    void ScooterDetailReady(final ScooterDetailObject object);
    void OneScooterDetailReady(final ScooterDetailObject object);
    void CheckScooterAvailableReady(final StatusJson object);
    void StartTripReady(final StatusJson object);
    void EndTripReady(final StatusJson object);
}
public class RapidAPI{

    private RapidAPIEvents listener;
    public void addListener(RapidAPIEvents toadd){
        listener = toadd;
    }

    private Request sendPostRequest(String postBody,String url) throws IOException {
        final MediaType jsonmedia = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonmedia,postBody);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;
    }

    public void getStartTrip(String ScooterID,String PhoneNumber) throws IOException {
        Gson gson = new Gson();
        StartEndJson temp = new StartEndJson();
        temp.ScooterID = ScooterID;
        temp.PhoneNumber = PhoneNumber;

        Request request = sendPostRequest(gson.toJson(temp),"http://185.81.99.239:8080/StartTrip");
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                StatusJson status = gson.fromJson(response.body().string(),StatusJson.class);
                listener.StartTripReady(status);
            }
        });

    }

    public void getEndTrip(String ScooterID,String PhoneNumber) throws IOException {
        final Gson gson = new Gson();
        StartEndJson temp = new StartEndJson();
        temp.PhoneNumber = PhoneNumber;
        temp.ScooterID = ScooterID;

        Request request = sendPostRequest(gson.toJson(temp),"http://185.81.99.239:8080/EndTrip");
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                StatusJson status = gson.fromJson(response.body().string(),StatusJson.class);
                listener.EndTripReady(status);
            }
        });
    }

    public void getOneScooterDetail(String ScooterID) throws IOException{
        Gson gson = new Gson();
        ScooterIDJson temp = new ScooterIDJson();
        temp.ScooterID = ScooterID;

        Request request = sendPostRequest(gson.toJson(temp),"http://185.81.99.239:8080/OneScooterDetail");
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ScooterDetailObject scoot = gson.fromJson(response.body().string(),ScooterDetailObject.class);
                listener.OneScooterDetailReady(scoot);
            }
        });

    }

    public void getScooterDetail() throws IOException {
        Gson gson = new Gson();
        Request request = sendPostRequest("","http://185.81.99.239:8080/testapi");
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ScooterDetailObject scoot = gson.fromJson(response.body().string(),ScooterDetailObject.class);
                listener.ScooterDetailReady(scoot);
            }
        });
    }

    public void getCheckScooterAvailable(String ScooterID) throws IOException {
        Gson gson = new Gson();
        ScooterIDJson temp = new ScooterIDJson();
        temp.ScooterID = ScooterID;

        final Request request = sendPostRequest(gson.toJson(temp),"http://185.81.99.239:8080/CheckScooterAvailable");
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                StatusJson Status = gson.fromJson(response.body().string(),StatusJson.class);
                listener.CheckScooterAvailableReady(Status);
            }
        });

    }
}
