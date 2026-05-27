const mongoose = require('mongoose');

const SaleSchema = new mongoose.Schema({
  product: { type: String, required: true },
  amount: { type: Number, required: true },
  date: { type: Date, required: true }
}, { timestamps: true });

module.exports = mongoose.model('Sale', SaleSchema);