import Config.DbConfig;
import Models.Equipment;
import Models.FrontendResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Handler value: example.Handler
public class Lambda2Handler implements RequestHandler<List<Integer>, String>{
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public String handleRequest(List<Integer> primarykeys, Context context)
    {
        LambdaLogger logger = context.getLogger();




//        logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
//        logger.log("CONTEXT: " + gson.toJson(context));
//        logger.log("EVENT - gson.toJson: " + gson.toJson(event));
//        logger.log("EVENT" + event);
//        logger.log("EVENT tostring" + event.toString());


        return "done";


    }


    public static void httpCall() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://dev.iris.trackunit.com/public/api/digital-key/keys")
                .addHeader("Authorization", "Bearer eyJraWQiOiI4SDliSU5waUZlcG13SEdqLURiQzBVdDRWNGx0b1NGZFNzZ3RCTHVMdk9nIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULk5NOVVabHpBNWZHYmk3aFgtQVlfNkUwME52b29mTFh4VUZJbkY4emJONWciLCJpc3MiOiJodHRwczovL3RyYWNrdW5pdC1wb3J0YWwub2t0YS5jb20vb2F1dGgyL2F1czMxeGVnN2UwRjdhZWVRMzU3IiwiYXVkIjoiYXBpIiwiaWF0IjoxNjM4MzM2ODUzLCJleHAiOjE2MzgzNDA0NTMsImNpZCI6IjBvYWRpdmlvdjF2a0xnWmRrMzU3IiwidWlkIjoiMDB1ZGl2cDFhdXlNNnM1U3AzNTciLCJzY3AiOlsiYXBpIl0sInN1YiI6IjBvYWRpdmlvdjF2a0xnWmRrMzU3IiwiYWNjb3VudElkIjoiMmJjZDgzNzAtMDM0OS00NzU4LWFlNjItNTQ5YjczYWNhNzljIiwiY3VzdG9tZXJJZCI6NDMyMTMsInVzZXJJZCI6MCwidGFzVXNlcklkIjoiODlhODg3Y2QtNDQ4Ny0zODgzLWJmYjctZDI1ZGFlM2I5MWViIn0.TOysd6fNAZ9fN5Nsb0XWO6Io8ZL4Rysn899rNZNRHUmW8QvIo9cxv9ZUhX13Ys66seO3dweu_pMeLmVz1XMf9_OzzODIhC1lQIGEfXxGQ7jjzEe0wTdGCZmjxnwFlJ-32kwoVVw0VmajVnI5WVWDmXl1QjS3UqvWM12dgYos-lPW42B5QPpyMD858vdHLoyeSMNiXJFNqNqGXeLkVfeb16B6eWf0wRYaiLSF2vN6M7rQevC6KvOIXy6CBiX5Wy3zCBvUgNl0DpuZWyuG3x51DluaXucfL-j-B8pKnhgbz21N3G6KaoPr66YNiSdW3ZbTznIMVRQaQcgsSRuTXNJNiw")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        System.out.println(response);
//        assertThat(response.code(), equalTo(200));
    }

    public static void main(String[] args){
        try{
            httpCall();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


}