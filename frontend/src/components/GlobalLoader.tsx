import "../styles/common-loading.scss";
import { useUIStore } from "../store/uiStore";

export default function GlobalLoader() {
  const loading = useUIStore((state) => state.globalLoading);

  if (!loading) return null;

  return (
    <div className="fullscreen-loading">
      <div className="spinner"></div>
      <div className="text">Loading...</div>
    </div>
  );
}