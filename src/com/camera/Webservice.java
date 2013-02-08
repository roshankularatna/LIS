package com.camera;



import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class Webservice {
	public String METHOD_NAME = null;
	public String SOAP_ACTION ="http://calculator.me.org/"+METHOD_NAME;
	public String NAMESPACE ="http://calculator.me.org/";
	public static final String URL ="http://192.168.107.1:8080/WebApplication/CalculatorWSService?WSDL";
    
	public SoapObject getRequest(){
		return new SoapObject(NAMESPACE, METHOD_NAME);
	}
	public SoapSerializationEnvelope setRequestAndGetEnvilop(SoapObject request ) throws IOException, XmlPullParserException{
	
		MarshalBase64 marshal = new MarshalBase64();

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
   	    envelope.setOutputSoapObject(request);
   	    marshal.register(envelope);

   	    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
   	    androidHttpTransport.call(SOAP_ACTION,envelope);
   	    return envelope;
	
	}
	public SoapPrimitive getPrimitive(SoapSerializationEnvelope envelope) throws SoapFault{
		return (SoapPrimitive) envelope.getResponse();
	}
	public SoapObject getAll(SoapSerializationEnvelope envelope){
		return (SoapObject) envelope.bodyIn;
	}
}
