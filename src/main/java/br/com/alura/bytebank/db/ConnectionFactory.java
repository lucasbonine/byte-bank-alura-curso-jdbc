package br.com.alura.bytebank.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	private static int numeroConexoes = 0;
	
	public Connection recuperarConexao() {	
		try {
			System.out.println("Número de conexões: " + ++numeroConexoes);
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=rootroot");
		}catch(SQLException e){
			throw new RuntimeException(e); //evita o retorno de null, por isso passa
		}
	}
}
