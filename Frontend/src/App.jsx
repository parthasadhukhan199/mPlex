import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import HomePage from "./components/HomePage";
import LoginSignup from "./components/LoginSignUp";
import SeriesPage from "./components/SerisesPage";

// Protected Route Wrapper
function PrivateRoute({ children }) {
  const token = localStorage.getItem("accessToken");
  return token ? children : <Navigate to="/login" />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Login */}
        <Route path="/login" element={<LoginSignup />} />
        <Route path="/series/:id" element={<SeriesPage />} />

        {/* Home -  */}
        <Route path="/" element={<HomePage />} />

      </Routes>
    </BrowserRouter>
  );
}
