package banco;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

class Transacao extends Object {

    Transacao(int data, String descricao, double quantia) {
	this.data = data;
	this.descricao = descricao;
	this.quantia = quantia;
    }

    // Formato: ddmmyyyy
    public int data = (int) 0;
    public String descricao = null;
    public double quantia = (double) 0;

}

public class Conta {

    protected int conta;
    protected double saldo;
    protected double fundo_acoes;
    protected double poupanca;
    protected double renda_fixa;

    protected List<Transacao> transacoes;

    public Conta(int conta, double saldo) {
	transacoes = new Vector<Transacao>();
	this.conta = conta;
	this.saldo = saldo;
	this.novaTransacao("Nova Conta", saldo);
    }

    public double getSaldo() {
	return this.saldo;
    }

    public int getConta() {
	return this.conta;
    }

    public List<Transacao> getTransacoes() {
	this.novaTransacao("Extrato", 0);
	return this.transacoes;
    }

    public void saca(double valor) throws saldo_insuf {
	if (this.saldo < valor) {
	    throw new saldo_insuf("Saldo Insuficiente", valor);
	}
	this.saldo -= valor;
	this.novaTransacao("Saque", valor);
    }

    public void aplicaFundoAcoes(double valor) throws saldo_insuf {
	if (this.saldo < valor) {
	    throw new saldo_insuf("Saldo Insuficiente", valor);
	}
	this.fundo_acoes += valor;
	this.saldo -= valor;
	this.novaTransacao("AP Acoes", valor);
    }

    public void aplicaPoupanca(double valor) throws saldo_insuf {
	if (this.saldo < valor) {
	    throw new saldo_insuf("Saldo Insuficiente", valor);
	}
	this.poupanca += valor;
	this.saldo -= valor;
	this.novaTransacao("AP Poupanca", valor);
    }

    public void aplicaRendaFixa(double valor) throws saldo_insuf {
	if (this.saldo < valor) {
	    throw new saldo_insuf("Saldo Insuficiente", valor);
	}
	this.renda_fixa += valor;
	this.saldo -= valor;
	this.novaTransacao("AP RendaFixa", valor);
    }

    public void recebeTransferencia(Conta origem, double valor) {
	this.saldo += valor;
	novaTransacao("Transf Receb", valor);
    }

    public void fazTransferencia(Conta destino, double valor) throws saldo_insuf {
	if (this.saldo < valor) {
	    throw new saldo_insuf("Saldo Insuficiente", valor);
	}
	this.saldo -= valor;
	destino.recebeTransferencia(this, valor);
	novaTransacao("Transf Feita", valor);
    }

    public void novaTransacao(String descricao, double valor) {
	String data = String.valueOf(Calendar.DAY_OF_MONTH) + String.valueOf(Calendar.MONTH)
		+ String.valueOf(Calendar.YEAR);
	transacoes.add(new Transacao(Integer.parseInt(data), descricao, valor));
    }

}