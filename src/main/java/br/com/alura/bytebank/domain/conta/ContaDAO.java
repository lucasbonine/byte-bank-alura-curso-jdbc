package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

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
			e.printStackTrace();
			throw new RegraDeNegocioException("Ocorreu um erro de SQL, favor verificar");
		}
	}
	
	public Set<Conta> listarContasAbertas(){
		
		Set<Conta> contas = new HashSet<Conta>();
		
		String sql = "SELECT * FROM conta";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Integer numero = rs.getInt(1);
				BigDecimal saldo = rs.getBigDecimal(2);
				String nome = rs.getString(3);
				String cpf = rs.getString(4);
				String email = rs.getString(5);
				
				DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);
				Cliente cliente = new Cliente(dadosCadastroCliente);
				Conta conta = new Conta(numero, cliente);
				conta.depositar(saldo);
				
				contas.add(conta);
			}
			
			
		}catch (SQLException e) {
			// TODO: handle exception
		}
		
		return contas;
		
	}

}
