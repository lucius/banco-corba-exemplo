package banco;

/**
 * banco/caixa_eletronicoHelper.java . Generated by the IDL-to-Java compiler
 * (portable), version "3.2" from Banco.idl Thursday, October 9, 2008 3:43:50 PM
 * BRT
 */

abstract public class caixa_eletronicoHelper {
    private static String _id = "IDL:banco/caixa_eletronico:1.0";

    public static void insert(org.omg.CORBA.Any a, banco.caixa_eletronico that) {
	org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
	a.type(type());
	write(out, that);
	a.read_value(out.create_input_stream(), type());
    }

    public static banco.caixa_eletronico extract(org.omg.CORBA.Any a) {
	return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    synchronized public static org.omg.CORBA.TypeCode type() {
	if (__typeCode == null) {
	    __typeCode = org.omg.CORBA.ORB.init().create_interface_tc(banco.caixa_eletronicoHelper.id(),
		    "caixa_eletronico");
	}
	return __typeCode;
    }

    public static String id() {
	return _id;
    }

    public static banco.caixa_eletronico read(org.omg.CORBA.portable.InputStream istream) {
	return narrow(istream.read_Object(_caixa_eletronicoStub.class));
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, banco.caixa_eletronico value) {
	ostream.write_Object((org.omg.CORBA.Object) value);
    }

    public static banco.caixa_eletronico narrow(org.omg.CORBA.Object obj) {
	if (obj == null)
	    return null;
	else if (obj instanceof banco.caixa_eletronico)
	    return (banco.caixa_eletronico) obj;
	else if (!obj._is_a(id()))
	    throw new org.omg.CORBA.BAD_PARAM();
	else {
	    org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
	    banco._caixa_eletronicoStub stub = new banco._caixa_eletronicoStub();
	    stub._set_delegate(delegate);
	    return stub;
	}
    }

    public static banco.caixa_eletronico unchecked_narrow(org.omg.CORBA.Object obj) {
	if (obj == null)
	    return null;
	else if (obj instanceof banco.caixa_eletronico)
	    return (banco.caixa_eletronico) obj;
	else {
	    org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
	    banco._caixa_eletronicoStub stub = new banco._caixa_eletronicoStub();
	    stub._set_delegate(delegate);
	    return stub;
	}
    }

}
