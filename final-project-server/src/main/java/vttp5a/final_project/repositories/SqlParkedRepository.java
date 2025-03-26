package vttp5a.final_project.repositories;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp5a.final_project.models.Parked;
import vttp5a.final_project.utils.Utils;

@Repository
public class SqlParkedRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_PARKING_SESSION = "insert into user_parking_sessions (session_id, username) values (?, ?)";

    private static final String SQL_ADD_PARKING_DETAILS = "insert into parking_details (session_id, carpark_location, level, deck, park_start_time, notes) values (?, ?, ?, ?, ?, ?)";

    // private static final String SQL_ADD_PARKING_DETAILS_NO_TIME = "insert into parking_details (session_id, carpark_location, level, deck, notes) values (?, ?, ?, ?, ?)";

    // private static final String SQL_GET_PARKING_SESSIONS = "select * from user_parking_sessions where username = ?";

    // private static final String SQL_GET_PARKING_DETAILS = "select * from parking_details where session_id = ?";

    private static final String SQL_GET_PARKING_DETAILS = 
        """
        select * from user_parking_sessions ups 
        join parking_details pd 
        on ups.session_id = pd.session_id 
        where ups.username = ?
        """;

    private static final String SQL_DELETE_PARKING_DETAILS = "delete from parking_details where session_id = ?";

    private static final String SQL_DELETE_PARKING_SESSION = "delete from user_parking_sessions where session_id = ?";

    public Boolean addParkingSession(Parked parked) {
        int added = jdbcTemplate.update(SQL_ADD_PARKING_SESSION, parked.getSession_id(), parked.getUsername());
        return added > 0;
    }

    public Boolean addParkingDetails(Parked parked) {
        long timestampLong = parked.getPark_start_time();
        Timestamp timestamp = new Timestamp(timestampLong);
        int added = jdbcTemplate.update(SQL_ADD_PARKING_DETAILS, parked.getSession_id(), parked.getCarpark_location(), parked.getLevel(), parked.getDeck(), timestamp, parked.getNotes());
        return added > 0;
    }

    public List<Parked> getParkingDetailsByUser(String username) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_PARKING_DETAILS, username);
        List<Parked> details = new ArrayList<>();
        while (rs.next()) {
            details.add(Utils.toParked(rs));
        }
        return details;
    }

    public Boolean removeParkingSession(String sessionId) {
        int deleted = jdbcTemplate.update(SQL_DELETE_PARKING_SESSION, sessionId);
        return deleted > 0;
    }

    public Boolean removeParkingDetails(String sessionId) {
        int deleted = jdbcTemplate.update(SQL_DELETE_PARKING_DETAILS, sessionId);
        return deleted > 0;
    }

}
