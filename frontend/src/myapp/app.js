const express = require('express');
const { Pool } = require('pg');
const dotenv = require('dotenv').config({ path: './database.env' });
//const { Loader } = require('@googlemaps/js-api-loader');

//Create express app
const app = express();
const port = 3000;

//Load Google Maps API
/*const loader = new Loader({
    apiKey: "AIzaSyDHMrXaSQ6ROeOPjYppOK1rorr5laqEBbg",
    version: "weekly"
  });
  
  loader.load().then(async () => {
    const { Map } = await google.maps.importLibrary("maps");
  
    map = new Map(document.getElementById("map"), {
      center: { lat: -34.397, lng: 150.644 },
      zoom: 15,
    });
  });*/

  //Create Pool
const pool = new Pool({
    user: process.env.PSQL_USER,
    host: process.env.PSQL_HOST,
    database: process.env.PSQL_DATABASE,
    password: process.env.PSQL_PASSWORD,
    port: process.env.PSQL_PORT,
    ssl: {rejectUnauthorized: false}
});

//Add process hook to shutdown pool
process.on('SIGINT', function() {
    pool.end();
    console.log('\nApplication Successfully Shutdown');
    process.exit(0);
});

/*
async function connect() {
    try {
      await pool.connect();
      console.log('Connected to database');
    } catch (error) {
      console.error('Error connecting to database', error);
    }
}
*/

app.set("view engine", "ejs");
app.use(express.static(__dirname + '/views'));

app.get('/', (req, res) => {
    res.render('index');
});

app.get('/order', (req, res) => {
    res.render('order');
});

app.get('/get-price', async (req, res) => {
    try {
      const queryString = 'SELECT price FROM menu_meals ORDER BY id';
      const result = await pool.query(queryString);
      res.json(result.rows);
    } catch (error) {
      console.error('Error executing query', error);
      res.status(500).send('Internal server error');
    }
});
    
app.listen(port, () => {
console.log(`Example app listening at http://localhost:${port}`);
});