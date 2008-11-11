package cliente;

import java.util.Properties;

import org.omg.CORBA.*;
import org.omg.CosNaming.*;

import banco.aplicacao;
import banco.caixa_eletronico;
import banco.caixa_eletronicoHelper;
import banco.conta_inval;
import banco.saldo_insuf;
import banco.transacoesHolder;

public class Cliente {

    private static java.io.BufferedReader reader;
    private static caixa_eletronico server;
    private static int conta;

    public static final int SALDO = 1;
    public static final int EXTRATO = 2;
    public static final int SAQUE = 3;
    public static final int TRANSFERENCIA = 4;
    public static final int INVESTIMENTO = 5;
    public static final int SAIR = 6;

    public static final int POUPANCA = 1;
    public static final int FUNDO_ACOES = 2;
    public static final int RENDA_FIXA = 3;
    public static final int CANCELAR = 4;

    public static void main(String args[]) {

	if (args.length != 2) {
	    System.err.println("USO: cliente.Cliente host porta");
	    return;
	}

	reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

	try {
	    Properties orbProps = new Properties();
	    orbProps.put("org.omg.CORBA.ORBInitialHost", args[0]);
	    orbProps.put("org.omg.CORBA.ORBInitialPort", args[1]);

	    ORB orb = ORB.init(args, orbProps);

	    org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    String name = "CaixaEletronico";
	    server = caixa_eletronicoHelper.narrow(ncRef.resolve_str(name));

	    System.out.println(server.boas_vindas());

	    while (true) {
		System.out.print("Informe o numero da sua conta: ");
		String entrada = reader.readLine();

		if (entrada.isEmpty()) {
		    System.err.println("Erro: Numero de Conta não especificado.");
		    continue;
		}

		conta = Integer.parseInt(entrada);

		if (conta <= 0) {
		    System.out.println("Codigo de controle informado.");
		    break;
		}

		int opcao;
		do {
		    System.out.println();
		    System.out.println();
		    System.out.println("------ Opcoes Disponiveis --------");
		    System.out.println(Integer.toString(SALDO) + ": Consulta de saldo.");
		    System.out.println(Integer.toString(EXTRATO) + ": Consulta de extrato.");
		    System.out.println(Integer.toString(SAQUE) + ": Operacao de saque.");
		    System.out.println(Integer.toString(TRANSFERENCIA) + ": Operacao de transferencia.");
		    System.out.println(Integer.toString(INVESTIMENTO) + ": Investimentos.");
		    System.out.println(Integer.toString(SAIR) + ": Sair.");
		    System.out.println("----------------------------------");
		    System.out.println();

		    System.out.print("Informe a opcao desejada: ");
		    entrada = reader.readLine();

		    entrada = (entrada.isEmpty()) ? "0" : entrada;
		    opcao = Integer.parseInt(entrada);

		    switch (opcao) {
		    case SALDO:
			saldo();
			break;
		    case EXTRATO:
			extrato();
			break;
		    case SAQUE:
			saque();
			break;
		    case TRANSFERENCIA:
			transferencia();
			break;
		    case INVESTIMENTO:
			investimento();
			break;
		    case SAIR:
			break;
		    default:
			System.err.println("Opcao invalida: Informe uma opcao valida.");
		    }
		} while (opcao != SAIR);
	    }
	} catch (Exception e) {
	    System.err.println("ERRO: " + e);
	    e.printStackTrace(System.err);
	} finally {
	    System.out.println("Finalizando aplicacao cliente");
	}
    }

    private static void saldo() {
	try {
	    System.out.println("Saldo atual: R$ " + server.saldo(conta));
	} catch (conta_inval e) {
	    System.err.println("Erro: Numero de Conta invalido.");
	    return;
	}
    }

    private static void extrato() {
	transacoesHolder transacoes = new transacoesHolder();
	DoubleHolder saldo = new DoubleHolder(0);

	try {
	    server.extrato(conta, transacoes, saldo);
	} catch (conta_inval e) {
	    System.err.println("Erro: Numero de Conta invalido.");
	    return;
	}

	System.out.println("-------- Extrato --------");

	for (int c = 0; c < transacoes.value.length; c++) {
	    System.out.println(transacoes.value[c].data + " - " + transacoes.value[c].descricao + " - R$ "
		    + transacoes.value[c].quantia);
	}

	System.out.println("-------------------------");
	System.out.println("Saldo atual: R$ " + saldo.value);
    }

    private static void saque() throws java.io.IOException {
	System.out.print("Informe o valor do saque: R$ ");
	double valor = Double.parseDouble(reader.readLine());

	try {
	    server.saque(conta, valor);
	} catch (conta_inval e) {
	    System.err.println("Erro: Numero de Conta invalido.");
	    return;
	} catch (saldo_insuf e) {
	    System.err.println("Erro: Saldo insuficiente.");
	    return;
	}

	System.out.println("Saque efetuado. aguarde, contanto notas... $$$!");
    }

    private static void transferencia() throws java.io.IOException {
	System.out.print("Informe o numero da Conta de Destino: ");
	int destino = Integer.parseInt(reader.readLine());

	System.out.print("Informe o Valor de Transferencia: R$ ");
	double valor = Double.parseDouble(reader.readLine());

	try {
	    server.tranferencia(conta, destino, valor);
	} catch (conta_inval e) {
	    System.err.println("Erro: Numero de Conta invalido.");
	    return;
	} catch (saldo_insuf e) {
	    System.err.println("Erro: Saldo insuficiente.");
	    return;
	}

	System.out.println("Transferencia bem sucedida.");
    }

    private static void investimento() throws java.io.IOException {
	double valor;

	System.out.println("-------- Opcoes de Investimento --------");
	System.out.println(Integer.toString(POUPANCA) + ": Fundo de Poupanca.");
	System.out.println(Integer.toString(FUNDO_ACOES) + ": Fundo de Acoes.");
	System.out.println(Integer.toString(RENDA_FIXA) + ": Fundo de Renda Fixa.");
	System.out.println(Integer.toString(CANCELAR) + ": Cancelar Investimento.");
	System.out.println("----------------------------------------");
	System.out.print("Informe a opcao de investimento desejado: ");
	int investimento = Integer.parseInt(reader.readLine());

	try {
	    switch (investimento) {
	    case POUPANCA:
		System.out.print("Informe o valor de investimento na poupanca: R$ ");
		valor = Double.parseDouble(reader.readLine());
		server.investimento(conta, aplicacao.poupanca, valor);
		break;
	    case FUNDO_ACOES:
		System.out.print("Informe o valor de investimento no fundo de acoes: R$ ");
		valor = Double.parseDouble(reader.readLine());
		server.investimento(conta, aplicacao.fundo_acoes, valor);
		break;
	    case RENDA_FIXA:
		System.out.print("Informe o valor de investimento em renda fixa: R$ ");
		valor = Double.parseDouble(reader.readLine());
		server.investimento(conta, aplicacao.renda_fixa, valor);
		break;
	    case CANCELAR:
		return;
	    default:
		System.err.println("Opcao de investimento invalida: Informe uma opcao valida.");
		return;
	    }
	} catch (conta_inval e) {
	    System.err.println("Erro: Numero de Conta invalido.");
	    return;
	} catch (saldo_insuf e) {
	    System.err.println("Erro: Saldo insuficiente.");
	    return;
	}

	System.out.println("Investimento efetuado com sucesso.");
    }
}
