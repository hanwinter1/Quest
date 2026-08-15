package kr.hanwinter.quest.user;

import kr.hanwinter.quest.quest.Quest;
import kr.hanwinter.quest.quest.QuestStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public User(List<Quest> questList, List<Quest> clearedQuests, Map<String, List<Integer>> progressMap) {
        this.questList = questList;
        this.clearedQuests = clearedQuests;
        this.progressMap = progressMap;
    }

    public List<Quest> getQuestList() {
        return questList;
    }

    public List<Quest> getClearedQuests() {
        return clearedQuests;
    }

    public Map<String, List<Integer>> getProgressMap() {
        return progressMap;
    }

    private final List<Quest> questList;
    private final List<Quest> clearedQuests;
    private final Map<String, List<Integer>> progressMap;
}
