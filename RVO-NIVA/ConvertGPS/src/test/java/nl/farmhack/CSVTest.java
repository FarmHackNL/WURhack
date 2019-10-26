package nl.farmhack;
/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.junit.Test;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;

import com.csvreader.CsvReader;

public class CSVTest {

	@Test
	public void listNames() throws Exception {

		Map<String, Serializable> params = new HashMap<>();

		URL url = CSVTest.class.getResource("session-ib012-2019-03-30-GPS.csv");
		File file = new File(url.toURI());
		try (FileReader reader = new FileReader(file)) {
			params.put("file", file);

			DataStore store = DataStoreFinder.getDataStore(params);

			String names[] = store.getTypeNames();
			System.out.println("typenames: " + names.length);
			System.out.println("typename[0]: " + names[0]);

		}
	}
	
	@Test
	public void work() throws Exception {
	       Map<String, Serializable> params = new HashMap<>();

			URL url = CSVTest.class.getResource("session-ib012-2019-03-30-GPS.csv");
			File file = new File(url.toURI());

	       params.put("file", file);
	        DataStore store = DataStoreFinder.getDataStore(params);

	        SimpleFeatureType type = store.getSchema("session-ib012-2019-03-30-GPS");

	        System.out.println("featureType  name: " + type.getName());
	        System.out.println("featureType count: " + type.getAttributeCount());

	        System.out.println("featuretype attributes list:");
	        // access by list
	        for (AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
	            System.out.print("  " + descriptor.getName());
	            System.out.print(
	                    " (" + descriptor.getMinOccurs() + "," + descriptor.getMaxOccurs() + ",");
	            System.out.print((descriptor.isNillable() ? "nillable" : "manditory") + ")");
	            System.out.print(" type: " + descriptor.getType().getName());
	            System.out.println(" binding: " + descriptor.getType().getBinding().getSimpleName());
	        }
	        // access by index
	        AttributeDescriptor attributeDescriptor = type.getDescriptor(0);
	        System.out.println("attribute 0    name: " + attributeDescriptor.getName());
	        System.out.println("attribute 0    type: " + attributeDescriptor.getType().toString());
	        System.out.println("attribute 0 binding: " + attributeDescriptor.getType().getBinding());

	        // access by name
	        AttributeDescriptor cityDescriptor = type.getDescriptor("value");
	        System.out.println("attribute 'value'    name: " + cityDescriptor.getName());
	        System.out.println("attribute 'value'    type: " + cityDescriptor.getType().toString());
	        System.out.println("attribute 'value' binding: " + cityDescriptor.getType().getBinding());

	        // default geometry
	        GeometryDescriptor geometryDescriptor = type.getGeometryDescriptor();
	        System.out.println("default geom    name: " + geometryDescriptor.getName());
	        System.out.println("default geom    type: " + geometryDescriptor.getType().toString());
	        System.out.println("default geom binding: " + geometryDescriptor.getType().getBinding());
	        System.out.println(
	                "default geom     crs: "
	                        + CRS.toSRS(geometryDescriptor.getCoordinateReferenceSystem()));
	        
	        Style style = SLD.createSimpleStyle(type);
	        Layer layer = new FeatureLayer(store.getFeatureSource(type.getName()), style);
	        
	        MapContent map = new MapContent();
	        map.setTitle("Quickstart");
	        map.addLayer(layer);

	        // Now display the map
	        JMapFrame.showMap(map);
	        
	        // TODO make Concave Hull
	        
	}
	
	@Test
	public void testSomeExampleDataFromGeoTools() throws Exception {
		List<String> cities = new ArrayList<>();
		URL url = CSVTest.class.getResource("locations.csv");
		File file = new File(url.toURI());
		try (FileReader reader = new FileReader(file)) {
			CsvReader locations = new CsvReader(reader);
			locations.readHeaders();
			while (locations.readRecord()) {
				cities.add(locations.get("CITY"));
			}
		}
		assertTrue(cities.contains("Victoria"));
	}
	
	
	@Test
	public void tes() throws Exception {
        
        Map<String, Serializable> params = new HashMap<>();

     			URL url = CSVTest.class.getResource("session-ib012-2019-03-30-GPS.csv");
     			File file = new File(url.toURI());

        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

//        Set<FeatureId> selection = new HashSet();
//        selection.add(ff.featureId("locations.7"));
//
//        Filter<T> filter = ff.id(selection);
//        Query query = new Query("locations", filter);
//
//        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
//                store.getFeatureReader(query, Transaction.AUTO_COMMIT);
//
//        try {
//            while (reader.hasNext()) {
//                SimpleFeature feature = reader.next();
//                System.out.println("feature " + feature.getID());
//
//                for (Property property : feature.getProperties()) {
//                    System.out.print("\t");
//                    System.out.print(property.getName());
//                    System.out.print(" = ");
//                    System.out.println(property.getValue());
//                }
//            }
//        } finally {
//            reader.close();
//        }
	}
	
}