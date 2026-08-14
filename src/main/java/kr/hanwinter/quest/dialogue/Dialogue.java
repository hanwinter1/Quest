package kr.hanwinter.quest.dialogue;

import java.util.List;

public class Dialogue {
    public Dialogue(String name, String displayName, List<String> dialogue, String quest, List<String> preQuest, int reqLevel) {
        this.name = name;
        this.displayName = displayName;
        this.dialogue = dialogue;
        this.quest = quest;
        this.preQuest = preQuest;
        this.reqLevel = reqLevel;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getDialogue() {
        return dialogue;
    }

    public String getQuest() {
        return quest;
    }

    public List<String> getPreQuest() {
        return preQuest;
    }

    public int getReqLevel() {
        return reqLevel;
    }

    private String name;
    private String displayName;
    private List<String> dialogue;
    private String quest;
    private List<String> preQuest;
    private int reqLevel;
}
