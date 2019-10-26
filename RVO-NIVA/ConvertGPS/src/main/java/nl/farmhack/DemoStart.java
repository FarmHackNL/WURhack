package nl.farmhack;


import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Prompts the user for a shapefile and displays the contents on the screen in a map frame.
 *
 * <p>This is the GeoTools Quickstart application used in documentationa and tutorials. *
 */
public class DemoStart {

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile and displays its
     * contents on the screen in a map frame
     */
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
        Map<String, Serializable> params = new HashMap<>();

 			URL url = DemoStart.class.getResource("session-ib012-2019-03-30-GPS.csv");
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
 	            }
}