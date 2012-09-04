package org.yummydata.store;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.manager.RepositoryManager;
import org.openrdf.repository.util.RDFInserter;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.turtle.TurtleParser;


public class InsertTTL {

	public static void main(String[] args) throws FileNotFoundException, IOException, 
	RDFHandlerException, RDFParseException, RepositoryException, RepositoryConfigException {
		String url = "http://monomorphic.org:8001/openrdf-sesame";
		RepositoryManager rm = RemoteRepositoryManager.getInstance(url, "user", "pass");
		
		rm.initialize();
		Repository r = rm.getRepository("yummy");
		RepositoryConnection con = r.getConnection();
		System.out.println("Got connection");
		TurtleParser ttl = new TurtleParser();
		ttl.setRDFHandler(new RDFInserter(con));
		System.out.println("Inserting data");
		ttl.parse(new FileInputStream(args[0]), "http://yummydata.org");
		con.close();				
		System.out.println("Done");
	}
}
