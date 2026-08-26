import api from "../api/axios";
import type { RoadmapResponse } from "../types/roadmap";

export async function getActiveRoadmap(): Promise<RoadmapResponse> {
    const response = await api.get<RoadmapResponse>(
        "/roadmap/active"
    );

    return response.data;
}

export async function generateRoadmap(): Promise<RoadmapResponse> {
    const response = await api.post<RoadmapResponse>(
        "/roadmap/generate"
    );

    return response.data;
}