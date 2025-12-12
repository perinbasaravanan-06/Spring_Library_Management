import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import Home from "./pages/Home/Home.jsx";
import Login from "./pages/Login/Login.jsx";
import Register from "./pages/Register/Register.jsx";
import "./App.css";

function App() {
  const username = localStorage.getItem("username");
  const role = localStorage.getItem("role");

  const logout = () => {
    localStorage.clear();
    window.location.href = "/login";
  };

  return (
    <BrowserRouter>
      <nav className="navbar">
        <h2 className="logo">📚 Library</h2>

        <div className="nav-links">
          <Link to="/">Home</Link>

          {!username ? (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register">Register</Link>
            </>
          ) : (
            <>
              <span className="username">Hi, {username}</span>
              {role === "ROLE_ADMIN" && (
                <span className="admin-badge">Admin</span>
              )}
              <button onClick={logout} className="logout-btn">
                Logout
              </button>
            </>
          )}
        </div>
      </nav>

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
