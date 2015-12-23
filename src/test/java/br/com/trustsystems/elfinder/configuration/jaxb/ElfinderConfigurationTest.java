/*
 * #%L
 * %%
 * Copyright (C) 2015 Trustsystems Desenvolvimento de Sistemas, LTDA.
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Trustsystems Desenvolvimento de Sistemas, LTDA. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package br.com.trustsystems.elfinder.configuration.jaxb;

import br.com.trustsystems.elfinder.configuration.jaxb.ElfinderConfiguration.Thumbnail;
import br.com.trustsystems.elfinder.configuration.jaxb.ElfinderConfiguration.Volume;
import br.com.trustsystems.elfinder.configuration.jaxb.ElfinderConfiguration.Volume.Constraint;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class ElfinderConfigurationTest {

    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    /**
     * Inits the configuration
     *
     * @throws JAXBException
     * @throws SAXException
     * @throws java.io.IOException
     */
    @BeforeClass
    public static void init() throws JAXBException, SAXException, IOException {
        JAXBContext context = JAXBContext.newInstance(ElfinderConfiguration.class.getPackage().getName());

        InputStream xsdStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("elfinder-configuration.xsd");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new StreamSource(xsdStream));

        // marshaller
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.trustsystems.com.br/elfinder http://www.trustsystems.com.br/elfinder/elfinder-configuration.xsd");
//        marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "file://");
        marshaller.setSchema(schema);

        // unmarshaller
        unmarshaller = context.createUnmarshaller();
        unmarshaller.setSchema(schema);

        xsdStream.close();
    }

    /**
     * Marshals Object to XML
     *
     * @throws JAXBException
     */
    @Test
    public void marshal() throws JAXBException {
        System.out.println();
        System.out.println("JAXB MARSHAL - OBJECT to XML");
        System.out.println();

        ElfinderConfiguration elfinderConfiguration = new ObjectFactory().createElfinderConfiguration();

        Thumbnail thumbnail = new ObjectFactory().createElfinderConfigurationThumbnail();

        Volume volume = new ObjectFactory().createElfinderConfigurationVolume();
        Volume volume2 = new ObjectFactory().createElfinderConfigurationVolume();

        Constraint contraint = new ObjectFactory().createElfinderConfigurationVolumeConstraint();
        Constraint contraint2 = new ObjectFactory().createElfinderConfigurationVolumeConstraint();

        contraint.setLocked(false);
        contraint.setReadable(true);
        contraint.setWritable(false);

        contraint2.setLocked(false);
        contraint2.setReadable(true);
        contraint2.setWritable(true);

        thumbnail.setWidth(new BigInteger("80"));

        volume.setSource("filesystem");
        volume.setAlias("My Books");
        volume.setPath("/Users/thiago/Development/books");
        volume.setDefault(true);
        volume.setLocale("pt_BR");
        volume.setConstraint(contraint);

        volume2.setSource("filesystem");
        volume2.setAlias("My JDBC Drivers");
        volume2.setPath("/Users/thiago/Development/jdbc/drivers");
        volume2.setDefault(false);
        volume2.setLocale("pt_BR");
        volume2.setConstraint(contraint2);

        elfinderConfiguration.setThumbnail(thumbnail);
        elfinderConfiguration.getVolume().add(volume);
        elfinderConfiguration.getVolume().add(volume2);

        try {
//            marshaller.marshal(elfinderConfiguration, new File("src/test/resources/elfinder-configuration.xml"));
            marshaller.marshal(elfinderConfiguration, System.out);
        } catch (JAXBException e) {
            System.err.println("Marshaller error: " + e.getCause().getMessage() + "\n");
            throw e;
        }
    }

    /**
     * Unmarshals XML to Object
     *
     * @throws JAXBException
     * @throws java.io.IOException
     */
    @Test(dependsOnMethods = "marshal")
    public void unmarshal() throws JAXBException, IOException {
        System.out.println();
        System.out.println("JAXB UNMARSHAL - XML to OBJECT");
        System.out.println();

        try (InputStream xmlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("elfinder-configuration.xml")) {
            ElfinderConfiguration elfinderConfiguration = (ElfinderConfiguration) unmarshaller.unmarshal(xmlStream);

            System.out.println("Thumbnail Width: " + elfinderConfiguration.getThumbnail().getWidth());
            System.out.println();
            for (ElfinderConfiguration.Volume volume : elfinderConfiguration.getVolume()) {
                System.out.println("Source  : " + volume.getSource());
                System.out.println("Alias   : " + volume.getAlias());
                System.out.println("Path    : " + volume.getPath());
                System.out.println("Default : " + volume.isDefault());
                System.out.println("Locale  : " + volume.getLocale());
                System.out.println("Locked  : " + volume.getConstraint().isLocked());
                System.out.println("Readable: " + volume.getConstraint().isReadable());
                System.out.println("Writable: " + volume.getConstraint().isWritable());
                System.out.println();
            }
        } catch (JAXBException e) {
            System.err.println("Unmarshaller error: " + e.getCause().getMessage() + "\n");
            throw e;
        }

    }
}
