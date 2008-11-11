package banco;

import java.util.Hashtable;
import java.util.Vector;

import org.omg.CORBA.DoubleHolder;

class auto_atendimentoImpl extends auto_atendimentoPOA {

    private Hashtable<String, Conta> contas;

    public auto_atendimentoImpl() {
	contas = new Hashtable<String, Conta>();
	contas.put(Integer.toString(100100), new Conta(100100, 50000.00));
	contas.put(Integer.toString(200200), new Conta(200200, 5.00));
	contas.put(Integer.toString(300300), new Conta(300300, 200.00));
	contas.put(Integer.toString(400400), new Conta(400400, 1.99));
	contas.put(Integer.toString(500500), new Conta(500500, 25.00));
    }

    public String boas_vindas() {
	return "Bem-vindo ao banco " + banco.nome_banco.value;
    }

    public void extrato(int c, transacoesHolder t, DoubleHolder saldo) throws conta_inval {

	Vector<Transacao> transacoes = (Vector<Transacao>) this.getConta(c).getTransacoes();

	t.value = new transacao[transacoes.size()];
	for (int i = 0; i < transacoes.size(); ++i) {
	    Transacao tmp = transacoes.get(i);
	    t.value[i] = new transacao(tmp.data, tmp.descricao, tmp.quantia);
	}

	saldo.value = this.getConta(c).getSaldo();
    }

    public void investimento(int c, aplicacao apl, double v) throws conta_inval, saldo_insuf {
	Conta conta = this.getConta(c);
	switch (apl.value()) {
	case banco.aplicacao._fundo_acoes:
	    conta.aplicaFundoAcoes(v);
	    break;
	case banco.aplicacao._poupanca:
	    conta.aplicaPoupanca(v);
	    break;
	case banco.aplicacao._renda_fixa:
	    conta.aplicaRendaFixa(v);
	    break;
	default:
	    break;
	}
    }

    public double saldo(int c) throws conta_inval {
	return this.getConta(c).getSaldo();
    }

    public void tranferencia(int origem, int destino, double v) throws conta_inval, saldo_insuf {
	this.getConta(origem).fazTransferencia(this.getConta(destino), v);
    }

    protected Conta getConta(int conta) throws conta_inval {
	String conta_chave = Integer.toString(conta);

	if (!this.contas.containsKey(conta_chave)) {
	    System.out.println("Atencao: Conta " + conta + " nao encontrada!");
	    throw new conta_inval("Conta invalida", conta);
	}
	return this.contas.get(conta_chave);
    }

}