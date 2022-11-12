package com.increff.Assure.util;

import com.increff.Assure.model.data.InvoiceData;
import org.apache.fop.apps.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class InvoiceUtil
{
    private static final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

    public static String jaxbObjectToXML(InvoiceData invoiceData)
    {
        try{

            JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceData.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(invoiceData,sw);
            String xmlContent = sw.toString();
            return xmlContent;
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static void convertToPDF(InvoiceData invoiceData, File xslt, File pdf , String xml)throws IOException, TransformerException
    {
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        OutputStream out = new java.io.FileOutputStream(pdf);
        out = new java.io.BufferedOutputStream(out);
        try{
            Fop fop = null;
            try{
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent,out);
            }catch (FOPException e)
            {
                throw new RuntimeException(e);
            }

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslt));

            Source src = new StreamSource(new StringReader(xml));

            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src,res);
        }catch (FOPException e)
        {
            throw new RuntimeException(e);
        }
        finally {
            out.close();
        }

    }
}
