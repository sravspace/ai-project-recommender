import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";
import type {
    ProjectRecommendation,
    RecommendationResponse
} from "../types/recommendation";
import "./Dashboard.css";

interface Profile {
    fullName: string;
    bio: string;
    experienceLevel: string;
    goals: string;
    interests: string;
    timeAvailability: string;
    githubUrl: string;
    linkedinUrl: string;
    profileCompleted: boolean;
}

export default function Dashboard() {
    const [profile, setProfile] = useState<Profile | null>(null);

    const [recommendations, setRecommendations] =
        useState<ProjectRecommendation[]>([]);

    const [loading, setLoading] = useState(true);

    const [recommendationLoading, setRecommendationLoading] =
        useState(true);

    const [selectedProjectId, setSelectedProjectId] =
        useState<number | null>(null);

    const [selectingProjectId, setSelectingProjectId] =
        useState<number | null>(null);

    const hasFetched = useRef(false);

    useEffect(() => {

        async function fetchData() {

            if (hasFetched.current) {
                return;
            }

            hasFetched.current = true;

            try {

                const profileResponse =
                    await api.get("/profile/me");

                setProfile(profileResponse.data);

                const recommendationResponse =
                    await api.get<RecommendationResponse>(
                        "/recommendations/me"
                    );

                setRecommendations(
                    recommendationResponse.data.projects
                );

                const activeProjectResponse =
                    await api.get<ProjectRecommendation | null>(
                        "/recommendations/active"
                    );

                if (activeProjectResponse.data) {

                    setSelectedProjectId(
                        activeProjectResponse.data.id
                    );
                }

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);
                setRecommendationLoading(false);

            }
        }

        fetchData();

    }, []);

    async function handleSelectProject(
        recommendationId: number
    ) {

        try {

            setSelectingProjectId(
                recommendationId
            );

            await api.post(
                `/recommendations/select/${recommendationId}`
            );

            setSelectedProjectId(
                recommendationId
            );

        } catch (error) {

            console.error(error);

            alert(
                "Failed to select project. Please try again."
            );

        } finally {

            setSelectingProjectId(null);

        }
    }

    if (loading) {

        return (
            <main className="dashboard-state">
                Curating your project space…
            </main>
        );
    }

    if (!profile) {

        return (
            <main className="dashboard-state">
                Failed to load profile.
            </main>
        );
    }

    if (!profile.profileCompleted) {

        return (
            <main className="dashboard-state">

                <section className="dashboard-empty-card">

                    <p className="dashboard-eyebrow">
                        PROJECTPATH AI
                    </p>

                    <h1>
                        Your project path starts with your profile.
                    </h1>

                    <p>
                        Tell us about your skills, goals, and
                        interests so we can recommend work that
                        fits you.
                    </p>

                    <Link
                        className="dashboard-button"
                        to="/profile"
                    >
                        Complete profile
                        <span>→</span>
                    </Link>

                </section>

            </main>
        );
    }

    const firstName =
        profile.fullName.trim().split(" ")[0] || "there";

    const profileStrength =
        [
            profile.bio,
            profile.goals,
            profile.interests,
            profile.timeAvailability,
            profile.githubUrl ||
            profile.linkedinUrl
        ].filter(Boolean).length;

    const profileStrengthLabel =
        `${Math.round(
            (profileStrength / 5) * 100
        )}%`;

    return (
        <main className="dashboard-page terracotta-dashboard">

            <header className="dashboard-header">

                <Link
                    className="dashboard-brand"
                    to="/dashboard"
                >
                    <span>✦</span>
                    ProjectPath AI
                </Link>

                <nav>

                    <Link
                        className="active"
                        to="/dashboard"
                    >
                        Dashboard
                    </Link>

                    <Link to="/profile">
                        Profile
                    </Link>

                </nav>

                <Link
                    className="dashboard-profile-link"
                    to="/profile"
                    aria-label="Edit profile"
                >
                    {firstName
                        .slice(0, 1)
                        .toUpperCase()}
                </Link>

            </header>

            <div className="dashboard-shell">

                <section className="dashboard-hero">

                    <p className="dashboard-eyebrow">
                        YOUR CURATED SPACE
                    </p>

                    <h1>
                        Hello, {firstName}.
                    </h1>

                    <p>
                        Ready to build something meaningful?
                    </p>

                </section>

                <section
                    className="dashboard-metrics"
                    aria-label="Dashboard summary"
                >

                    <article>

                        <span className="metric-icon">
                            ✦
                        </span>

                        <p>
                            Profile strength
                        </p>

                        <strong>
                            {profileStrengthLabel}
                        </strong>

                        <small>
                            {profileStrength < 5
                                ? "A few details can make it stronger."
                                : "Your profile is ready to guide recommendations."
                            }
                        </small>

                    </article>

                    <article>

                        <span className="metric-icon">
                            ◌
                        </span>

                        <p>
                            Experience level
                        </p>

                        <strong className="metric-word">
                            {profile.experienceLevel || "—"}
                        </strong>

                        <small>
                            Projects are tailored to your level.
                        </small>

                    </article>

                    <article>

                        <span className="metric-icon">
                            ↗
                        </span>

                        <p>
                            Recommended projects
                        </p>

                        <strong>
                            {recommendations.length}
                        </strong>

                        <small>
                            Based on your goals and interests.
                        </small>

                    </article>

                </section>

                <div className="dashboard-content">

                    <section className="recommendations-section">

                        <div className="section-heading">

                            <div>

                                <p className="dashboard-eyebrow">
                                    MADE FOR YOUR GOALS
                                </p>

                                <h2>
                                    Recommended projects
                                </h2>

                            </div>

                            <span>
                                {recommendations.length} matches
                            </span>

                        </div>

                        {recommendationLoading ? (

                            <div className="dashboard-note">
                                Generating recommendations…
                            </div>

                        ) : recommendations.length === 0 ? (

                            <div className="dashboard-note">
                                No project recommendations are
                                available yet.
                            </div>

                        ) : (

                            <div className="project-list">

                                {recommendations.map(
                                    (project) => (

                                        <ProjectCard
                                            key={project.id}
                                            project={project}
                                            selected={
                                                selectedProjectId ===
                                                project.id
                                            }
                                            selecting={
                                                selectingProjectId ===
                                                project.id
                                            }
                                            onSelect={
                                                handleSelectProject
                                            }
                                        />

                                    )
                                )}

                            </div>

                        )}

                    </section>

                    <aside className="dashboard-sidebar">

                        <section className="sidebar-card sidebar-card-tinted">

                            <p className="dashboard-eyebrow">
                                YOUR PROFILE
                            </p>

                            <h3>
                                Built around you.
                            </h3>

                            <dl>

                                <div>

                                    <dt>
                                        Goals
                                    </dt>

                                    <dd>
                                        {profile.goals ||
                                            "Add your goals"}
                                    </dd>

                                </div>

                                <div>

                                    <dt>
                                        Interests
                                    </dt>

                                    <dd>
                                        {profile.interests ||
                                            "Add your interests"}
                                    </dd>

                                </div>

                                <div>

                                    <dt>
                                        Availability
                                    </dt>

                                    <dd>
                                        {profile.timeAvailability ||
                                            "Add your availability"}
                                    </dd>

                                </div>

                            </dl>

                            <Link
                                className="dashboard-outline-button"
                                to="/profile"
                            >
                                Update profile
                            </Link>

                        </section>

                        <section className="sidebar-card">

                            <p className="dashboard-eyebrow">
                                YOUR LINKS
                            </p>

                            <h3>
                                Your professional home.
                            </h3>

                            <div className="link-stack">

                                {profile.githubUrl ? (

                                    <a
                                        href={profile.githubUrl}
                                        target="_blank"
                                        rel="noreferrer"
                                    >
                                        GitHub ↗
                                    </a>

                                ) : (

                                    <span>
                                        GitHub not added
                                    </span>

                                )}

                                {profile.linkedinUrl ? (

                                    <a
                                        href={profile.linkedinUrl}
                                        target="_blank"
                                        rel="noreferrer"
                                    >
                                        LinkedIn ↗
                                    </a>

                                ) : (

                                    <span>
                                        LinkedIn not added
                                    </span>

                                )}

                            </div>

                        </section>

                    </aside>

                </div>

            </div>

        </main>
    );
}

function ProjectCard({
    project,
    selected,
    selecting,
    onSelect
}: {
    project: ProjectRecommendation;
    selected: boolean;
    selecting: boolean;
    onSelect: (id: number) => void;
}) {

    return (

        <article
            className={`project-card ${
                selected
                    ? "project-card-selected"
                    : ""
            }`}
        >

            <div className="project-card-top">

                <div>

                    <h3>
                        {project.title}
                    </h3>

                    <p>
                        {project.description}
                    </p>

                </div>

                <span
                    className={`difficulty difficulty-${project.difficulty.toLowerCase()}`}
                >
                    {project.difficulty}
                </span>

            </div>

            <div className="project-meta">

                <span>
                    ⌛ {project.estimatedTime.totalWeeks} week
                    {project.estimatedTime.totalWeeks === 1
                        ? ""
                        : "s"}
                </span>

                <span>
                    Learning {project.estimatedTime.learningHours}h
                    {" · "}
                    Building {project.estimatedTime.buildHours}h
                </span>

            </div>

            <div className="tag-row">

                {project.technologies.map(
                    (technology, index) => (

                        <span key={index}>
                            {technology}
                        </span>

                    )
                )}

            </div>

            <div className="project-details">

                <section>

                    <h4>
                        Why it fits
                    </h4>

                    <ul>

                        {project.whyItFits.map(
                            (reason, index) => (

                                <li key={index}>
                                    {reason}
                                </li>

                            )
                        )}

                    </ul>

                </section>

                <section>

                    <h4>
                        You will learn
                    </h4>

                    <ul>

                        {project.youWillLearn.map(
                            (item, index) => (

                                <li key={index}>
                                    {item}
                                </li>

                            )
                        )}

                    </ul>

                </section>

            </div>

            <p className="feasibility">

                <strong>
                    Why this is feasible
                </strong>

                {project.feasibilitySummary}

            </p>

            {project.skillGaps.length > 0 && (

                <div className="skill-gaps">

                    <strong>
                        Growth opportunities
                    </strong>

                    <div className="tag-row">

                        {project.skillGaps.map(
                            (skill, index) => (

                                <span key={index}>
                                    {skill}
                                </span>

                            )
                        )}

                    </div>

                </div>

            )}

            <details>

                <summary>
                    Explore the full roadmap
                </summary>

                <div className="roadmap-grid">

                    <ListBlock
                        title="Resume skills"
                        items={project.resumeSkills}
                    />

                    <ListBlock
                        title="Prerequisites"
                        items={project.prerequisites}
                    />

                    <ListBlock
                        title="Stretch goals"
                        items={project.stretchGoals}
                    />

                </div>

            </details>

            <div className="project-selection">

                <button
                    type="button"
                    className={`select-project-button ${
                        selected
                            ? "selected"
                            : ""
                    }`}
                    onClick={() =>
                        onSelect(project.id)
                    }
                    disabled={selecting}
                >
                    {selecting
                        ? "Selecting..."
                        : selected
                            ? "Selected ✓"
                            : "Select Project"
                    }
                </button>

            </div>

        </article>
    );
}

function ListBlock({
    title,
    items
}: {
    title: string;
    items: string[];
}) {

    return (

        <section>

            <h4>
                {title}
            </h4>

            <ul>

                {items.map(
                    (item, index) => (

                        <li key={index}>
                            {item}
                        </li>

                    )
                )}

            </ul>

        </section>
    );
}