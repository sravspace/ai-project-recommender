export interface RoadmapResponse {
    id: number;
    activeProjectId: number;
    title: string;
    description: string;
    difficulty: string;
    milestones: MilestoneResponse[];
    boardId: number;
}

export interface MilestoneResponse {
    id: number;
    title: string;
    description: string;
    milestoneOrder: number;
    status: TaskStatus;
    tasks: TaskResponse[];
}

export interface TaskResponse {
    id: number;
    title: string;
    description: string | null;
    taskOrder: number;
    status: TaskStatus;
}

export type TaskStatus =
    | "TODO"
    | "IN_PROGRESS"
    | "DONE";