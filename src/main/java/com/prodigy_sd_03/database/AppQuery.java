package com.prodigy_sd_03.database;

import com.prodigy_sd_03.entity.UserEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.prodigy_sd_03.UserController.showAlert;

/**
 *
 * @author kabiru
 */
public class AppQuery {

    private final DbConnection db = new DbConnection();

    public void addUser(UserEntity user) {
        try {
            db.getDBConn();
            PreparedStatement ps = DbConnection.getCon().prepareStatement("insert into USER(firstname,lastname, phoneNo, email)values(?,?,?,?)");
            ps.setString(1, user.getFirstname());
            ps.setString(2, user.getLastname());
            ps.setInt(3, user.getPhoneNo());
            ps.setString(4, user.getEmail());
            ps.execute();
            ps.close();
            DbConnection.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this instead
            showAlert("Database Error", "An error occurred while accessing the database.", e.getMessage());
        }
    }

    public ObservableList< UserEntity> getUSerList() {
        ObservableList<UserEntity> userList = FXCollections.observableArrayList();

        try {
            String query = "select id,firstname, lastname, phoneNo, email from USER order by lastname asc";
            db.getDBConn();
            Statement st = db.getCon().createStatement();
            ResultSet rs = st.executeQuery(query);
            UserEntity u;
            while (rs.next()) {
                u = new UserEntity(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"), rs.getInt("phoneNo"), rs.getString("email"));
                userList.add(u);
            }
            rs.close();
            st.close();
            DbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this instead
            showAlert("Database Error", "An error occurred while accessing the database.", e.getMessage());
        }
        return userList;
    }

    public void updateUser(UserEntity user) {
        try {
            db.getDBConn();
            PreparedStatement ps = DbConnection.getCon().prepareStatement("UPDATE`USER`\n"
                    + "SET\n"
                    + "`firstname` = ?,\n"
                    + "`lastname` = ?,\n"
                    + "`phoneNo` = ?,\n"
                    + "`email` = ?\n"
                    + "WHERE `id` = ?");
            ps.setString(1, user.getFirstname());
            ps.setString(2, user.getLastname());
            ps.setString(3, user.getPhoneNo().toString());
            ps.setString(4, user.getEmail());
            ps.setInt(5, user.getId());
            ps.execute();
            ps.close();
            DbConnection.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging this instead
            showAlert("Database Error", "An error occurred while accessing the database.", e.getMessage());
        }
    }

    public void deleteUser(UserEntity user) {
        try {
            db.getDBConn();
            PreparedStatement ps = DbConnection.getCon().prepareStatement("DELETE FROM `USER`\n"
                    + "WHERE id =?;");
            ps.setInt(1, user.getId());
            ps.execute();
            ps.close();
            DbConnection.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
