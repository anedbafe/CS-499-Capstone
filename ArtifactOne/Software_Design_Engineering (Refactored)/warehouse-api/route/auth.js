// ==== ROUTER FOR USER AUTHENTICATION ====
const express = require('express');
const router = express.Router();
const User = require('../model/User');

// ==== REGISTER USER ====
router.post('/register', async (req, res) => {
  const { username, email, password } = req.body;
  try {
    const existingUser = await User.findOne({ email });
    if (existingUser) return res.status(400).json({ message: 'User already exists' });

    const newUser = new User({ username, email, password });
    await newUser.save();

    console.log('New user created:', newUser); // CONFIRMS USER IS SAVED

    res.status(201).json({ message: 'User registered successfully' });
  } catch (error) {
    res.status(500).json({ message: 'Registration failed', error });
  }
});

// ==== LOGIN USER ====
router.post('/login', async (req, res) => {
  const { email, password } = req.body;
  try {
    const user = await User.findOne({ email, password });
    if (!user) return res.status(401).json({ message: 'Invalid email or password' });

    res.status(200).json({ message: 'Login successful', user });
  } catch (error) {
    res.status(500).json({ message: 'Login failed', error });
  }
});

// ==== EXPORT ROUTER ====
module.exports = router;
