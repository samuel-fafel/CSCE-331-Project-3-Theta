//Global variables
let order = [];
let order_sec = [];
let prices = [];
let items = [];

/**
 * Resize Functions
 * These functions are used to change the size of all elements on the website.
 * This helps with accessibility.
 *
 */

function resize_def(){
    //Get style element by ID (hint: getElementById)
    var style_el = document.getElementById("css");
    //Check the current stylesheet file name.
    var fileName = style_el.getAttribute("href");
    //Determine new stylesheet file name
    var newFileName = "order_style.css";
    if (fileName != "order_style.css"){
        // replace stylesheet with new stylesheet
        style_el.setAttribute("href", newFileName);
    }

    //store new name
    localStorage.setItem("styleSheet", newFileName);
}

function resize_large(){
    //Get style element by ID (hint: getElementById)
    var style_el = document.getElementById("css");
    //Check the current stylesheet file name.
    var fileName = style_el.getAttribute("href");
    //Determine new stylesheet file name
    var newFileName = "style_big.css";
    if (fileName != "style_big.css"){
        // replace stylesheet with new stylesheet
        style_el.setAttribute("href", newFileName);
    }


    //store new name
    localStorage.setItem("styleSheet", newFileName);
}

function resize_huge(){
    //Get style element by ID (hint: getElementById)
    var style_el = document.getElementById("css");
    //Check the current stylesheet file name.
    var fileName = style_el.getAttribute("href");
    //Determine new stylesheet file name
    var newFileName = "style_huge.css";
    if (fileName != "style_huge.css"){
        // replace stylesheet with new stylesheet
        style_el.setAttribute("href", newFileName);
    }

    //store new name
    localStorage.setItem("styleSheet", newFileName);
}

window.onload = function(){
        // get stylesheet name from local storage hint: localStorage.getItem(name)
        var style_element = localStorage.getItem("styleSheet");

        //Avoid null css file when first booting website
        if ((style_element != "order_style.css") && (style_element != "style_big.css") && (style_element != "style_huge.css")){
            style_element = "order_style.css";
        }
        // get html style element by ID
        var element = document.getElementById("css");
        // replace href attribute of html element.
        element.setAttribute("href", style_element);
}

async function get_price(id) {
    try {
        const response = await fetch('/get-price');
        const data = await response.json();
        const price = data[id-1].price;
        prices[0] = price;
    } catch (error) {
        console.error('Error getting price', error);
    }
    print_price();
}

//Other Functions
function print_order(){
    let OLen = order.length;

    let text = "Order Items<ul class='no-bullet'>";
    for (let i = 0; i < OLen; i++) {
      text += "<li class='no-bullet'>" + order[i] + "</li>";
    }
    text += "</ul>";
    document.getElementById("order").innerHTML = text;
}

function print_price(){
    let PLen = prices.length;

    let text = "Item Prices<ul class='no-bullet'>";
    for (let i = 0; i < PLen; i++) {
      text += "<li class='no-bullet'>" + prices[i] + "</li>";
    }
    text += "</ul>";

    document.getElementById("prices").innerHTML = text;
    update_total();
}

function update_total() {
    let PLen = prices.length;
    let total = 0.0;
    for (let i = 0; i < PLen; i++) {
        total += Number(prices[i]);
    }
    total += Number((total * 0.0825).toFixed(2));

    if (total == 0) total = '';
    let text = "Order Total<ul class='no-bullet'><li>" + total + "</li></ul>";
    document.getElementById("total").innerHTML = text;
}

function add_meal(order, item, id) {
    clear_order(); // Reset Order
    clear_price(); // Reset Price
    order[0] = item; // Set Name of Item
    print_order(); // Print to Screen
    get_price(id); // Get/Set/Print Price of Item
}

function add_entree(order, item){
    if (order[0] == "Bowl" || order[0] == "Cub_Meal" || order[0].includes("A_La_Carte_Entree_")) {
        order[1] = item;

    }
    else if (order[0] == "Plate") {
        order[1] = item;
        order[2] = item;
    }
    else if (order[0] == "Bigger_Plate") {
        order[1] = item;
        order[2] = item;
        order[3] = item;
    }
    else if (order[0] == "Family_Meal") {
        order[1] = item;
        order[2] = item;
        order[3] = item;
    }
    else if (order[0].includes("A_La_Carte_Side_")) {
        order[1] = "Please choose from: Sides";
    }
    else {
        order[0] = "First select from:"
        order[1] = "Regular Meals"
        order[2] = "or"
        order[3] = "A-La-Carte"
    }
    print_order();
}

function add_side(order, item){
    if (order[0] == "Bowl" || order[0] == "Cub_Meal") {
        order[2] = item;
    }
    else if (order[0] == "Plate") {
        order[3] = item;
        order[4] = item;
    }
    else if (order[0] == "Bigger_Plate") {
        order[4] = item;
        order[5] = item;
    }
    else if (order[0] == "Family_Meal") {
        order[4] = item;
        order[5] = item;
    }
    else if (order[0].includes("A_La_Carte_Side_")) {
        order[1] = item;
    }
    else if (order[0].includes("A_La_Carte_Entree_")) {
        order[1] = "Please choose from: Entrees";
    }
    else {
        order[0] = "First select from:"
        order[1] = "Regular Meals"
        order[2] = "or"
        order[3] = "A-La-Carte"
    }
    print_order();
}

function add_appetizer(order, item) {
    order[4] = item;
    print_order();
}

function add_drink(order, item) {
    order[5] = item;
    print_order();
}

function clear_order(){
    if (order[0] == '') order = [];
    for (let i = 0; i < order.length; i++) {
        order[i] = [''];
    }
}

function clear_price(){
    if (prices[0] == '') prices = [];
    for (let i = 0; i < prices.length; i++) {
        prices[i] = [''];
    }
}

//Google Maps Functionality
let map;

async function initMap() {
  //@ts-ignore
  const { Map } = await google.maps.importLibrary("maps");

  map = new Map(document.getElementById("map"), {
    center: { lat: 30.6125, lng: -96.341 },
    zoom: 16,
  });

  // The marker, positioned at Panda
  var marker = new google.maps.Marker({
    position: { lat: 30.61225, lng: -96.34146 },
    title: "Panda Express MSC",
    animation: google.maps.Animation.DROP,
    //icon: {labelOrigin: new google.maps.Point(100,0)},
    label: "Panda Express MSC",
  })

  marker.setMap(map);
}

initMap();

// get radio buttons and labels
const radioButtons = document.querySelectorAll('input[type="radio"]');
const labels = document.querySelectorAll('label');

// add event listener to each radio button
radioButtons.forEach((radioButton, index) => {
  radioButton.addEventListener("change", () => {
    // remove "selected" class from all labels
    labels.forEach((label) => label.classList.remove("selected"));

    // add "selected" class to the label of the selected radio button
    labels[index].classList.add("selected");
  });
});
