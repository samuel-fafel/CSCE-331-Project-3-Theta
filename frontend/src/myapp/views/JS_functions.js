//Global variables
let order = [];
let prices = [];
let items = [];
let current_entree = 1;
let current_side = 2;
let current_drink = 3;
let TRANSACTION_ID = 300000;
let PAYMENT_METHOD = "Dining Dollars";

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

/**
 * Resize to LArger Functions
 * This functions is used to change the size of all elements on the website to be larger.
 * This helps with accessibility.
 *
 * @function
 */
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

// function resize_huge(){
//     //Get style element by ID (hint: getElementById)
//     var style_el = document.getElementById("css");
//     //Check the current stylesheet file name.
//     var fileName = style_el.getAttribute("href");
//     //Determine new stylesheet file name
//     var newFileName = "style_huge.css";
//     if (fileName != "style_huge.css"){
//         // replace stylesheet with new stylesheet
//         style_el.setAttribute("href", newFileName);
//     }

//     //store new name
//     localStorage.setItem("styleSheet", newFileName);
// }

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

/**
 * get_price
 * This function fetches the price of a meal with the given id
 * calls the print_price() function after retrieval.
 * 
 * @function
 * @param id - Item ID, used as an index
 */
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

/**
 * get_drink_price
 * This function fetches the price of a drink with the given id
 * calls the print_price() function after retrieval.
 * 
 * @function
 * @param id - Item ID, used as an index
 */
async function get_drink_price(id) {
    try {
        const response = await fetch('/get-drink-price');
        const data = await response.json();
        const price = data[id-1].price;
        prices[1] = price;
    } catch (error) {
        console.error('Error getting price', error);
    }
    print_price();
}

/**
 * print_order
 * This function prints the current order to the "Order Items" panel
 * Does not return anything
 * 
 * @function
 */
function print_order(){
    let OLen = order.length;

    let text = "<h1>Order Items</h1><ul class='no-bullet overflow-fix'>";
    for (let i = 0; i < OLen; i++) {
        if (i == 0) {
            text += "<li class='no-bullet'><u>" + order[i] + "</u></li>";
        }
        else {
            text += "<li class='no-bullet'>" + order[i] + "</li>";
        }
    }
    text += "</ul>";
    document.getElementById("order").innerHTML = text;
}

/**
 * update_total
 * This function updates the Total price in the "Order Total" panel
 * Returns the total price, including tax
 * 
 * @function
 * @returns The new total price of an order including tax
 */
function update_total() {
    let PLen = prices.length;
    let total = Number(0.0);
    for (let i = 0; i < PLen; i++) {
        total += Number(prices[i]);
    }
    total += Number((total * 0.0825).toFixed(2));
    total = Number(total.toFixed(2));

    let text = "<h1>Order Total</h1><ul class='no-bullet'><li>$" + total + "</li></ul>";
    if (total == 0) {
        total = '';
        text = "<h1>Order Total</h1><ul class='no-bullet'><li>" + total + "</li></ul>";
    }

    document.getElementById("total").innerHTML = text;
    return total;
}

/**
 * print_price
 * This function prints the current order's price to the "Subtotal" panel
 * Calls the update_total() function
 * Does not return anything
 * 
 * @function
 */
function print_price(){
    let PLen = prices.length;

    let text = "<h1>Item Prices</h1><ul class='no-bullet'>";
    if (prices[0] != '') {
        for (let i = 0; i < PLen; i++) {
            text += "<li class='no-bullet'>$" + prices[i] + "</li>";
        }
    }
    text += "</ul>";

    document.getElementById("prices").innerHTML = text;
    update_total();
}

/**
 * add_meal
 * This function resets the current order/price and adds the selected meal to the order panel
 * Calls print_order() and get_price()
 * 
 * @function
 */
function add_meal(order, item, id) {
    clear_order(); // Reset Order
    clear_price(); // Reset Price
    order[0] = item; // Set Name of Item
    if (item == "Bowl" || item == "Cub Meal" || item.includes("Entree") || item.includes("Side")) {
        order[1] = "Entree 1";
        if (item.includes("Side")) {
            order[1] = "Side 1";
        } else if (!item.includes("Entree")) {
            order[2] = "Side 1";
        }
        current_side = 2;
        current_drink = 3;
    }
    else if (item == "Plate") {
        order[1] = "Entree 1";
        order[2] = "Entree 2";
        order[3] = "Side 1";
        current_side = 3;
        current_drink = 4;
    }
    else if (item == "Bigger Plate") {
        order[1] = "Entree 1";
        order[2] = "Entree 2";
        order[3] = "Entree 3";
        order[4] = "Side 1";
        current_side = 4;
        current_drink = 5;
    }
    else if (item == "Family Meal") {
        order[1] = "Large Entree 1";
        order[2] = "Large Entree 2";
        order[3] = "Large Entree 3";
        order[4] = "Large Side 1";
        order[5] = "Large Side 2";
        current_side = 4;
        current_drink = 6;
    }
    else if (item.includes("Side")) {
        order[1] = "Please choose from: Sides";
    }
    else {
        order[0] = "First select from:"
        order[1] = "Regular Meals"
        order[2] = "or"
        order[3] = "A-La-Carte"
    }

    print_order(); // Print to Screen
    print_price();
    if (id) {
        get_price(id); // Get/Set/Print Price of Item
    }
}

/**
 * add_entree
 * This function adds the selected entree to the order panel
 * Depending on the meal type selected, behavior changes slightly
 * 
 * @function
 * @param order The order that the entree will be added to
 * @param item The item that will be added to the order
 */
function add_entree(order, item){
    if (order[0] == "Bowl" || order[0] == "Cub Meal" || order[0].includes("Entree")) {
        if (current_entree != 1) {current_entree = 1};
        order[current_entree] = item;
        current_side = 2;
    }
    else if (order[0] == "Plate") {
        if (current_entree > 2) {current_entree = 1};
        order[current_entree++] = item;
        current_side = 3;
    }
    else if (order[0] == "Bigger Plate") {
        if (current_entree > 3) {current_entree = 1};
        order[current_entree++] = item;
        current_side = 3;
    }
    else if (order[0] == "Family Meal") {
        if (current_entree > 3) {current_entree = 1};
        order[current_entree++] = item;
    }
    else if (order[0].includes("Side")) {
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

/**
 * add_side
 * This function adds the selected side to the order panel
 * Depending on the meal type selected, behavior changes slightly
 * 
 * @function
 * @param order The order that the side will be added to
 * @param item The item that will be added to the order
 */
function add_side(order, item){
    if (order[0] == "Bowl" || order[0] == "Cub Meal") {
        order[current_side] = item;
    }
    else if (order[0] == "Plate") {
        order[current_side] = item;
    }
    else if (order[0] == "Bigger Plate") {
        if (current_side != 4) {current_side = 4};
        order[current_side] = item;
    }
    else if (order[0] == "Family Meal") {
        if (current_side < 4 || current_side > 5) {current_side = 4};
        order[current_side++] = item;
    }
    else if (order[0].includes("Side")) {
        order[1] = item;
    }
    else if (order[0].includes("Entree")) {
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

/**
 * add_appetizer
 * This function adds the selected appetizer to the order panel
 * Depending on the meal type selected, behavior changes slightly
 * 
 * @function
 * @param order The order that the side will be added to
 * @param item The item that will be added to the order
 */
function add_appetizer(order, item) {
    if (order[0] != "" && !order[0].includes() != "First select from:") {
        order[current_drink+1] = item;
        print_order();
    }
}

/**
 * add_drink
 * This function adds the selected drink to the order panel
 * Depending on the meal type selected, behavior changes slightly
 * 
 * @function
 * @param order The order that the drink will be added to
 * @param item The item that will be added to the order
 * @id The item identification
 */
function add_drink(order, item, id) {
    if (order[0] != "" && !order[0].includes("First select from:") ) {
        order[current_drink] = item;
        print_order();
        print_price();
        if (id) {
            get_drink_price(id);
        }
    }
}

/**
 * clear_order
 * This function resets the current order list
 * Does not return anything
 * 
 * @function
 */
function clear_order(){
    current_entree = 1;
    current_side = 2;
    current_drink = 3;
    document.getElementById("place_order").innerHTML = "Place Order";
    for (let i = 0; i < order.length; i++) {
        order[i] = [''];
    }
}

/**
 * clear_price
 * This function resets the current price list
 * Does not return anything
 * 
 * @function
 */
function clear_price(){
    for (let i = 0; i < prices.length; i++) {
        prices[i] = [''];
    }
}

/**
 * alter_payment_method
 * This function is used to set the payment method
 * Does not return anything
 * 
 * @function
 * @param method This is the type of payment method the customer will use
 */
function alter_payment_method(method) {
    PAYMENT_METHOD = method;
}


/**
 * get_latest_transaction
 * This function fetches the latest transaction id
 * 
 * @function
 */
async function get_latest_transaction() {
    try {
        const response = await fetch('/get-latest-transaction');
        const data = await response.json();
        const id = data[0].id;
        TRANSACTION_ID = id + 1;
    } catch (error) {
        console.error('Error getting price', error);
    }
}

/**
 * insert_query
 * This function inserts the given query into the AWS Database
 * 
 * @function
 * @param my_query This is the SQL query that is passed
 */
async function insert_query(my_query) {
    try {
        const response = await fetch('/insert-query', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ my_query })
        });
        const data = await response.text();
        console.log(data);
    } catch (error) {
        console.error('Error inserting query', error);
    }
}


/**
 * place_order
 * This function sends a complete transaction to the database and clears the current order.
 * Does not return anything
 * 
 * @function
 */
async function place_order(){
    await get_latest_transaction();
    let OLen = order.length;
    let PLen = prices.length;
    let taxtotal = update_total();
    let subtotal = 0.0;
    let valid = 0;
    for (let p = 0; p < PLen; p++) { // sum prices for subtotal entree
        subtotal += Number(prices[p]);
    }
    subtotal = Number(subtotal.toFixed(2));
    let tax = taxtotal-subtotal;

    const currentDate = new Date();
    const month = ('0' + (currentDate.getMonth() + 1)).slice(-2);
    const day = ('0' + currentDate.getDate()).slice(-2);
    const year = currentDate.getFullYear();
    const hours = ('0' + currentDate.getHours()).slice(-2);
    const minutes = ('0' + currentDate.getMinutes()).slice(-2);
    const seconds = ('0' + currentDate.getSeconds()).slice(-2);
    const formattedDate = `${month}/${day}/${year}`;
    const formattedTime = `${hours}:${minutes}:${seconds}`;

    let queryString = "INSERT INTO transactions VALUES('";
    queryString += TRANSACTION_ID + "', 'Sale', ";
    switch (order[0]) {
        case "Bowl":
            if (!order[3]) {order[3] = "none";}
            queryString += "'" + order[0] + "', '" + order[1] + "', 'none', 'none', '" + order[2] + "', 'none', '" + order[3] + "', ";
            valid = 1;
            break;
        case "Plate":
            queryString += "'" + order[0] + "', '" + order[1] + "', '" + order[2] + "', 'none', '" + order[3] + "', 'none', '" + order[4] + "', ";
            valid = 1;
            break;
        case "Bigger Plate":
            queryString += "'" + order[0] + "', '" + order[1] + "', '" + order[2] + "', '" + order[3] + "', '" + order[4] + "', 'none', '" + order[5] + "', ";
            valid = 1;
            break;
        case "Family Meal":
            queryString += "'" + order[0] + "', '" + order[1] + "', '" + order[2] + "', '" + order[3] + "', '" + order[4] + "', '" + order[5] + "', '" + order[6] + "', ";
            valid = 1;
            break;
        case "Cub Meal":
            queryString += "'" + order[0] + "', '" + order[1] + "', 'none', 'none', 'none', 'none', '" + order[3] + "', ";
            valid = 1;
            break;
        case "Small Entree A-La-Carte":
            queryString += "'" + order[0] + "', '" + order[1] + "', 'none', 'none', 'none', 'none', '" + order[3] + "', ";
            valid = 1;
            break;
        case "Medium Entree A-La-Carte":
            queryString += "'" + order[0] + "', '" + order[1] + "', 'none', 'none', 'none', 'none', '" + order[3] + "', ";
            valid = 1;
            break;
        case "Large Entree A-La-Carte":
            queryString += "'" + order[0] + "', '" + order[1] + "', 'none', 'none', 'none', 'none', '" + order[3] + "', ";
            valid = 1;
            break;
        case "Medium Side A-La-Carte":
            queryString += "'" + order[0] + "', 'none', 'none', 'none', '"+order[1]+"', 'none', '" + order[3] + "', ";
            valid = 1;
            break;
        case "Large Side A-La-Carte":
            queryString += "'" + order[0] + "', 'none', 'none', 'none', '"+order[1]+"', 'none', '" + order[3] + "', ";
            valid = 1;
            break;
        default:
            queryString += "'none', 'none', 'none', 'none', 'none', 'none', 'none', ";
            valid = 0;
    }
    queryString += "'" + formattedDate + "', 'Customer', '" + PAYMENT_METHOD + "', '" + Number(subtotal).toFixed(2) + "', '" + Number(tax).toFixed(2)
    + "', '" + Number(taxtotal).toFixed(2) + "', '" + formattedTime + "', '" + Number(TRANSACTION_ID/100).toFixed(0) + "')";
    for (let i = 0; i < OLen; i++) {
        if (order[i].includes("Please choose from:") || order[i].includes("First select from:")) {
            valid = 0;
        }
        if (i > 0 && (order[i].includes("Side") || order[i].includes("Entree"))) {
            valid = 0;
        }
    }
    if (valid) {
        insert_query(queryString);
        clear_order();
        order[0] = "Order Placed!";
        print_order();
    } else {
        document.getElementById("place_order").innerHTML = "Place Order: Invalid Order!";
    }
}

//Google Maps Functionality
let map;
/**
 * This function does the API call for Google maps,
 * it passes exact coordinates and places a marker
 * 
 */
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