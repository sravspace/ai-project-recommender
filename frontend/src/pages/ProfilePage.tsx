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

interface Skill {
    id: number;
    name: string;
    category: string;
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

    const [skills, setSkills] = useState<Skill[]>([]);
    const [selectedSkillId, setSelectedSkillId] = useState("");
    const [selectedSkills, setSelectedSkills] = useState<Skill[]>([]);

    useEffect(() => {

        async function loadData() {

            try {

                const profileResponse = await api.get("/profile/me");
                setProfile(profileResponse.data);

                const skillsResponse = await api.get("/skills");
                setSkills(skillsResponse.data);

                const mySkillsResponse = await api.get("/profile/skills");

                setSelectedSkills(
                    mySkillsResponse.data.map((s: any) => ({
                        id: s.skillId,
                        name: s.skillName,
                        category: s.category
                    }))
                );

            } catch (error) {

                console.error(error);

            } finally {

                setLoading(false);

            }

        }

        loadData();

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

    async function handleAddSkill() {

        if (!selectedSkillId) return;

        try {

            await api.post("/profile/skills", {
                skillId: Number(selectedSkillId),
                proficiency: "BEGINNER"
            });

            const mySkillsResponse = await api.get("/profile/skills");

            setSelectedSkills(
                mySkillsResponse.data.map((s: any) => ({
                    id: s.skillId,
                    name: s.skillName,
                    category: s.category
                }))
            );

            setSelectedSkillId("");

        } catch (error) {

            console.error(error);

            alert("Failed to add skill.");

        }

    }

    function removeSkill(id: number) {

        setSelectedSkills(
            selectedSkills.filter(
                skill => skill.id !== id
            )
        );

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

            <h2>Skills</h2>

            <select
                value={selectedSkillId}
                onChange={(e) => setSelectedSkillId(e.target.value)}
                style={{
                    width: "100%",
                    padding: "8px"
                }}
            >
                <option value="">Select Skill</option>

                {skills.map(skill => (
                    <option
                        key={skill.id}
                        value={skill.id}
                    >
                        {skill.name} ({skill.category})
                    </option>
                ))}
            </select>

            <br /><br />

            <button
                onClick={handleAddSkill}
            >
                Add Skill
            </button>

            <br /><br />

            {selectedSkills.map(skill => (

                <div
                    key={skill.id}
                    style={{
                        display: "inline-block",
                        padding: "8px 12px",
                        margin: "5px",
                        background: "#1976d2",
                        color: "white",
                        borderRadius: "20px"
                    }}
                >
                    {skill.name}

                    <button
                        onClick={() => removeSkill(skill.id)}
                        style={{
                            marginLeft: "10px",
                            cursor: "pointer"
                        }}
                    >
                        ✕
                    </button>

                </div>

            ))}

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