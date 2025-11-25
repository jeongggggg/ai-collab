import { useUIStore } from "../store/uiStore";

export default function GlobalLoader() {
  const loading = useUIStore((state) => state.globalLoading);

  if (!loading) return null;

  return (
    <div
      style={{
        position: "fixed",
        top: 0,
        width: "100%",
        background: "rgba(0,0,0,0.05)",
        padding: "10px",
        textAlign: "center",
        zIndex: 999,
      }}
    >
      Loading...
    </div>
  );
}