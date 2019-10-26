import React, { useState } from "react";
import "./App.css";
import { Map, TileLayer, GeoJSON } from "react-leaflet";
import shp from "shpjs";
import concaveman from "concaveman";

import "leaflet/dist/leaflet.css";

function App() {
  const [points, setPoints] = useState();
  const [polygon, setPolygon] = useState();

  const handleFile = file2 => {
    var reader = new FileReader();

    reader.addEventListener(
      "load",
      () => {
        const geoJson = shp.parseShp(reader.result);
        setPoints(geoJson);
        const points = geoJson.map(item => item.coordinates);
        const polygon = concaveman(points);
        const geoJsonPolygon = {
          type: "Feature",
          geometry: {
            type: "Polygon",
            coordinates: [polygon]
          }
        };
        setPolygon(geoJsonPolygon);
      },
      false
    );
    if (file2.target.files) {
      reader.readAsArrayBuffer(file2.target.files[0]);
    }
  };

  let position = [51.84276079, 5.18380148];
  if (polygon) {
    const [first, second] = polygon.geometry.coordinates[0][0];
    position = [second, first];
  }

  return (
    <div className="map">
      <Map center={position} zoom={15} className="leaflet-map">
        <TileLayer
          attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {/* {points && <GeoJSON data={points} />} */}
        {polygon && <GeoJSON data={polygon} />}
      </Map>
      <input type="file" id="input" onChange={handleFile}></input>
    </div>
  );
}

export default App;
