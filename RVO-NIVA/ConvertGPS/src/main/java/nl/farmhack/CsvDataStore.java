package nl.farmhack;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;

/**
 * DataStore for Comma Seperated Value (CSV) files.
 * https://docs.geotools.org/latest/userguide/tutorial/datastore/source.html
 * 
 * @author Jody Garnett (Boundless)
 */
public class CsvDataStore extends ContentDataStore {

	File file;

	public CsvDataStore(File file) {
		        this.file = file;
		    }

	/**
	 * Allow read access to file; for our package visible "friends". Please close
	 * the reader when done.
	 *
	 * @return CsvReader for file
	 */
	CsvReader read() throws IOException {
		Reader reader = new FileReader(file);
		CsvReader csvReader = new CsvReader(reader);
		return csvReader;
	}

	protected List<Name> createTypeNames() throws IOException {
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf('.'));

		Name typeName = new NameImpl(name);
		return Collections.singletonList(typeName);
	}

	@Override
	protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
		return new CSVFeatureSource(entry, Query.ALL);
	}

}