package com.hanekedesign.build;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DoneDone {

    private OkHttpClient client;

    private int projectId;
    private String username;
    private String apiKey;

    private static final String BASE_URL = "https://haneke.mydonedone.com/issuetracker/api/v2/";

    public DoneDone(int projectId, String username, String apiKey){
        this.projectId = projectId;
        this.username = username;
        this.apiKey = apiKey;

        client = new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String s) {
                System.out.println(s);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    }

    public boolean PushNextRelease(String projectName, String versionName) throws IOException {
        Call call = client.newCall(new Request.Builder().addHeader("Authorization",getAuthHeader()).get().url(BASE_URL + "projects/"+projectId+"/issues/all_active.json").build());
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

        JsonObject releaseBody = new JsonObject();
        releaseBody.addProperty("order_numbers", createIssueIdList(retestBugs));
        releaseBody.addProperty("title", projectName + " " + versionName);
        releaseBody.addProperty("description", "New release for "+projectName+", version "+versionName);

        call = client.newCall(new Request.Builder()
                .addHeader("Authorization",getAuthHeader())
                .url(BASE_URL + "projects/"+projectId+"/release_builds.json")
                .post(RequestBody.create(MediaType.parse("application/json"),releaseBody.toString()))
                .build());

        Response r = call.execute();
        return r.isSuccessful();

    }

    private String getAuthHeader(){
        return "Basic "+ Base64.getEncoder().encodeToString((username + ":" + apiKey).getBytes());
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
