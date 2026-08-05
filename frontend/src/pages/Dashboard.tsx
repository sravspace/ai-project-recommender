import { useEffect, useState } from "react";
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

        <div style={{ padding: "40px" }}>

            <h1>Welcome, {profile.fullName}! 👋</h1>

            <hr />

            <h3>Bio</h3>
            <p>{profile.bio || "-"}</p>

            <h3>Experience</h3>
            <p>{profile.experienceLevel || "-"}</p>

            <h3>Goals</h3>
            <p>{profile.goals || "-"}</p>

            <h3>Interests</h3>
            <p>{profile.interests || "-"}</p>

            <h3>Time Availability</h3>
            <p>{profile.timeAvailability || "-"}</p>

            <h3>GitHub</h3>
            <p>{profile.githubUrl || "-"}</p>

            <h3>LinkedIn</h3>
            <p>{profile.linkedinUrl || "-"}</p>

        </div>

    );

}