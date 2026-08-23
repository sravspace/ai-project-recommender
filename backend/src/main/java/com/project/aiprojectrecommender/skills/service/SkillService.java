package com.project.aiprojectrecommender.skills.service;

import com.project.aiprojectrecommender.entity.Skill;
import com.project.aiprojectrecommender.repository.SkillRepository;
import com.project.aiprojectrecommender.skills.dto.SkillRequest;
import com.project.aiprojectrecommender.skills.dto.SkillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<SkillResponse> getAllSkills() {

        return skillRepository.findAll()
                .stream()
                .map(skill -> SkillResponse.builder()
                        .id(skill.getId())
                        .name(skill.getName())
                        .category(skill.getCategory())
                        .build())
                .toList();
    }

    public SkillResponse createSkill(SkillRequest request) {

        if (skillRepository.existsByName(request.getName())) {
            throw new RuntimeException("Skill already exists.");
        }

        Skill skill = Skill.builder()
                .name(request.getName())
                .category(request.getCategory())
                .build();

        skill = skillRepository.save(skill);

        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .build();
    }

    public List<SkillResponse> createSkillsBulk(
            List<SkillRequest> requests) {

        return requests.stream()
                .filter(request ->
                        !skillRepository.existsByName(request.getName()))
                .map(this::createSkill)
                .toList();
    }
}