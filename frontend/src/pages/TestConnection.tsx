import { useEffect, useState } from "react";
import { api } from "../api/client";

export default function TestConnection() {
  const [result, setResult] = useState("");

  useEffect(() => {
    api
      .get("/api/ping")
      .then((res) => setResult(res.data))
      .catch(() => setResult("error"));
  }, []);

  return <div>Backend: {result}</div>;
}