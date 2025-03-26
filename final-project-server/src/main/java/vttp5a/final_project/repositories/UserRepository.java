package vttp5a.final_project.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp5a.final_project.models.AppUser;
import vttp5a.final_project.utils.Utils;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_SELECT_USER_BY_ID = "select * from users where username = ?";
    private static final String SQL_ADD_USER = "insert into users (username, email, password, role) values (?, ?, ?, ?)";

    public Optional<AppUser> findUserById(String id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SELECT_USER_BY_ID, id);
        while (rs.next()) {
            AppUser user = Utils.toUser(rs);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public int saveUser(AppUser user) {
        int added = jdbcTemplate.update(SQL_ADD_USER, user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
        return added;
    }
}
