import React, { useState, useEffect } from 'react';

import {
  Bar,
  Pie
} from 'react-chartjs-2';

import {
  Chart as ChartJS,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend
);

export default function AnalyticsClient() {

  const [monthlyStats, setMonthlyStats] =
    useState([]);

  const [topProducts, setTopProducts] =
    useState([]);

  useEffect(() => {

    const fetchData = async () => {

      const res1 =
        await fetch(
          "http://localhost:8001/analytics/stats/monthly"
        );

      setMonthlyStats(await res1.json());

      const res2 =
        await fetch(
          "http://localhost:8001/analytics/stats/top-products"
        );

      setTopProducts(await res2.json());
    };

    fetchData();

  }, []);

  const monthlyData = {

    labels:
      monthlyStats.map(
        s => `Mes ${s.month}`
      ),

    datasets: [

      {
        label: 'Ventas Totales',

        data:
          monthlyStats.map(
            s => s.total
          ),

        backgroundColor:
          'rgba(75,192,192,0.6)'
      }

    ]
  };

  const productData = {

    labels:
      topProducts.map(
        p => p._id
      ),

    datasets: [

      {
        label: 'Top Productos',

        data:
          topProducts.map(
            p => p.total
          ),

        backgroundColor: [
          'red',
          'blue',
          'green',
          'orange',
          'purple'
        ]
      }

    ]
  };

  return (

    <div>

      <h2>Analítica de Ventas</h2>

      <h3>Ventas Mensuales</h3>

      <Bar data={monthlyData} />

      <h3>Top Productos</h3>

      <Pie data={productData} />

    </div>
  );
}