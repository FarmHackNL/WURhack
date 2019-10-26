package nl.farmhack;

import com.csvreader.CsvReader;
import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Read-only access to CSV File.
 *
 * @author Jody Garnett (Boundless)
 */
public class CSVFeatureSource extends ContentFeatureSource {

	public CSVFeatureSource(ContentEntry entry, Query query) {
		super(entry, query);
	}

	/** Access parent CSVDataStore. */
	public CsvDataStore getDataStore() {
		return (CsvDataStore) super.getDataStore();
	}

	@Override
	protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
		return null; // feature by feature scan required to establish bounds
	}

	@Override
	protected int getCountInternal(Query query) throws IOException {
		// TODO Auto-generated method stub
		if (query.getFilter() == Filter.INCLUDE) {
			CsvReader reader = getDataStore().read();
			try {
				boolean connect = reader.readHeaders();
				if (connect == false) {
					throw new IOException("Unable to connect");
				}
				int count = 0;
				while (reader.readRecord()) {
					count += 1;
				}
				return count;
			} finally {
				reader.close();
			}
		}
		return -1; // feature by feature scan required to count records
	}

	@Override
	protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
		// TODO Auto-generated method stub
		return new CsvFeatureReader(query);
	}

	@Override
	protected SimpleFeatureType buildFeatureType() throws IOException {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName(entry.getName());

		// read headers
		CsvReader reader = getDataStore().read();
		try {
			boolean success = reader.readHeaders();
			if (success == false) {
				throw new IOException("Header of CSV file not available");
			}

			// we are going to hard code a point location
			// columns like lat and lon will be gathered into a
			// Point called Location
			builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system
			builder.add("Location", Point.class);

			for (String column : reader.getHeaders()) {
				if ("lat".equalsIgnoreCase(column)) {
					continue; // skip as it is part of Location
				}
				if ("lon".equalsIgnoreCase(column)) {
					continue; // skip as it is part of Location
				}
				builder.add(column, String.class);
			}

			// build the type (it is immutable and cannot be modified)
			final SimpleFeatureType SCHEMA = builder.buildFeatureType();
			return SCHEMA;
		} finally {
			reader.close();
		}
	}

}