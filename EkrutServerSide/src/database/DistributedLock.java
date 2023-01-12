package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DistributedLock 
{
	    private Connection conn;
	    private String lockName;
	    private String owner;

	    public DistributedLock(Connection conn, String lockName) {
	        this.conn = conn;
	        this.lockName = lockName;
	    }

	    public boolean acquire() {
	        String sql = "INSERT INTO lock_table (lock_name, locked, owner) VALUES (?, 1, ?) "
	                   + "ON DUPLICATE KEY UPDATE locked = 1, owner = ?";
	        try (PreparedStatement statement = conn.prepareStatement(sql)) {
	            statement.setString(1, lockName);
	            statement.setString(2, owner);
	            statement.setString(3, owner);
	            int affectedRows = statement.executeUpdate();
	            return affectedRows > 0;
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    public boolean release() {
	        String sql = "UPDATE lock_table SET locked = 0, owner = '' WHERE lock_name = ? AND owner = ?";
	        try (PreparedStatement statement = conn.prepareStatement(sql)) {
	            statement.setString(1, lockName);
	            statement.setString(2, owner);
	            int affectedRows = statement.executeUpdate();
	            return affectedRows > 0;
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    public boolean isLocked() {
	        String sql = "SELECT locked FROM lock_table WHERE lock_name = ?";
	        try (PreparedStatement statement = conn.prepareStatement(sql)) {
	            statement.setString(1, lockName);
	            try (ResultSet rs = statement.executeQuery()) {
	                return rs.next() && rs.getBoolean("locked");
	            }
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	    }

	    public void setOwner(String owner) {
	        this.owner = owner;
	    }
}
