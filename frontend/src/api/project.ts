import { api } from "./client";

export const projectApi = {
  create: (payload: any) => api.post("/api/projects", payload),
};