import { api } from "./client";

export const githubApi = {
  getRepos: () => api.get("/api/github/repos"),
  getPrs: (owner: string, repo: string) =>
    api.get(`/api/github/prs?owner=${owner}&repo=${repo}`),
};