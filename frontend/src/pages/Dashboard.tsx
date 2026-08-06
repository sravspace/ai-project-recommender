import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api/axios";

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
    const [loading, setLoading] = useState(true);

    useEffect(() => {

        async function fetchProfile() {

            try {

                const response = await api.get("/profile/me");
                setProfile(response.data);

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);

            }

        }

        fetchProfile();

    }, []);

    if (loading) {
        return <h2>Loading...</h2>;
    }

    if (!profile) {
        return <h2>Failed to load profile.</h2>;
    }

    return (

        <div
            style={{
                maxWidth: "900px",
                margin: "40px auto",
                padding: "30px",
                border: "1px solid #444",
                borderRadius: "12px",
            }}
        >

            <h1>Welcome, {profile.fullName}! 👋</h1>

            <div
                style={{
                    marginTop: "30px",
                    padding: "20px",
                    border: "1px solid #555",
                    borderRadius: "10px",
                }}
            >

                <h2>Profile</h2>

                <p><strong>Bio:</strong> {profile.bio || "-"}</p>

                <p><strong>Experience:</strong> {profile.experienceLevel || "-"}</p>

                <p><strong>Goals:</strong> {profile.goals || "-"}</p>

                <p><strong>Interests:</strong> {profile.interests || "-"}</p>

                <p><strong>Time Availability:</strong> {profile.timeAvailability || "-"}</p>

            </div>

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