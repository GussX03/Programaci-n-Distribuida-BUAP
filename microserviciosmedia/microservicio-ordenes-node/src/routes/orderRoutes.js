const express = require("express");
const {
  createOrder,
  getOrderById,
  updateOrderStatus,
  getAllOrders,
  getOrdersByUser
} = require("../controllers/orderController");

const router = express.Router();

router.post("/orders", createOrder);
router.get("/orders", getAllOrders);
router.get("/orders/:id", getOrderById);
router.get("/orders/user/:userId", getOrdersByUser);
router.put("/orders/:id/status", updateOrderStatus);

module.exports = router;