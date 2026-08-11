package kr.hanwinter.quest.quest;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.*;

public class Quest {
    public String getName() {
        return name;
    }

    public Set<String> getPreQuest() {
        return preQuest;
    }

    public List<QuestStep> getSteps() {
        return steps;
    }

    public Integer getExperienceReward() {
        return experienceReward;
    }

    public Integer getMoneyReward() {
        return moneyReward;
    }

    public Boolean getSequential() {
        return isSequential;
    }

    public Quest(String name, Set<String> preQuest, List<QuestStep> steps, Integer experienceReward, Integer moneyReward, Boolean isSequential) {
        this.name = name;
        this.preQuest = preQuest;
        this.steps = steps;
        this.experienceReward = experienceReward;
        this.moneyReward = moneyReward;
        this.isSequential = isSequential;
    }

    private String name;
    private Set<String> preQuest;
    private List<QuestStep> steps;
    private Integer experienceReward;
    private Integer moneyReward;
    private Boolean isSequential;
}
