package br.com.alura.bytebank.db;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionFactory {
	
	private static int numeroConexoes = 0;
	
	public Connection recuperarConexao() {	
		try {
			//System.out.println("Número de conexões: " + ++numeroConexoes);
			return createDataSource().getConnection();
		}catch(SQLException e){
			throw new RuntimeException(e); //evita o retorno de null, por isso passa
		}
	}
	
	private HikariDataSource createDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
		config.setUsername("root");
		config.setPassword("rootroot");
		config.setMaximumPoolSize(10);
		return new HikariDataSource(config);
	}

}
