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
public class SqlCarparkRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_SELECT_CARPARKS_BY_ID = "select * from carparks where carpark_id = ?";
    private static final String SQL_SELECT_CARPARKS = "select * from carparks";
    private static final String SQL_SELECT_CARPARKS_BY_AGENCY = "select * from carparks where agency = ?";
    private static final String SQL_SELECT_CARPARKS_BY_TERM = "select * from carparks where carpark_id like ? or carpark_name like ?";
    private static final String SQL_SELECT_CARPARKS_BY_ID_OR_NAME = "select * from carparks where (carpark_id like ? or carpark_name like ?) and agency like ?";
    private static final String SQL_ADD_CARPARKS = 
    """
    insert into carparks (carpark_id, carpark_name, latitude, longitude, agency) values (?, ?, ?, ?, ?) 
        on duplicate key update carpark_name = values(carpark_name), latitude = values(latitude), longitude = values(longitude), agency = values(agency) 
    """;

    public int insertCarpark(Carpark carpark) {
        int added = jdbcTemplate.update(SQL_ADD_CARPARKS, carpark.getCarpark_id(), carpark.getCarpark_name(), carpark.getLatitude(), carpark.getLongitude(), carpark.getAgency());
        return added;
    }   

    public List<Carpark> getAllCarparks() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_CARPARKS);
        List<Carpark> carparks = new ArrayList<>();
        while (rs.next()) {
            carparks.add(Utils.toCarpark(rs));
        }
        return carparks;
    }

    public List<Carpark> getCarparksByAgency(String agency) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_CARPARKS_BY_AGENCY, agency);
        List<Carpark> carparks = new ArrayList<>();
        while (rs.next()) {
            carparks.add(Utils.toCarpark(rs));
        }
        return carparks;
    }

    public Carpark getCarparkById(String id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_CARPARKS_BY_ID, id);
        if (rs.next()) {
            Carpark c = Utils.toCarpark(rs);
            return c;
        } else {
            throw new RuntimeException("Carpark not found!");
        }
    }

    public List<Carpark> searchCarparksByTerm(String searchTerm) {
        String searchPattern = "%" + searchTerm + "%";

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_CARPARKS_BY_TERM, searchPattern, searchPattern);
        List<Carpark> carparks = new ArrayList<>();
        while (rs.next()) {
            carparks.add(Utils.toCarpark(rs));
        }
        return carparks;
    }

    public List<Carpark> searchCarparks(String searchTerm, String agency) {
        String searchPattern = "%" + searchTerm + "%";

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_CARPARKS_BY_ID_OR_NAME, searchPattern, searchPattern, "%" + agency + "%");
        List<Carpark> carparks = new ArrayList<>();
        while (rs.next()) {
            carparks.add(Utils.toCarpark(rs));
        }
        return carparks;
    }
}
