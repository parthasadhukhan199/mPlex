import React, { useEffect, useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import Header from "./Header";
import { useNavigate } from "react-router-dom";

// Card Component
function SeriesCard({ seriesId, title, description, poster }) {
  const navigate = useNavigate();

  const goToSeriesPage = () => {
    if (!seriesId) {
      console.error("SeriesCard: seriesId is missing!");
      return;
    }
    navigate(`/series/${seriesId}`); // ✔ correct route
  };

  return (
    <motion.div
      onClick={goToSeriesPage}
      whileHover={{ scale: 1.05 }}
      className="bg-gray-800 rounded-xl overflow-hidden shadow-lg cursor-pointer transition"
    >
      <img
        src={`http://192.168.0.109:9000/${poster}`}
        alt={title}
        className="w-full h-56 object-cover"
      />

      <div className="p-4 text-white">
        <h2 className="text-xl font-bold">{title}</h2>
        <p className="text-gray-400 text-sm mt-1">{description}</p>
      </div>
    </motion.div>
  );
}
// Home Page Component
export default function HomePage() {
  const [seriesList, setSeriesList] = useState([]);

  useEffect(() => {
    const fetchSeries = async () => {
      try {
        const token = localStorage.getItem("accessToken");

        const res = await axios.get(
          "http://192.168.0.109:8080/api/v1/user-series/getAll",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setSeriesList(res.data);
      } catch (err) {
        console.error("Error fetching series:", err);
      }
    };

    fetchSeries();
  }, []);

  return (
    <>
      <Header />
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
  {seriesList.map((item) => {
    return (
      <SeriesCard
        key={item.seriesId}
        seriesId={item.seriesId}        // ✅ important
        title={item.title}
        description={item.description}
        poster={item.posterPath}
      />
    );
  })}
</div>
    </>
  );
}
