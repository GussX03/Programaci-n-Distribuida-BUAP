const Inventory = require("../models/Inventory");

// Obtener inventario por productId
const getInventoryByProductId = async (req, res) => {
  try {
    const { productId } = req.params;
    const item = await Inventory.findOne({ productId });
    
    if (!item) {
      return res.status(404).json({ 
        error: "Producto no encontrado en inventario",
        productId 
      });
    }
    
    // Agregar información de bajo stock
    const response = item.toObject();
    response.isLowStock = item.isLowStock();
    
    res.json(response);
  } catch (error) {
    res.status(500).json({ error: "Error al obtener el inventario" });
  }
};

// Crear o actualizar inventario
const createOrUpdateInventory = async (req, res) => {
  try {
    const { productId, productName, quantity } = req.body;
    
    if (!productId || !productName) {
      return res.status(400).json({ error: "productId y productName son requeridos" });
    }
    
    let item = await Inventory.findOne({ productId });
    
    if (item) {
      // Actualizar existente
      item.quantity = quantity !== undefined ? quantity : item.quantity;
      item.productName = productName;
      item.lastUpdated = Date.now();
      await item.save();
    } else {
      // Crear nuevo
      item = new Inventory({ productId, productName, quantity: quantity || 0 });
      await item.save();
    }
    
    const response = item.toObject();
    response.isLowStock = item.isLowStock();
    
    res.json(response);
  } catch (error) {
    res.status(500).json({ error: "Error al crear/actualizar inventario" });
  }
};

// Actualizar cantidad (incrementar o decrementar)
const updateStock = async (req, res) => {
  try {
    const { productId } = req.params;
    const { change } = req.body; // change puede ser positivo o negativo
    
    if (change === undefined) {
      return res.status(400).json({ error: "El campo 'change' es requerido" });
    }
    
    const item = await Inventory.findOne({ productId });
    
    if (!item) {
      return res.status(404).json({ error: "Producto no encontrado en inventario" });
    }
    
    const newQuantity = item.quantity + change;
    
    if (newQuantity < 0) {
      return res.status(400).json({ 
        error: "Stock insuficiente",
        currentStock: item.quantity,
        requestedChange: change
      });
    }
    
    item.quantity = newQuantity;
    item.lastUpdated = Date.now();
    await item.save();
    
    const response = item.toObject();
    response.isLowStock = item.isLowStock();
    
    // Alerta de bajo stock
    if (response.isLowStock) {
      console.log(`⚠️ ALERTA: Stock bajo para ${item.productName} (${item.quantity} unidades)`);
    }
    
    res.json(response);
  } catch (error) {
    res.status(500).json({ error: "Error al actualizar el stock" });
  }
};

// Obtener todos los productos en inventario
const getAllInventory = async (req, res) => {
  try {
    const items = await Inventory.find();
    
    const response = items.map(item => ({
      ...item.toObject(),
      isLowStock: item.isLowStock()
    }));
    
    res.json(response);
  } catch (error) {
    res.status(500).json({ error: "Error al obtener el inventario" });
  }
};

// Obtener productos con bajo stock
const getLowStockItems = async (req, res) => {
  try {
    const items = await Inventory.find();
    const lowStockItems = items.filter(item => item.isLowStock());
    
    res.json({
      count: lowStockItems.length,
      items: lowStockItems
    });
  } catch (error) {
    res.status(500).json({ error: "Error al obtener productos con bajo stock" });
  }
};

// Eliminar producto del inventario
const deleteInventoryItem = async (req, res) => {
  try {
    const { productId } = req.params;
    const item = await Inventory.findOneAndDelete({ productId });
    
    if (!item) {
      return res.status(404).json({ error: "Producto no encontrado en inventario" });
    }
    
    res.json({ message: "Producto eliminado del inventario", productId });
  } catch (error) {
    res.status(500).json({ error: "Error al eliminar el producto" });
  }
};

module.exports = {
  getInventoryByProductId,
  createOrUpdateInventory,
  updateStock,
  getAllInventory,
  getLowStockItems,
  deleteInventoryItem
};