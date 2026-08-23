import { useState } from "react";
import { useNavigate } from "react-router-dom";

import api from "../api/axios";
import { useAuth } from "../context/AuthContext";
import "./LoginPage.css";

export default function LoginPage() {
    const navigate = useNavigate();
    const { login } = useAuth();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    async function handleLogin(e: React.FormEvent) {
        e.preventDefault();

        try {
            const response = await api.post("/auth/login", { email, password });
            login(response.data.token);
            navigate("/dashboard");
        } catch (error) {
            alert("Invalid email or password.");
            console.error(error);
        }
    }

    return (
        <main className="login-page">
            <section className="login-showcase" aria-label="About ProjectPath AI">
                <div className="login-brand"><span>✦</span> ProjectPath AI</div>
                <div className="login-message">
                    <p className="login-kicker">YOUR NEXT BUILD STARTS HERE</p>
                    <h1>Turn your skills into meaningful projects.</h1>
                    <ul>
                        <li>Personalised project ideas</li>
                        <li>Skills-based recommendations</li>
                        <li>Build a portfolio with purpose</li>
                    </ul>
                </div>
                <p className="login-copyright">© 2026 ProjectPath AI</p>
            </section>

            <section className="login-panel">
                <form className="login-card" onSubmit={handleLogin}>
                    <div className="login-mobile-brand"><span>✦</span> ProjectPath AI</div>
                    <div>
                        <p className="login-kicker">WELCOME BACK</p>
                        <h2>Continue your journey.</h2>
                        <p className="login-subtitle">Sign in to see projects picked for your goals.</p>
                    </div>

                    <label htmlFor="email">Email address</label>
                    <input id="email" type="email" placeholder="you@example.com" value={email} onChange={(e) => setEmail(e.target.value)} required />

                    <label htmlFor="password">Password</label>
                    <input id="password" type="password" placeholder="••••••••" value={password} onChange={(e) => setPassword(e.target.value)} required />

                    <button type="submit">Sign in <span aria-hidden="true">→</span></button>
                </form>
            </section>
        </main>
    );
}
