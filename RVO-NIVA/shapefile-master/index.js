const shp = require("shpjs");
const fs = require("fs");
const concaveman = require("concaveman");

console.log("1234");

fs.readFile("./jacobcirkel.shp", (err, data) => {
  if (err) throw err;
  const geoJson = shp.parseShp(data);

  fs.writeFile("./output/shapefile.geojson", JSON.stringify(geoJson), function(
    err
  ) {
    if (err) {
      throw err;
    }

    console.log("The file was saved!");
  });
  const points = geoJson.map(item => item.coordinates);
  const polygon = concaveman(points);
  const geoJsonPolygon = {
    type: "Feature",
    geometry: {
      type: "Polygon",
      coordinates: [polygon]
    }
  };

  fs.writeFile(
    "./output/polygon.geojson",
    JSON.stringify(geoJsonPolygon),
    function(err) {
      if (err) {
        throw err;
      }

      console.log("The file was saved!");
    }
  );
});
