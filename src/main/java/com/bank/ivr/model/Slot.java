package com.bank.ivr.model;

import java.util.Arrays;

public class Slot {

    private String slotName;
    private String slotType;
    private String primaryPrompt;
    private String[] noInputPrompts;
    private String[] noMatchPrompts;

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    public String getPrimaryPrompt() {
        return primaryPrompt;
    }

    public void setPrimaryPrompt(String primaryPrompt) {
        this.primaryPrompt = primaryPrompt;
    }

    public String[] getNoInputPrompts() {
        return noInputPrompts;
    }

    public void setNoInputPrompts(String[] noInputPrompts) {
        this.noInputPrompts = noInputPrompts;
    }

    public String[] getNoMatchPrompts() {
        return noMatchPrompts;
    }

    public void setNoMatchPrompts(String[] noMatchPrompts) {
        this.noMatchPrompts = noMatchPrompts;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "slotName='" + slotName + '\'' +
                ", slotType='" + slotType + '\'' +
                ", primaryPrompt='" + primaryPrompt + '\'' +
                ", noInputPrompts=" + Arrays.toString(noInputPrompts) +
                ", noMatchPrompts=" + Arrays.toString(noMatchPrompts) +
                '}';
    }

    public static final class SlotBuilder {
        private String slotName;
        private String slotType;
        private String primaryPrompt;
        private String[] noInputPrompts;
        private String[] noMatchPrompts;

        private SlotBuilder() {
        }

        public static SlotBuilder aSlot() {
            return new SlotBuilder();
        }

        public SlotBuilder withSlotName(String slotName) {
            this.slotName = slotName;
            return this;
        }

        public SlotBuilder withSlotType(String slotType) {
            this.slotType = slotType;
            return this;
        }

        public SlotBuilder withPrimaryPrompt(String primaryPrompt) {
            this.primaryPrompt = primaryPrompt;
            return this;
        }

        public SlotBuilder withNoInputPrompts(String[] noInputPrompts) {
            this.noInputPrompts = noInputPrompts;
            return this;
        }

        public SlotBuilder withNoMatchPrompts(String[] noMatchPrompts) {
            this.noMatchPrompts = noMatchPrompts;
            return this;
        }

        public Slot build() {
            Slot slot = new Slot();
            slot.setSlotName(slotName);
            slot.setSlotType(slotType);
            slot.setPrimaryPrompt(primaryPrompt);
            slot.setNoInputPrompts(noInputPrompts);
            slot.setNoMatchPrompts(noMatchPrompts);
            return slot;
        }
    }
}
