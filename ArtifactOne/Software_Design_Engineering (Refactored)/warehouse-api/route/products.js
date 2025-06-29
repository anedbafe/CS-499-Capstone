const express = require('express');
const router = express.Router();
const Product = require('../model/product');

// ==== GET ALL PRODUCTS ====
router.get('/', async (req, res) => {
  try {
    const products = await Product.find();
    res.status(200).json(products);
  } catch (error) {
    res.status(500).json({ message: 'Failed to fetch products', error });
  }
});

// ==== GET PRODUCT BY NAME (opcional) ====
router.get('/search/:name', async (req, res) => {
  const { name } = req.params;
  try {
    const product = await Product.findOne({ product_name: name });
    if (product) {
      res.status(200).json(product);
    } else {
      res.status(404).json({ message: 'Product not found' });
    }
  } catch (error) {
    res.status(500).json({ message: 'Failed to fetch product', error });
  }
});

// ==== ADD A PRODUCT ====
router.post('/', async (req, res) => {
  const { product_name, product_quantity, product_location } = req.body;

  try {
    const newProduct = new Product({ product_name, product_quantity, product_location });
    await newProduct.save();
    res.status(201).json(newProduct);
  } catch (error) {
    res.status(500).json({ message: 'Failed to add product', error });
  }
});

// ==== UPDATE PRODUCT BY ID ====
router.put('/:id', async (req, res) => {
  const { id } = req.params;
  const { product_name, product_quantity, product_location } = req.body;

  try {
    const updatedProduct = await Product.findByIdAndUpdate(
      id,
      { product_name, product_quantity, product_location },
      { new: true }
    );

    if (updatedProduct) {
      res.status(200).json(updatedProduct);
    } else {
      res.status(404).json({ message: 'Product not found' });
    }
  } catch (error) {
    res.status(500).json({ message: 'Failed to update product', error });
  }
});

// ==== DELETE PRODUCT BY ID ====
router.delete('/:id', async (req, res) => {
  const { id } = req.params;

  try {
    const deletedProduct = await Product.findByIdAndDelete(id);

    if (deletedProduct) {
      res.status(200).json({ message: 'Product deleted successfully' });
    } else {
      res.status(404).json({ message: 'Product not found' });
    }
  } catch (error) {
    res.status(500).json({ message: 'Failed to delete product', error });
  }
});

module.exports = router;
