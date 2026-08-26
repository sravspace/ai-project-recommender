import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import {
    DndContext,
    DragOverlay,
    PointerSensor,
    closestCorners,
    useDraggable,
    useDroppable,
    useSensor,
    useSensors,
} from "@dnd-kit/core";

import type {
    DragEndEvent,
} from "@dnd-kit/core";
import { CSS } from "@dnd-kit/utilities";

import type {
    RoadmapResponse,
    TaskResponse,
    TaskStatus,
} from "../types/roadmap";

import { getActiveRoadmap } from "../services/roadmapService";
import "./RoadmapPage.css";

const columns: {
    status: TaskStatus;
    title: string;
}[] = [
    {
        status: "TODO",
        title: "To Do",
    },
    {
        status: "IN_PROGRESS",
        title: "In Progress",
    },
    {
        status: "DONE",
        title: "Done",
    },
];

interface RoadmapTask extends TaskResponse {
    milestoneId: number;
    milestoneTitle: string;
}

export default function RoadmapPage() {
    const [roadmap, setRoadmap] =
        useState<RoadmapResponse | null>(null);

    const [tasks, setTasks] =
        useState<RoadmapTask[]>([]);

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState<string | null>(null);

    const [activeTask, setActiveTask] =
        useState<RoadmapTask | null>(null);

    const sensors = useSensors(
        useSensor(PointerSensor, {
            activationConstraint: {
                distance: 8,
            },
        })
    );

    useEffect(() => {
        async function loadRoadmap() {
            try {
                const data = await getActiveRoadmap();

                setRoadmap(data);

                const flattenedTasks: RoadmapTask[] =
                    data.milestones.flatMap((milestone) =>
                        milestone.tasks.map((task) => ({
                            ...task,
                            milestoneId: milestone.id,
                            milestoneTitle: milestone.title,
                        }))
                    );

                setTasks(flattenedTasks);
            } catch (err) {
                console.error(err);
                setError("Unable to load your roadmap.");
            } finally {
                setLoading(false);
            }
        }

        loadRoadmap();
    }, []);

    const tasksByStatus = useMemo(() => {
        return {
            TODO: tasks.filter(
                (task) => task.status === "TODO"
            ),

            IN_PROGRESS: tasks.filter(
                (task) => task.status === "IN_PROGRESS"
            ),

            DONE: tasks.filter(
                (task) => task.status === "DONE"
            ),
        };
    }, [tasks]);

    function handleDragStart(event: {
        active: {
            id: string | number;
        };
    }) {
        const taskId = Number(event.active.id);

        const task =
            tasks.find(
                (item) => item.id === taskId
            ) ?? null;

        setActiveTask(task);
    }

    function handleDragEnd(event: DragEndEvent) {
        setActiveTask(null);

        const { active, over } = event;

        if (!over) {
            return;
        }

        const taskId = Number(active.id);

        const newStatus =
            String(over.id) as TaskStatus;

        const isValidStatus =
            columns.some(
                (column) =>
                    column.status === newStatus
            );

        if (!isValidStatus) {
            return;
        }

        setTasks((currentTasks) =>
            currentTasks.map((task) =>
                task.id === taskId
                    ? {
                          ...task,
                          status: newStatus,
                      }
                    : task
            )
        );
    }

    function handleDragCancel() {
        setActiveTask(null);
    }

    if (loading) {
        return (
            <main className="roadmap-state">
                <p>Loading your roadmap...</p>
            </main>
        );
    }

    if (error) {
        return (
            <main className="roadmap-state">
                <h1>Something went wrong.</h1>

                <p>{error}</p>

                <Link to="/dashboard">
                    Back to dashboard
                </Link>
            </main>
        );
    }

    if (!roadmap) {
        return (
            <main className="roadmap-state">
                <h1>No roadmap found.</h1>

                <p>
                    Select a project and generate a
                    roadmap first.
                </p>

                <Link to="/dashboard">
                    Back to dashboard
                </Link>
            </main>
        );
    }

    return (
        <main className="roadmap-page">

            <header className="roadmap-header">

                <div className="roadmap-header-left">

                    <Link
                        to="/dashboard"
                        className="roadmap-back"
                    >
                        ← Dashboard
                    </Link>

                    <div className="roadmap-heading">

                        <p className="roadmap-eyebrow">
                            PROJECT ROADMAP
                        </p>

                        <h1>{roadmap.title}</h1>

                        <p className="roadmap-description">
                            {roadmap.description}
                        </p>

                    </div>

                </div>

                <div className="roadmap-summary">

                    <span className="roadmap-difficulty">
                        {roadmap.difficulty}
                    </span>

                    <span>
                        {tasks.length} tasks
                    </span>

                    <span>
                        {roadmap.milestones.length} milestones
                    </span>

                </div>

            </header>

            <section className="milestone-strip">

                {roadmap.milestones.map(
                    (milestone) => {

                        const milestoneTasks =
                            tasks.filter(
                                (task) =>
                                    task.milestoneId ===
                                    milestone.id
                            );

                        const completedTasks =
                            milestoneTasks.filter(
                                (task) =>
                                    task.status ===
                                    "DONE"
                            ).length;

                        return (
                            <article
                                className="milestone-summary"
                                key={milestone.id}
                            >

                                <span className="milestone-number">
                                    {String(
                                        milestone.milestoneOrder
                                    ).padStart(2, "0")}
                                </span>

                                <div>

                                    <h3>
                                        {milestone.title}
                                    </h3>

                                    <p>
                                        {completedTasks}/
                                        {milestoneTasks.length}{" "}
                                        tasks complete
                                    </p>

                                </div>

                            </article>
                        );
                    }
                )}

            </section>

            <DndContext
                collisionDetection={closestCorners}
                sensors={sensors}
                onDragStart={handleDragStart}
                onDragEnd={handleDragEnd}
                onDragCancel={handleDragCancel}
            >

                <section
                    className="roadmap-board"
                    aria-label="Project task board"
                >

                    {columns.map((column) => {

                        const columnTasks =
                            tasksByStatus[
                                column.status
                            ];

                        return (
                            <BoardColumn
                                key={column.status}
                                status={column.status}
                                title={column.title}
                                tasks={columnTasks}
                            />
                        );
                    })}

                </section>

                <DragOverlay>
                    {activeTask ? (
                        <TaskCard
                            task={activeTask}
                            isDragging
                        />
                    ) : null}
                </DragOverlay>

            </DndContext>

        </main>
    );
}

interface BoardColumnProps {
    status: TaskStatus;
    title: string;
    tasks: RoadmapTask[];
}

function BoardColumn({
    status,
    title,
    tasks,
}: BoardColumnProps) {

    const {
        setNodeRef,
        isOver,
    } = useDroppable({
        id: status,
    });

    return (
        <section
            ref={setNodeRef}
            className={`board-column board-column-${status.toLowerCase()} ${
                isOver
                    ? "board-column-over"
                    : ""
            }`}
        >

            <header className="board-column-header">

                <div>

                    <span className="board-column-marker" />

                    <h2>{title}</h2>

                </div>

                <span className="task-count">
                    {tasks.length}
                </span>

            </header>

            <div className="board-task-list">

                {tasks.length === 0 ? (

                    <div className="empty-column">

                        <span>✦</span>

                        <p>
                            Drop a task here.
                        </p>

                    </div>

                ) : (

                    tasks.map((task) => (
                        <TaskCard
                            key={task.id}
                            task={task}
                        />
                    ))

                )}

            </div>

        </section>
    );
}

function TaskCard({
    task,
    isDragging = false,
}: {
    task: RoadmapTask;
    isDragging?: boolean;
}) {

    const {
        attributes,
        listeners,
        setNodeRef,
        transform,
        isDragging: currentlyDragging,
    } = useDraggable({
        id: task.id,
    });

    const style = {
        transform: CSS.Translate.toString(
            transform
        ),
    };

    return (
        <article
            ref={setNodeRef}
            style={style}
            className={`roadmap-task-card ${
                currentlyDragging || isDragging
                    ? "roadmap-task-card-dragging"
                    : ""
            }`}
            {...listeners}
            {...attributes}
        >

            <div className="task-card-top">

                <span className="task-id">
                    #{String(task.id).padStart(2, "0")}
                </span>

                <span className="task-status-dot" />

            </div>

            <h3>{task.title}</h3>

            <p className="task-milestone">
                {task.milestoneTitle}
            </p>

            {task.description && (
                <p className="task-description">
                    {task.description}
                </p>
            )}

        </article>
    );
}