import { useState } from "react";
import { login } from "../../services/AuthService";
import { useNavigate } from "react-router-dom";
import "./Login.css";

function Login() {
  const [form, setForm] = useState({ username: "", password: "" });
  const navigate = useNavigate();

  const submit = async () => {
    if (!form.username || !form.password)
      return alert("Enter all fields");

    try {
      await login(form.username, form.password);
      alert("Login successful!");
      navigate("/");
    } catch {
      alert("Invalid username or password");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h2>Login</h2>

        <input
          placeholder="Username"
          value={form.username}
          onChange={(e) => setForm({ ...form, username: e.target.value })}
        />

        <input
          type="password"
          placeholder="Password"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
        />

        <button onClick={submit}>Login</button>

        <p className="switch">
          Don’t have an account?{" "}
          <span onClick={() => navigate("/register")}>Register</span>
        </p>
      </div>
    </div>
  );
}

export default Login;
