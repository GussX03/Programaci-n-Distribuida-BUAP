const Order = require("../models/Order");

// Crear una orden
const createOrder = async (req, res) => {
  try {
    const order = new Order(req.body);
    await order.save();
    res.status(201).json(order);
  } catch (error) {
    res.status(400).json({ error: "Error al crear la orden" });
  }
};

// Obtener orden por ID
const getOrderById = async (req, res) => {
  try {
    const order = await Order.findById(req.params.id);
    if (!order) {
      return res.status(404).json({ error: "Orden no encontrada" });
    }
    res.json(order);
  } catch (error) {
    res.status(400).json({ error: "Error al obtener la orden" });
  }
};

// Actualizar estado de la orden
const updateOrderStatus = async (req, res) => {
  try {
    const { status } = req.body;
    const order = await Order.findByIdAndUpdate(
      req.params.id,
      { status },
      { new: true, runValidators: true }
    );
    if (!order) {
      return res.status(404).json({ error: "Orden no encontrada" });
    }
    res.json(order);
  } catch (error) {
    res.status(400).json({ error: "Error al actualizar el estado" });
  }
};

// Obtener todas las órdenes (opcional)
const getAllOrders = async (req, res) => {
  try {
    const orders = await Order.find();
    res.json(orders);
  } catch (error) {
    res.status(500).json({ error: "Error al obtener las órdenes" });
  }
};

// Obtener órdenes por usuario
const getOrdersByUser = async (req, res) => {
  try {
    const orders = await Order.find({ userId: req.params.userId });
    res.json(orders);
  } catch (error) {
    res.status(500).json({ error: "Error al obtener las órdenes del usuario" });
  }
};

module.exports = {
  createOrder,
  getOrderById,
  updateOrderStatus,
  getAllOrders,
  getOrdersByUser
};