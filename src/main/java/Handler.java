import Config.DbConfig;
import Models.Equipment;
import Models.FrontendResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Handler value: example.Handler
public class Handler implements RequestHandler<Object, String>{
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public String handleRequest(Object event, Context context)
    {
        LambdaLogger logger = context.getLogger();

        String jsonString = gson.toJson(event);
        FrontendResponse frontendResponse = gson.fromJson(jsonString, FrontendResponse.class);

//        logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
//        logger.log("CONTEXT: " + gson.toJson(context));
//        logger.log("EVENT - gson.toJson: " + gson.toJson(event));
//        logger.log("EVENT" + event);
//        logger.log("EVENT tostring" + event.toString());

        try {
            DbConfig db = new DbConfig();
            Connection conn = db.getConnection();
            int assignmentId = updateMAC_ASSIGNMENT(conn, frontendResponse.getAssignmentName());
            ExecuteQueries(conn, assignmentId, frontendResponse);
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return "done";


    }

    public int updateMAC_ASSIGNMENT(Connection conn, String assignmentName) throws SQLException {
        String SQL = "INSERT INTO MAC_ASSIGNMENT(ASSIGNMENT_NAME) values(?)";
        PreparedStatement pstmt = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);
        pstmt.setString( 1, assignmentName );
        pstmt.executeUpdate();
        ResultSet rs = pstmt.getGeneratedKeys();
        rs.next();
        int assignmentId = rs.getInt(1);
        return assignmentId;
    }


    public void ExecuteQueries(Connection conn, int assignmentId, FrontendResponse frontendResponse) throws SQLException {

        String SQL2 = "INSERT INTO MAC_ASSIGNMENT_DETAIL (ASSIGNMENT_ID, ASSIGNMENT_NAME," +
                " ASSIGNMENT_START_DATE, ASSIGNMENT_END_DATE, ACCOUNT_NUMBER, ADMIN_EMAIL, " +
                "OPERATOR_EMAIL, IC_NUMBER, HERC_EQUIPMENT_NAME," +
                " CUSTOM_EQUIPMENT_NAME, CONTRACT_START_DATE, CONTRACT_END_DATE)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt2 = conn.prepareStatement(SQL2);

        conn.setAutoCommit(false);


        pstmt2.setInt(1, assignmentId);
        pstmt2.setString(2,frontendResponse.getAssignmentName());
        pstmt2.setString(3,frontendResponse.getAssignmentStartDate());
        pstmt2.setString(4,frontendResponse.getAssignmentEndDate());
        pstmt2.setString(5,frontendResponse.getAccountNumber());
        pstmt2.setString(6,frontendResponse.getAdminEmail());

        for (Equipment eq : frontendResponse.getEquipments()){
            for(String email : frontendResponse.getOperatorEmails()){
                pstmt2.setString(7,email);
                pstmt2.setString(8,eq.getIcNumber());
                pstmt2.setString(9,eq.getHercEquipmentName());
                pstmt2.setString(10,eq.getHercCustomName());
                pstmt2.setString(11,eq.getRmStartDate());
                pstmt2.setString(12,eq.getRmEndDate());
                pstmt2.addBatch();
            }
        }
        int[] count = pstmt2.executeBatch();
        conn.commit();
    }
}