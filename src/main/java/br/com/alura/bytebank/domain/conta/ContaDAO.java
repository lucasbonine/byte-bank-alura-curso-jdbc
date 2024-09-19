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

	private Connection conn; // via injeção de dependência

	public ContaDAO(Connection conn) {
		this.conn = conn;
	}

	public void abrir(Conta conta, Cliente cliente) {
		String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, conta.getNumero());
			ps.setBigDecimal(2, BigDecimal.ZERO);
			ps.setString(3, cliente.getNome());
			ps.setString(4, cliente.getCpf());
			ps.setString(5, cliente.getEmail());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RegraDeNegocioException("Ocorreu um erro de SQL, favor verificar");
		}finally {
			try {
				if(ps != null)
					ps.close();
				if(conn != null)
					conn.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Set<Conta> listarContasAbertas() {
		Set<Conta> contas = new HashSet<Conta>();
		String sql = "SELECT * FROM conta";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null)
					ps.close();
				if(rs != null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return contas;
	}

}
