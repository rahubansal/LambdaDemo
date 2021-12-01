import Config.DbConfig;
import Models.DigitalKeyApiRequest;
import Models.DigitalKeyApiResponse;
//import Models.Equipment;
//import Models.FrontendResponse;
//import com.amazonaws.services.lambda.AWSLambdaAsyncClient;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
import okhttp3.*;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;

// Handler value: example.Handler
public class Lambda2Handler implements RequestHandler<List<Integer>, String>{
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public String handleRequest(List<Integer> primarykeys, Context context)
    {
        LambdaLogger logger = context.getLogger();
        logger.log("CONTEXT: " + gson.toJson(context));


        return "done";
    }


    public static String getToken() throws SQLException {

        String tokenString;
        String SQL = "SELECT TOKEN_STRING FROM TOKEN_STORE";
        DbConfig dbConfig = new DbConfig();
        Connection conn = dbConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.executeQuery(SQL);
        ResultSet rs = pstmt.getResultSet();
        rs.next();
        tokenString = rs.getString(1);
        conn.close();
        return tokenString;

    }

    public static void httpCall(DigitalKeyApiRequest digitalKeyApiRequest) throws IOException, SQLException {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operatorId", digitalKeyApiRequest.getOperatorId());
        jsonObject.put("assetId", digitalKeyApiRequest.getAssetId());
        jsonObject.put("timePeriodStart", digitalKeyApiRequest.getTimePeriodStart());
        jsonObject.put("timePeriodEnd", digitalKeyApiRequest.getTimePeriodEnd());
        RequestBody body = RequestBody.create(JSON , jsonObject.toString());

        Request request = new Request.Builder()
                .url("https://dev.iris.trackunit.com/public/api/digital-key/keys")
                .addHeader("Authorization", "Bearer "+ getToken())
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        ResponseBody responseBody = response.body();
        System.out.println(responseBody.string());
        Gson gson = new Gson();
        if(response.code() == 200){
            DigitalKeyApiResponse entity = gson.fromJson(responseBody.string(), DigitalKeyApiResponse.class);
            System.out.println(entity.getId());
            System.out.println(entity.getAssetId());
            System.out.println(entity.getOperatorId());
            System.out.println(entity.getTimePeriodEnd());
            System.out.println(entity.getTimePeriodStart());
        }
        else if(response.code() == 403){
            System.out.println("token expired");
        }


    }

    public static void getRowsFromPrimaryKeys(List<Integer> primaryKeys) throws SQLException, IOException {
        String tokenString;
//        String SQL = "select OPERATOR_EMAIL , IC_NUMBER, ASSIGNMENT_START_DATE ,ASSIGNMENT_END_DATE from MAC_ASSIGNMENT_DETAIL where ASSIGNMENT_DETAIL_ID in (1,2,3,4);";
        String SQL2 = "select o.operator_id, meem.ASSET_ID, mad.ASSIGNMENT_START_DATE, mad.ASSIGNMENT_END_DATE " +
                "from MAC_ASSIGNMENT_DETAIL mad " +
                "left join OPERATOR o on mad.OPERATOR_EMAIL = o.email " +
                "left join MAC_ENABLED_EQUIPMENT_MASTER meem on meem.IC_NUMBER = mad.IC_NUMBER " +
                "where mad.ASSIGNMENT_DETAIL_ID in (1, 2, 3, 4);";
        DbConfig dbConfig = new DbConfig();
        Connection conn = dbConfig.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(SQL2);
        ResultSet rs = pstmt.executeQuery();
        DigitalKeyApiRequest digitalKeyApiRequest = new DigitalKeyApiRequest();
        while(rs.next()){
            digitalKeyApiRequest.setOperatorId(rs.getString(1));
            digitalKeyApiRequest.setAssetId(rs.getString(2));
            digitalKeyApiRequest.setTimePeriodStart(rs.getString(3));
            digitalKeyApiRequest.setTimePeriodEnd(rs.getString(4));
            httpCall(digitalKeyApiRequest);
        }
        conn.close();
    }

    public static void main(String[] args){


        DigitalKeyApiRequest digitalKeyApiRequest = new DigitalKeyApiRequest();
        List<Integer> primaryKeys = new ArrayList<>();
        primaryKeys.add(1);
        primaryKeys.add(2);
        primaryKeys.add(3);
        primaryKeys.add(4);

        try{
            getRowsFromPrimaryKeys(primaryKeys);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}