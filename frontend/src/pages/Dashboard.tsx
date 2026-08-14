import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";
import type {
    ProjectRecommendation,
    RecommendationResponse,
} from "../types/recommendation";

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
    const [recommendations, setRecommendations] = useState<
        ProjectRecommendation[]
    >([]);
    const [loading, setLoading] = useState(true);
    const [recommendationLoading, setRecommendationLoading] = useState(true);

    useEffect(() => {
        async function fetchData() {
            try {
                const profileResponse = await api.get("/profile/me");
                setProfile(profileResponse.data);

                const recommendationResponse =
                    await api.get<RecommendationResponse>(
                        "/recommendations/me"
                    );

                setRecommendations(
                    recommendationResponse.data.projects
                );
            } catch (error) {
                console.error(error);
            } finally {
                setLoading(false);
                setRecommendationLoading(false);
            }
        }

        fetchData();
    }, []);

    if (loading) {
        return <h2>Loading...</h2>;
    }

    if (!profile) {
        return <h2>Failed to load profile.</h2>;
    }

    if (!profile.profileCompleted) {
        return (
            <div
                style={{
                    maxWidth: "900px",
                    margin: "80px auto",
                    padding: "40px",
                    textAlign: "center",
                }}
            >
                <h1>Complete Your Profile</h1>

                <p>
                    Complete your profile to receive personalized project
                    recommendations.
                </p>

                <Link to="/profile">
                    <button
                        style={{
                            marginTop: "20px",
                            padding: "12px 24px",
                            cursor: "pointer",
                            fontSize: "16px",
                        }}
                    >
                        Complete Profile
                    </button>
                </Link>
            </div>
        );
    }

    return (
        <div
            style={{
                maxWidth: "1000px",
                margin: "40px auto",
                padding: "30px",
            }}
        >
            <h1>Welcome, {profile.fullName}! 👋</h1>

            {/* PROFILE */}
            <div
                style={{
                    marginTop: "30px",
                    padding: "20px",
                    border: "1px solid #555",
                    borderRadius: "10px",
                }}
            >
                <h2>Profile</h2>

                <p>
                    <strong>Bio:</strong> {profile.bio || "-"}
                </p>

                <p>
                    <strong>Experience:</strong>{" "}
                    {profile.experienceLevel || "-"}
                </p>

                <p>
                    <strong>Goals:</strong> {profile.goals || "-"}
                </p>

                <p>
                    <strong>Interests:</strong>{" "}
                    {profile.interests || "-"}
                </p>

                <p>
                    <strong>Time Availability:</strong>{" "}
                    {profile.timeAvailability || "-"}
                </p>
            </div>

            {/* LINKS */}
            <div
                style={{
                    marginTop: "20px",
                    padding: "20px",
                    border: "1px solid #555",
                    borderRadius: "10px",
                }}
            >
                <h2>Links</h2>

                <p>
                    <strong>GitHub:</strong>{" "}
                    {profile.githubUrl || "-"}
                </p>

                <p>
                    <strong>LinkedIn:</strong>{" "}
                    {profile.linkedinUrl || "-"}
                </p>
            </div>

            {/* RECOMMENDATIONS */}
            <div style={{ marginTop: "40px" }}>
                <h2>Recommended Projects</h2>

                {recommendationLoading ? (
                    <p>Generating recommendations...</p>
                ) : recommendations.length === 0 ? (
                    <p>No project recommendations available.</p>
                ) : (
                    <div
                        style={{
                            display: "grid",
                            gridTemplateColumns:
                                "repeat(auto-fit, minmax(300px, 1fr))",
                            gap: "20px",
                            marginTop: "20px",
                        }}
                    >
                        {recommendations.map((project, index) => (
                            <div
                                key={index}
                                style={{
                                    padding: "24px",
                                    border: "1px solid #555",
                                    borderRadius: "12px",
                                }}
                            >
                                <h3>{project.title}</h3>

                                <p>{project.description}</p>

                                {/* DIFFICULTY */}
                                <p>
                                    <strong>Difficulty:</strong>{" "}
                                    {project.difficulty}
                                </p>

                                {/* TIME */}
                                <div
                                    style={{
                                        marginTop: "15px",
                                        padding: "15px",
                                        border: "1px solid #555",
                                        borderRadius: "10px",
                                    }}
                                >
                                    <h4
                                        style={{
                                            marginTop: 0,
                                            marginBottom: "10px",
                                        }}
                                    >
                                        Estimated Time
                                    </h4>

                                    <p>
                                        <strong>Learning:</strong>{" "}
                                        {project.estimatedTime.learningHours}{" "}
                                        hours
                                    </p>

                                    <p>
                                        <strong>Building:</strong>{" "}
                                        {project.estimatedTime.buildHours}{" "}
                                        hours
                                    </p>

                                    <p>
                                        <strong>Total:</strong>{" "}
                                        {project.estimatedTime.totalWeeks}{" "}
                                        weeks
                                    </p>
                                </div>

                                {/* FEASIBILITY */}
                                <div
                                    style={{
                                        marginTop: "15px",
                                        padding: "15px",
                                        border: "1px solid #555",
                                        borderRadius: "10px",
                                    }}
                                >
                                    <h4
                                        style={{
                                            marginTop: 0,
                                            marginBottom: "10px",
                                        }}
                                    >
                                        Why This Is Feasible
                                    </h4>

                                    <p style={{ marginBottom: 0 }}>
                                        {project.feasibilitySummary}
                                    </p>
                                </div>

                                {/* WHY IT FITS */}
                                <div style={{ marginTop: "20px" }}>
                                    <h4>Why It Fits</h4>

                                    <ul>
                                        {project.whyItFits.map(
                                            (reason, reasonIndex) => (
                                                <li key={reasonIndex}>
                                                    {reason}
                                                </li>
                                            )
                                        )}
                                    </ul>
                                </div>

                                {/* SKILL GAPS */}
                                <div
                                    style={{
                                        marginTop: "20px",
                                        padding: "16px",
                                        border: "1px solid #b45309",
                                        borderRadius: "10px",
                                        backgroundColor:
                                            "rgba(180, 83, 9, 0.08)",
                                    }}
                                >
                                    <h4
                                        style={{
                                            marginTop: 0,
                                            marginBottom: "12px",
                                        }}
                                    >
                                        Skill Gaps
                                    </h4>

                                    {project.skillGaps.length === 0 ? (
                                        <p>No major skill gaps.</p>
                                    ) : (
                                        <div
                                            style={{
                                                display: "flex",
                                                flexWrap: "wrap",
                                                gap: "8px",
                                            }}
                                        >
                                            {project.skillGaps.map(
                                                (skill, skillIndex) => (
                                                    <span
                                                        key={skillIndex}
                                                        style={{
                                                            padding:
                                                                "6px 10px",
                                                            border: "1px solid #b45309",
                                                            borderRadius:
                                                                "20px",
                                                            fontSize:
                                                                "14px",
                                                        }}
                                                    >
                                                        {skill}
                                                    </span>
                                                )
                                            )}
                                        </div>
                                    )}
                                </div>

                                {/* WHAT YOU WILL LEARN */}
                                <div style={{ marginTop: "20px" }}>
                                    <h4>You Will Learn</h4>

                                    <ul>
                                        {project.youWillLearn.map(
                                            (item, itemIndex) => (
                                                <li key={itemIndex}>
                                                    {item}
                                                </li>
                                            )
                                        )}
                                    </ul>
                                </div>

                                {/* RESUME SKILLS */}
                                <div style={{ marginTop: "20px" }}>
                                    <h4>Resume Skills</h4>

                                    <ul>
                                        {project.resumeSkills.map(
                                            (skill, skillIndex) => (
                                                <li key={skillIndex}>
                                                    {skill}
                                                </li>
                                            )
                                        )}
                                    </ul>
                                </div>

                                {/* TECHNOLOGIES */}
                                <div style={{ marginTop: "20px" }}>
                                    <strong>Technologies:</strong>

                                    <ul>
                                        {project.technologies.map(
                                            (
                                                technology,
                                                technologyIndex
                                            ) => (
                                                <li
                                                    key={technologyIndex}
                                                >
                                                    {technology}
                                                </li>
                                            )
                                        )}
                                    </ul>
                                </div>

                                {/* PREREQUISITES */}
                                <div style={{ marginTop: "20px" }}>
                                    <h4>Prerequisites</h4>

                                    <ul>
                                        {project.prerequisites.map(
                                            (
                                                prerequisite,
                                                prerequisiteIndex
                                            ) => (
                                                <li
                                                    key={
                                                        prerequisiteIndex
                                                    }
                                                >
                                                    {prerequisite}
                                                </li>
                                            )
                                        )}
                                    </ul>
                                </div>

                                {/* STRETCH GOALS */}
                                <div style={{ marginTop: "20px" }}>
                                    <h4>Stretch Goals</h4>

                                    <ul>
                                        {project.stretchGoals.map(
                                            (
                                                goal,
                                                goalIndex
                                            ) => (
                                                <li key={goalIndex}>
                                                    {goal}
                                                </li>
                                            )
                                        )}
                                    </ul>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {/* EDIT PROFILE */}
            <div style={{ marginTop: "30px" }}>
                <Link to="/profile">
                    <button
                        style={{
                            padding: "10px 20px",
                            cursor: "pointer",
                            fontSize: "16px",
                        }}
                    >
                        Edit Profile
                    </button>
                </Link>
            </div>
        </div>
    );
}