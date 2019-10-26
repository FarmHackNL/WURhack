This Challenge is about finding an automated way to get machinedata into the datastore of the RVO paying agency. 
Different team members are working on the current topics:

- Filtering raw data (to only have data that describes actual activity)
- Creating a polygon from the data, to have a contour (necessary for digestion of the API of RVO)
- Reprojecting the coordinate system from Longitude/Latitude to EPSG 28993 (Dutch coordinate system)
- Adding metadata (if required)
- Pushing the data through SOAP api.

Results are published in subfolders:

- ConvertGPS for the code converting GPS coordinate data to the correct coordinate system used in NL
- Shapefile-master as a prototype for determining the polygon from a point data set (shapefile)
- Polygon conversion as a V2 for the shapefile to polygon conversion, including a web interface.