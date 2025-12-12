import { useState } from "react";
import { register } from "../../services/AuthService";
import { useNavigate } from "react-router-dom";
import "./Register.css";

function Register() {
  const [form, setForm] = useState({ username: "", password: "" });
  const navigate = useNavigate();

  const submit = async () => {
    if (!form.username || !form.password)
      return alert("Fill all fields");

    try {
      await register(form.username, form.password);
      alert("Account created!");
      navigate("/");
    } catch {
      alert("Username already exists!");
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h2>Register</h2>

        <input
          placeholder="Choose Username"
          value={form.username}
          onChange={(e) => setForm({ ...form, username: e.target.value })}
        />

        <input
          type="password"
          placeholder="Choose Password"
          value={form.password}
          onChange={(e) => setForm({ ...form, password: e.target.value })}
        />

        <button onClick={submit}>Create Account</button>

        <p className="switch">
          Already have an account?{" "}
          <span onClick={() => navigate("/login")}>Login</span>
        </p>
      </div>
    </div>
  );
}

export default Register;
