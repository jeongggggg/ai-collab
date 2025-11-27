import { api } from "./client";

export const uploadApi = {
  createUpload: (projectId: number, commitSha: string) =>
    api.post(`/api/projects/${projectId}/uploads`, { commitSha }),

  getUploadDetail: (projectId: number, uploadId: number) =>
    api.get(`/api/projects/${projectId}/uploads/${uploadId}`),
};