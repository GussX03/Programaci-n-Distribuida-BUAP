const express = require("express");
const {
  getInventoryByProductId,
  createOrUpdateInventory,
  updateStock,
  getAllInventory,
  getLowStockItems,
  deleteInventoryItem
} = require("../controllers/inventoryController");

const router = express.Router();

router.get("/inventory", getAllInventory);
router.get("/inventory/low-stock", getLowStockItems);
router.get("/inventory/:productId", getInventoryByProductId);
router.post("/inventory", createOrUpdateInventory);
router.put("/inventory/:productId", updateStock);
router.delete("/inventory/:productId", deleteInventoryItem);

module.exports = router;