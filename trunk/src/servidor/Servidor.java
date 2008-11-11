package servidor;

import java.util.Properties;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

import banco.caixa_eletronico;
import banco.caixa_eletronicoHelper;
import banco.caixa_eletronicoImpl;

public class Servidor {

    public static void main(String args[]) {

	if (args.length != 2) {
	    System.err.println("USO: servidor.Servidor host porta");
	    return;
	}

	try {
	    Properties orbProps = new Properties();
	    orbProps.put("org.omg.CORBA.ORBInitialHost", args[0]);
	    orbProps.put("org.omg.CORBA.ORBInitialPort", args[1]);

	    ORB orb = ORB.init(args, orbProps);

	    caixa_eletronicoImpl impl = new caixa_eletronicoImpl();

	    POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	    rootpoa.the_POAManager().activate();

	    org.omg.CORBA.Object ref = rootpoa.servant_to_reference(impl);
	    caixa_eletronico href = caixa_eletronicoHelper.narrow(ref);

	    org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

	    String name = "CaixaEletronico";
	    NameComponent path[] = ncRef.to_name(name);
	    ncRef.rebind(path, href);

	    System.out.println("Servidor aguardando requisicoes ....");

	    orb.run();
	} catch (Exception e) {
	    System.err.println("ERRO: " + e);
	    e.printStackTrace(System.err);
	}
	System.out.println("Finalizando o Servidor.");
    }
}
