const mongoose = require('mongoose');

// PRODUCT SCHEMA
const productSchema = new mongoose.Schema({
  product_name:     { 
    type: String, 
    required: true 
},
product_quantity: { 
    type: Number, 
    required: true 
},
product_location: { 
    type: String, 
    required: true }
});

// EXPORT PRODUCT MODEL
module.exports = mongoose.model('Product', productSchema);
