import React, { useState, useEffect } from "react";
import { motion } from "framer-motion";
import axios from "axios";
import { useNavigate } from "react-router-dom";


export default function LoginSignup() {

  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    userName: "",
    userEmail: "",
    password: "",
  });
  const [typeText, setTypeText] = useState("");

  const text = "Welcome to mPlix – Stream Your World";

  useEffect(() => {
    let i = 0;
    const interval = setInterval(() => {
      setTypeText(text.slice(0, i));
      i++;
      if (i > text.length) clearInterval(interval);
    }, 80);
    return () => clearInterval(interval);
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    const url = isLogin
      ? "http://192.168.0.109:8080/api/v1/auth/login"
      : "http://192.168.0.109:8080/api/v1/auth/signup";

    const body = isLogin
      ? { userName: formData.userName, password: formData.password }
      : {
          userName: formData.userName,
          userEmail: formData.userEmail,
          password: formData.password,
        };

    try {
      const res = await axios.post(url, body, {
        headers: { "Content-Type": "application/json" },
      });

      const data = res.data;

      if (data.token) {
        localStorage.setItem("accessToken", data.token.accessToken);
        localStorage.setItem("refreshToken", data.token.refreshToken);
      }
      navigate("/");
    } catch (error) {
      console.error("Error:", error);
    }

    try {
      const res = await axios.post(url, body, {
        headers: { "Content-Type": "application/json" },
      });

      const data = res.data;

      if (data.token) {
        localStorage.setItem("accessToken", data.token.accessToken);
        localStorage.setItem("refreshToken", data.token.refreshToken);
      }
    } catch (error) {
      if (error.response) {
        alert(error.response.data);
      } else {
        alert("Something went wrong");
      }
    }
  };

  useEffect(() => {
    const access = localStorage.getItem("accessToken");
    const refresh = localStorage.getItem("refreshToken");
    if (access && refresh) {
      console.log("Auto Login Enabled");
    }
  }, []);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 p-6">
      <div className="grid grid-cols-1 md:grid-cols-2 w-full max-w-5xl bg-gray-800 rounded-2xl overflow-hidden shadow-2xl">
        {/* LEFT HERO SECTION */}
        <div className="flex flex-col items-start justify-center p-10 text-white bg-gradient-to-br from-purple-700 to-blue-600">
          <motion.h1
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 1 }}
            className="text-4xl font-bold mb-4"
          >
            {typeText}
          </motion.h1>
          <p className="opacity-80">
            Your entertainment hub – Login or Signup to continue.
          </p>
        </div>

        {/* RIGHT FORM SECTION */}
        <div className="p-10 flex flex-col justify-center text-white">
          <h2 className="text-3xl font-bold mb-6 text-center">
            {isLogin ? "Login" : "Sign Up"}
          </h2>

          <div className="space-y-4">
            <input
              type="text"
              name="userName"
              placeholder="Username"
              value={formData.userName}
              onChange={handleChange}
              className="w-full p-3 rounded-lg bg-gray-700 focus:outline-none"
            />

            {!isLogin && (
              <input
                type="email"
                name="userEmail"
                placeholder="Email"
                value={formData.userEmail}
                onChange={handleChange}
                className="w-full p-3 rounded-lg bg-gray-700 focus:outline-none"
              />
            )}

            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              className="w-full p-3 rounded-lg bg-gray-700 focus:outline-none"
            />

            <button
              onClick={handleSubmit}
              className="w-full p-3 bg-blue-500 rounded-lg font-semibold hover:bg-blue-600 transition"
            >
              {isLogin ? "Login" : "Sign Up"}
            </button>
          </div>

          <p className="mt-6 text-center">
            {isLogin ? "Don't have an account?" : "Already registered?"}
            <span
              className="text-blue-400 cursor-pointer ml-1"
              onClick={() => setIsLogin(!isLogin)}
            >
              {isLogin ? "Sign Up" : "Login"}
            </span>
          </p>
        </div>
      </div>
    </div>
  );
}
