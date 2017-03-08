package com.hanekedesign.build;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DoneDone {

    private OkHttpClient client;
    private Gson gson;

    private int projectId;
    private String username;
    private String apiKey;

    private static final String BASE_URL = "https://haneke.mydonedone.com/issuetracker/api/v2/";

    public DoneDone(int projectId, String username, String apiKey){
        this.projectId = projectId;
        this.username = username;
        this.apiKey = apiKey;
        gson = new Gson();
        client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)).build();
    }

    public void PushNextRelease(String projectName, String versionName) throws IOException {
        Call call = client.newCall(new Request.Builder().addHeader("Authentication",getAuthHeader()).url(BASE_URL + "projects/"+projectId+"/issues/all_active.json").build());
        Response response = call.execute();

        List<Integer> retestBugs = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(response.body().string()).getAsJsonObject();
        JsonArray issueArray = o.getAsJsonArray("issues");

        for(int i = 0; i < issueArray.size(); i++){
            JsonObject issue = issueArray.get(i).getAsJsonObject();
            if(issue.has("status")){
                String name = issue.getAsJsonObject("status").get("name").getAsString();
                if(name.equalsIgnoreCase("Ready For Next Release")){
                    retestBugs.add(issue.get("order_number").getAsInt());
                }
            }
        }

         for(int i = 0; i < retestBugs.size(); i++){
             call = client.newCall(new Request.Builder().addHeader("Authentication",getAuthHeader()).url(BASE_URL + "projects/"+projectId+"/issues/"+retestBugs.get(i)).build());
         }

        JsonObject releaseBody = new JsonObject();
        releaseBody.addProperty("order_numbers", createIssueIdList(retestBugs));
        releaseBody.addProperty("title", projectName + " " + versionName);
        releaseBody.addProperty("description", "New release for "+projectName+", version "+versionName);


        call = client.newCall(new Request.Builder()
                .addHeader("Authentication",getAuthHeader())
                .url(BASE_URL + "projects/"+projectId+"/release_builds.json")
                .post(RequestBody.create(MediaType.parse("application/json"),releaseBody.toString()))
                .build());

        Response r = call.execute();
    }

    private String getAuthHeader(){
        return "";
    }

    private String createIssueIdList(List<Integer> bugIds){
        StringBuilder b = new StringBuilder();

        for(int i = 0; i < bugIds.size(); i++){
            b.append(bugIds.get(i));
            if(i < bugIds.size()-1){
                b.append(",");
            }
        }

        return b.toString();
    }
}
