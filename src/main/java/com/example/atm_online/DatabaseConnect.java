package com.example.atm_online;

import java.sql.*;

public class DatabaseConnect {
    public Connection connection;
    private Statement statement;

    public DatabaseConnect(String url) throws SQLException {
        connection = DriverManager.getConnection(url, "root", "1234");
        statement = connection.createStatement();
    }

    public void insert(String firstname, String lastname, String gender, String username, String password) throws SQLException {
        String querry = "INSERT INTO customers VALUES(" + "'" + firstname + "'," + "'"+ lastname + "',"
                        + "'"+ gender + "'," + "'" + username + "'," + "'" + password + "'," + "'0')";
        statement.executeUpdate(querry);

    }

    public boolean validAccount(String user, String pass) throws SQLException {
        String querry = "SELECT username, password FROM customers" + " WHERE username = '" + user + "' and password ='" + pass + "'";
        ResultSet result = statement.executeQuery(querry);
        return result.next();
    }

    public boolean existUsername(String user) throws SQLException {
        String querry = "SELECT username FROM customers WHERE username = " + "'" + user + "'";
        ResultSet result = statement.executeQuery(querry);
        return result.next();
    }

    public String getFirstname(String user) throws SQLException {
        String firstn = "";
        String querry = "SELECT * FROM customers WHERE username = " + "'" + user + "'";
        ResultSet result = statement.executeQuery(querry);
        while (result.next()) {
            firstn = result.getString("firstname");
        }
        return firstn;
    }
    public String getLastname(String user) throws SQLException {
        String lastn = "";
        String querry = "SELECT * FROM customers WHERE username = " + "'" + user + "'";
        ResultSet result = statement.executeQuery(querry);
        while (result.next()) {
            lastn = result.getString("lastname");
        }
        return lastn;
    }
    public String getGender(String user) throws SQLException {
        String gend = "";
        String querry = "SELECT * FROM customers WHERE username = " + "'" + user + "'";
        ResultSet result = statement.executeQuery(querry);
        while (result.next()) {
            gend = result.getString("gender");
        }
        return gend;
    }
    public Double getBalance(String user) throws SQLException {
        Double bal = 0.0;
        String querry = "SELECT balance FROM customers WHERE username = " + "'" + user + "'";
        ResultSet result = statement.executeQuery(querry);
        while (result.next()) {
            bal = result.getDouble("balance");
        }
        return bal;
    }

    public void setBalance(String user, Double amount) throws SQLException {
        String querry =  "UPDATE customers SET balance = " + amount + " WHERE username = " + "'" + user + "'";
        statement.executeUpdate(querry);
    }
}


