package com.divirad.discordbot.achievement.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.divirad.discordbot.achievement.bot.Constants;

public class Database {
	
	private final static String HOSTNAME = Constants.DATABASE_HOST;
	private final static String DATABASE = Constants.DATABASE_NAME;
	private final static String USERNAME = Constants.DATABASE_USERNAME;
	private final static String PASSWORD = Constants.DATABASE_PASSWORD;
	private final static String PARAMS	 = "useLegacyDatetimeCode=false&serverTimezone=UTC";
	//private final static String PARAMS	 = "";
	
    public interface ISetParams {
        void run(PreparedStatement ps) throws SQLException;
    }
    public interface IUseResultSet <T> {
        T run(ResultSet ps) throws SQLException;
    }

    /**
     * Executes a sql query with no parameter
     * @param sql prepared sql string
     * @param useResultSet function using the ResultSet to create the return value
     * @param <T> return Type
     * @return the return value of useResultSet
     */
    public static <T> T query(String sql, IUseResultSet<T> useResultSet) {
        return query(sql, ps -> {}, useResultSet);
    }

    /**
     * Executes a sql query
     * @param sql prepared sql string
     * @param setParams function to set the parameters of the PreparedStatement
     * @param useResultSet function using the ResultSet to create the return value
     * @param <T> return Type
     * @return the return value of useResultSet
     */
    public static <T> T query(String sql, ISetParams setParams, IUseResultSet<T> useResultSet) {
    	try (Connection con = DriverManager.getConnection("jdbc:mysql://" + HOSTNAME + "/" + DATABASE + "?" + PARAMS, USERNAME, PASSWORD)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                setParams.run(ps);
                try (ResultSet rs = ps.executeQuery()) {
                    return useResultSet.run(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a sql command with no parameter
     * @param sql prepared sql string
     * 
     * @return count of rows affected
     */
    public static int execute(String sql) {
        return execute(sql, ps -> {});
    }

    /**
     * Executes a sql command
     * @param sql prepared sql string
     * @param setParams function to set the parameters of the PreparedStatement
     * 
     * @return count of rows affected
     */
    public static int execute(String sql, ISetParams setParams) {
    	int updateCount = -1;
    	try (Connection con = DriverManager.getConnection("jdbc:mysql://" + HOSTNAME + "/" + DATABASE, USERNAME, PASSWORD)) {
        		try (PreparedStatement ps = con.prepareStatement(sql)) {
                setParams.run(ps);
                ps.execute();
                updateCount = ps.getUpdateCount();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	return updateCount;
        }
    }

    public static int getLastID() {
        return query("SELECT LAST_INSERT_ID();", rs -> rs != null && rs.next() ? rs.getInt(1) : null);
    }
}