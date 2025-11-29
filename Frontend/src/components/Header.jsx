
import { Link, useNavigate } from "react-router-dom";

export default function Header() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    navigate("/login");
  };

  return (
    <header className="w-full bg-gray-800 text-white p-4 flex items-center justify-between shadow-lg sticky top-0 z-50">
      {/* Left: Logo */}
      <div className="text-2xl font-bold tracking-wide cursor-pointer" onClick={() => navigate("/")}>mPlix</div>

      {/* Center: Navigation */}
      <nav className="hidden md:flex gap-6 text-lg">
        <Link to="/" className="hover:text-purple-400 transition">Home</Link>
        <Link to="/about" className="hover:text-purple-400 transition">About</Link>
      </nav>

      {/* Mobile Menu */}
      <div className="md:hidden flex items-center gap-4">
        <button
          onClick={handleLogout}
          className="bg-red-500 px-4 py-2 rounded-lg hover:bg-red-600 transition"
        >
          Logout
        </button>
      </div>

      {/* Desktop Logout */}
      <button
        onClick={handleLogout}
        className="hidden md:block bg-red-500 px-4 py-2 rounded-lg hover:bg-red-600 transition"
      >
        Logout
      </button>
    </header>
  );
}