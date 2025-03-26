package vttp5a.final_project.utils;

import java.sql.Timestamp;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import vttp5a.final_project.models.Carpark;
import vttp5a.final_project.models.Parked;
import vttp5a.final_project.models.AppUser;

public class Utils {

    public static Carpark toCarpark(SqlRowSet rs) {
        Carpark c = new Carpark();

        c.setCarpark_id(rs.getString("carpark_id"));
        c.setCarpark_name(rs.getString("carpark_name"));
        c.setLatitude(rs.getDouble("latitude"));
        c.setLongitude(rs.getDouble("longitude"));
        c.setAgency(rs.getString("agency"));

        return c;
    }

    public static AppUser toUser(SqlRowSet rs) {
        AppUser u = new AppUser();

        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));

        return u;
    }

    public static Parked toParked (SqlRowSet rs) {
        Parked p = new Parked();

        p.setUsername(rs.getString("username"));
        p.setSession_id(rs.getString("session_id"));
        p.setCarpark_location(rs.getString("carpark_location"));
        p.setLevel(rs.getInt("level"));
        p.setDeck(rs.getString("deck"));

        Timestamp timestamp = rs.getTimestamp("park_start_time");
        long timestampLong = timestamp != null ? timestamp.getTime() : 0;
        p.setPark_start_time(timestampLong);

        p.setNotes(rs.getString("notes"));

        return p;
    }

}
