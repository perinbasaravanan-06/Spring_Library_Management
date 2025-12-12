import axios from "axios";

const API = "http://localhost:8080/api/auth";

export async function login(username, password) {
  const res = await axios.post(`${API}/login`, { username, password });
  const { token, role, username: name } = res.data;

  localStorage.setItem("token", token);
  localStorage.setItem("role", role);
  localStorage.setItem("username", name);
}

export async function register(username, password) {
  const res = await axios.post(`${API}/register`, { username, password });
  const { token, role, username: name } = res.data;

  localStorage.setItem("token", token);
  localStorage.setItem("role", role);
  localStorage.setItem("username", name);
}

export function logout() {
  localStorage.clear();
}
