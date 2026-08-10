package kr.hanwinter.quest.quest;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest {
    public String getName() {
        return name;
    }

    public List<Component> getDialogue() {
        return dialogue;
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

    public Quest(String name, List<Component> dialogue, List<QuestStep> steps, Integer experienceReward, Integer moneyReward, Boolean isSequential) {
        this.name = name;
        this.dialogue = dialogue;
        this.steps = steps;
        this.experienceReward = experienceReward;
        this.moneyReward = moneyReward;
        this.isSequential = isSequential;
    }

    private String name;
    private List<Component> dialogue;
    private List<QuestStep> steps;
    private Integer experienceReward;
    private Integer moneyReward;
    private Boolean isSequential;
}
