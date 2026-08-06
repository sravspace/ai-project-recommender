package com.project.aiprojectrecommender.skills.service;

import com.project.aiprojectrecommender.entity.Skill;
import com.project.aiprojectrecommender.entity.User;
import com.project.aiprojectrecommender.entity.UserSkill;
import com.project.aiprojectrecommender.enums.Proficiency;
import com.project.aiprojectrecommender.repository.SkillRepository;
import com.project.aiprojectrecommender.repository.UserRepository;
import com.project.aiprojectrecommender.repository.UserSkillRepository;
import com.project.aiprojectrecommender.skills.dto.UserSkillRequest;
import com.project.aiprojectrecommender.skills.dto.UserSkillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSkillService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;

    public List<UserSkillResponse> getMySkills(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found."));

        return userSkillRepository.findByUser(user)
                .stream()
                .map(userSkill -> UserSkillResponse.builder()
                        .id(userSkill.getId())
                        .skillId(userSkill.getSkill().getId())
                        .skillName(userSkill.getSkill().getName())
                        .category(userSkill.getSkill().getCategory())
                        .proficiency(userSkill.getProficiency().name())
                        .verified(userSkill.getVerified())
                        .build())
                .toList();
    }

    public void addSkill(
            Authentication authentication,
            UserSkillRequest request) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found."));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found."));

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .proficiency(
                        Proficiency.valueOf(request.getProficiency())
                )
                .verified(false)
                .build();

        userSkillRepository.save(userSkill);

    }

}
