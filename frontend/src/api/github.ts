import { api } from "./client";

export const githubApi = {
  getRepos: () => api.get("/api/github/repos"),

  getPrs: (owner: string, repo: string) =>
    api.get(`/api/github/prs?owner=${owner}&repo=${repo}`),

  getPrFiles: (owner: string, repo: string, prNumber: number) =>
    api.get(`/api/github/prs/${prNumber}/files?owner=${owner}&repo=${repo}`),

  getPrHeadSha: (owner: string, repo: string, prNumber: number) =>
    api.get(`/api/github/prs/${prNumber}/head?owner=${owner}&repo=${repo}`),
};