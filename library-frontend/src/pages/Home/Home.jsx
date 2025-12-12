import { useEffect, useState } from "react";
import instance from "../../services/axiosInstance";
import "./Home.css";

function Home() {
  const role = localStorage.getItem("role");
  const [books, setBooks] = useState([]);
  const [search, setSearch] = useState("");
  const [form, setForm] = useState({
    title: "",
    author: "",
    category: "",
  });

  const loadBooks = () => {
    instance.get("/api/books").then((res) => setBooks(res.data));
  };

  useEffect(() => {
    loadBooks();
  }, []);

  const addBook = () => {
    if (role !== "ROLE_ADMIN") return alert("Admins only");

    instance.post("/api/books", form).then(() => {
      setForm({ title: "", author: "", category: "" });
      loadBooks();
    });
  };

  const deleteBook = (id) => {
    if (role !== "ROLE_ADMIN") return alert("Admins only");

    instance.delete(`/api/books/${id}`).then(loadBooks);
  };

  return (
    <div className="home-container">
      <h1>Books</h1>

      <input
        className="search-box"
        placeholder="Search by title or author"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      {role === "ROLE_ADMIN" && (
        <div className="add-box">
          <input
            placeholder="Book title"
            value={form.title}
            onChange={(e) => setForm({ ...form, title: e.target.value })}
          />
          <input
            placeholder="Author"
            value={form.author}
            onChange={(e) => setForm({ ...form, author: e.target.value })}
          />
          <input
            placeholder="Category"
            value={form.category}
            onChange={(e) => setForm({ ...form, category: e.target.value })}
          />

          <button onClick={addBook}>Add</button>
        </div>
      )}

      {books
        .filter(
          (b) =>
            b.title.toLowerCase().includes(search.toLowerCase()) ||
            b.author.toLowerCase().includes(search.toLowerCase())
        )
        .map((b) => (
          <div className="book-card" key={b.id}>
            <div>
              <h3>{b.title}</h3>
              <p>{b.author}</p>
              <p className="category">{b.category}</p>
              <p className={b.available ? "available" : "borrowed"}>
                {b.available ? "Available" : "Borrowed"}
              </p>
            </div>

            {role === "ROLE_ADMIN" && (
              <button className="delete-btn" onClick={() => deleteBook(b.id)}>
                Delete
              </button>
            )}
          </div>
        ))}
    </div>
  );
}

export default Home;
