package br.com.alura.bytebank.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private static Connection conn;

	public static Connection recuperarConexao() {
		try {

			if (conn == null) {
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=rootroot");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
