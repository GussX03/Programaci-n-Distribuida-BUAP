const mongoose = require("mongoose");

const InventorySchema = new mongoose.Schema({
  productId: {
    type: String,
    required: true,
    unique: true  // Evita duplicados del mismo producto
  },
  productName: {
    type: String,
    required: true
  },
  quantity: {
    type: Number,
    required: true,
    min: 0,
    default: 0
  },
  minStock: {
    type: Number,
    default: 5  // Nivel mínimo de stock para alertas
  },
  lastUpdated: {
    type: Date,
    default: Date.now
  }
}, {
  timestamps: true
});

// Método para verificar si hay bajo stock
InventorySchema.methods.isLowStock = function() {
  return this.quantity <= this.minStock;
};

module.exports = mongoose.model("Inventory", InventorySchema);