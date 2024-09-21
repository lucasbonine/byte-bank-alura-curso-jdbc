package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.db.ConnectionFactory;
import br.com.alura.bytebank.domain.RegraDeNegocioException;
import br.com.alura.bytebank.domain.cliente.Cliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaService {
	
	ConnectionFactory conn;
	
	public ContaService() {
		conn = new ConnectionFactory();
	}

    private Set<Conta> contas = new HashSet<>();

    public Set<Conta> listarContasAbertas() {
    		return new ContaDAO(conn.recuperarConexao()).listarContasAbertas();
    }

    public BigDecimal consultarSaldo(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        return conta.getSaldo();
    }

    public void abrir(DadosAberturaConta dadosDaConta) {
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente);
        if (contas.contains(conta)) {
            throw new RegraDeNegocioException("Já existe outra conta aberta com o mesmo número!");
        }
        
        
        ContaDAO contaDAO = new ContaDAO(conn.recuperarConexao()); //injeção de dependência
        contaDAO.abrir(conta, cliente);
    }

    public void realizarSaque(Integer numeroDaConta, BigDecimal valor) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do saque deve ser superior a zero!");
        }

        if (valor.compareTo(conta.getSaldo()) > 0) {
            throw new RegraDeNegocioException("Saldo insuficiente!");
        }

        conta.sacar(valor);
    }

    public void realizarDeposito(Integer numeroDaConta, BigDecimal valor) {
        Conta conta = buscarContaPorNumero(numeroDaConta);
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("Valor do deposito deve ser superior a zero!");
        }

        ContaDAO contaDAO = new ContaDAO(conn.recuperarConexao()); //injeção de dependência
        BigDecimal novoValorSaldoConta = conta.getSaldo().add(valor);
        contaDAO.alterarSaldoConta(conta.getNumero(), novoValorSaldoConta);
    }

    public void encerrar(Integer numeroDaConta) {
        var conta = buscarContaPorNumero(numeroDaConta);
        if (conta.possuiSaldo()) {
            throw new RegraDeNegocioException("Conta não pode ser encerrada pois ainda possui saldo!");
        }

        contas.remove(conta);
    }
    
    public Conta buscarContaPorNumero(Integer numero) {
    	ContaDAO contaDAO = new ContaDAO(conn.recuperarConexao()); //injeção de dependência
        Conta conta = contaDAO.listarContaPorNumeroCliente(numero);
        if(conta != null)
        	return conta;
        else
        	throw new RegraDeNegocioException("Conta não localizada.");
    }
}
