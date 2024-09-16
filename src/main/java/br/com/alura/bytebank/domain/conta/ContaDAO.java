package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.alura.bytebank.domain.cliente.Cliente;

public class ContaDAO {

	private Connection conn; //via injeção de dependência

	public ContaDAO(Connection conn) {
		this.conn = conn;
	}

	public void abrir(Conta conta, Cliente cliente) {

		String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email) VALUES (?, ?, ?, ?, ?)";

		try {

			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			preparedStatement.setInt(1, conta.getNumero());
			preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
			preparedStatement.setString(3, cliente.getNome());
			preparedStatement.setString(4, cliente.getCpf());
			preparedStatement.setString(5, cliente.getEmail());

			preparedStatement.execute();

		} catch (SQLException e) {
			// TODO: handle exception
		}
	}

}
