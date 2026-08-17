package kr.hanwinter.quest.quest;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.*;

public class Quest {
    public String getName() {
        return name;
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

    public Quest(String name, List<QuestStep> steps, Integer experienceReward, Integer moneyReward, Boolean isSequential) {
        this.name = name;
        this.steps = steps;
        this.experienceReward = experienceReward;
        this.moneyReward = moneyReward;
        this.isSequential = isSequential;
    }

    private final String name;
    private final List<QuestStep> steps;
    private final Integer experienceReward;
    private final Integer moneyReward;
    private final Boolean isSequential;
}
