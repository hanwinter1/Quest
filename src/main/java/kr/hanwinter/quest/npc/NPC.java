package kr.hanwinter.quest.npc;

import java.util.List;

public class NPC {
    public NPC(int id, String basicDialogue, List<String> dialogueList) {
        this.id = id;
        this.basicDialogue = basicDialogue;
        this.dialogueList = dialogueList;
    }

    public int getID() {
        return id;
    }

    public String getBasicDialogue() {
        return basicDialogue;
    }

    public List<String> getDialogueList() {
        return dialogueList;
    }

    private int id;
    private String basicDialogue;
    private List<String> dialogueList;
}
