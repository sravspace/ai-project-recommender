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
}

export default function ProfilePage() {

    const [profile, setProfile] = useState<Profile>({
        fullName: "",
        bio: "",
        experienceLevel: "",
        goals: "",
        interests: "",
        timeAvailability: "",
        githubUrl: "",
        linkedinUrl: "",
    });

    const [loading, setLoading] = useState(true);

    useEffect(() => {

        async function loadProfile() {

            try {

                const response = await api.get("/profile/me");

                setProfile(response.data);

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);

            }

        }

        loadProfile();

    }, []);

    async function handleSave() {

        try {

            await api.put("/profile", profile);

            alert("Profile updated successfully!");

        } catch (error) {

            console.error(error);

            alert("Failed to update profile.");

        }

    }

    if (loading) {
        return <h2>Loading...</h2>;
    }

    return (

        <div style={{ padding: "40px", maxWidth: "700px" }}>

            <h1>Edit Profile</h1>

            <br />

            <label>Full Name</label>

            <br />

            <input
                value={profile.fullName}
                onChange={(e) =>
                    setProfile({
                        ...profile,
                        fullName: e.target.value,
                    })
                }
                style={{ width: "100%", padding: "8px" }}
            />

            <br /><br />

            <label>Bio</label>

            <br />

            <textarea
                value={profile.bio}
                onChange={(e) =>
                    setProfile({
                        ...profile,
                        bio: e.target.value,
                    })
                }
                rows={4}
                style={{ width: "100%", padding: "8px" }}
            />

            <br /><br />

            <label>Experience Level</label>

            <br />

            <select
                value={profile.experienceLevel}
                onChange={(e) =>
                    setProfile({
                        ...profile,
                        experienceLevel: e.target.value,
                    })
                }
                style={{ width: "100%", padding: "8px" }}
            >
                <option value="">Select Experience</option>
                <option value="BEGINNER">Beginner</option>
                <option value="INTERMEDIATE">Intermediate</option>
                <option value="ADVANCED">Advanced</option>
            </select>

            <br /><br />

            <label>Goals</label>

            <br />

            <input
                value={profile.goals}
                onChange={(e) =>
                    setProfile({
                        ...profile,
                        goals: e.target.value,
                    })
                }
                style={{ width: "100%", padding: "8px" }}
            />

            <br /><br />

            <label>Interests</label>

            <br />

            <input
                value={profile.interests}
                onChange={(e) =>
                    setProfile({
                        ...profile,
                        interests: e.target.value,
                    })
                }
                style={{ width: "100%", padding: "8px" }}
            />

            <br /><br />

            <label>Time Availability</label>

            <br />

            <input
                value={profile.timeAvailability}
                onChange={(e) =>
                    setProfile({
                        ...profile,
                        timeAvailability: e.target.value,
                    })
                }
                style={{ width: "100%", padding: "8px" }}
            />

            <br /><br />

            <label>GitHub URL</label>

            <br />

            <input
                value={profile.githubUrl}
                onChange={(e) =>
                    setProfile({
                        ...profile,
                        githubUrl: e.target.value,
                    })
                }
                style={{ width: "100%", padding: "8px" }}
            />

            <br /><br />

            <label>LinkedIn URL</label>

            <br />

            <input
                value={profile.linkedinUrl}
                onChange={(e) =>
                    setProfile({
                        ...profile,
                        linkedinUrl: e.target.value,
                    })
                }
                style={{ width: "100%", padding: "8px" }}
            />

            <br /><br />

            <button
                onClick={handleSave}
                style={{
                    padding: "10px 20px",
                    cursor: "pointer"
                }}
            >
                Save Profile
            </button>

        </div>

    );

}