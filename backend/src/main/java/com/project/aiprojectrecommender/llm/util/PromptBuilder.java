package com.project.aiprojectrecommender.llm.util;

import com.project.aiprojectrecommender.entity.UserProfile;
import com.project.aiprojectrecommender.entity.UserSkill;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PromptBuilder {

    public String buildPrompt(UserProfile profile,
                              List<UserSkill> userSkills) {

        String skills = userSkills.stream()
                .map(userSkill -> userSkill.getSkill().getName())
                .collect(Collectors.joining(", "));

        return """
                You are a senior software engineering mentor and technical career advisor.

                Your job is to recommend portfolio-quality software projects for students.

                The student profile is:

                Experience Level:
                %s

                Skills:
                %s

                Goals:
                %s

                Interests:
                %s

                Time Available:
                %s

                Generate EXACTLY 5 project ideas.

                Rules:

                - Projects should gradually increase in difficulty.
                - Avoid generic CRUD applications.
                - Focus on projects that demonstrate real engineering skills.
                - Each project should teach something new.
                - Include technologies worth learning.
                - Estimate how long each project would take.
                - Mention what additional skills the student needs.

                Return ONLY valid JSON.

                Use this schema:

                {
                  "projects":[
                    {
                      "title":"",
                      "description":"",
                      "difficulty":"",
                      "estimatedTime":"",
                      "whyThisProject":"",
                      "resumeValue":"",
                      "learningOutcome":"",
                      "technologies":[],
                      "skillsToLearn":[],
                      "prerequisites":[],
                      "stretchGoals":[]
                    }
                  ]
                }
                """.formatted(

                profile.getExperienceLevel(),
                skills,
                profile.getGoals(),
                profile.getInterests(),
                profile.getTimeAvailability()

        );
    }
}