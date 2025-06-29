// ==== IMPORT DEPENDENCIES ====
const express = require('express');
const connectDB = require('./db');
const cors = require('cors');
require('dotenv').config();

// ==== INITIALIZE EXPRESS APP ====
const app = express();
const PORT = process.env.PORT || 5000;

// ==== MIDDLEWARE TO PARSE JSON ====
app.use(express.json());
app.use(cors());

// ==== CONNECT TO MONGODB ====
connectDB();

// ==== ROUTES ====
const authRoutes = require('./route/auth');
const productRoutes = require('./route/products');

app.use('/api/users', authRoutes);
app.use('/api/products', productRoutes);

// ==== BASIC ROUTE ====
app.get('/', (req, res) => {
  res.send('🚀 Warehouse API is working');
});

// ==== START SERVER ====
app.listen(PORT, () => {
  console.log(`✅ Server running on http://localhost:${PORT}`);
});
