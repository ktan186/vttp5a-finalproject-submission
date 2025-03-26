package vttp5a.final_project.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp5a.final_project.models.Carpark;
import vttp5a.final_project.utils.Utils;

@Repository
public class SqlFavouriteCarparkRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_FAV_CARPARKS = "insert into favourite_carparks (username, carpark_id) values (?, ?)";

    private static final String SQL_GET_FAV_CARPARKS_BY_USER = 
        """
        select c.* 
        from favourite_carparks as fc 
        join carparks as c
        on fc.carpark_id = c.carpark_id 
        where username = ?
        """;

    private static final String SQL_DELETE_FAV_CARPARKS_FROM_USER = "delete from favourite_carparks where username = ? and carpark_id = ?";

    public Boolean addFavouriteCarpark(String username, String carparkId) {
        int added = jdbcTemplate.update(SQL_ADD_FAV_CARPARKS, username, carparkId);
        return added > 0;
    }

    public List<Carpark> getFavouritesByUser(String username) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_FAV_CARPARKS_BY_USER, username);
        List<Carpark> carparks = new ArrayList<>();
        while (rs.next()) {
            carparks.add(Utils.toCarpark(rs));
        }
        return carparks;
    }

    public Boolean removeFavouriteCarpark(String username, String carparkId) {
        int deleted = jdbcTemplate.update(SQL_DELETE_FAV_CARPARKS_FROM_USER, username, carparkId);
        return deleted > 0;
    }
}
